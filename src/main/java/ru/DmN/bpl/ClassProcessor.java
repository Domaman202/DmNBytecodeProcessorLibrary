package ru.DmN.bpl;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import ru.DmN.bpl.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public class ClassProcessor extends ClassNode {
    public ClassProcessor() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();

        var bcprocessor = false;

        String cnm = null;
        Map<String, String> dm = new HashMap<>();

        var annotations = CollectionsHelper.combine(this.visibleAnnotations, this.invisibleAnnotations);
        for (AnnotationNode annotationNode : annotations) {
            var desc = processDescriptor(annotationNode.desc);
            if (desc.startsWith("ru/DmN/bpl/annotations/")) {
                var data = wrapAnnData(annotationNode.values);
                switch (desc.substring(desc.lastIndexOf('/') + 1)) {
                    case "BytecodeProcessor" -> bcprocessor = true;
                    case "Extends" -> {
                        if  (data.containsKey("extend"))
                            this.superName = (String) data.get("extend");
                        if (data.containsKey("deleteImpls") && (boolean) data.get("deleteImpls"))
                            this.interfaces = new ArrayList<>();
                        if (data.containsKey("impl"))
                            this.interfaces.addAll((List<String>) data.get("impl"));
                    }
                    case "TRename" -> {
                        cnm = this.name;
                        this.name = (String) data.get("value");
                    }
                    case "DescMap" -> {
                        var odesc = ((List<String>) data.get("odesc"));
                        var ndesc = ((List<String>) data.get("ndesc"));
                        for (int j = 0; j < odesc.size(); j++)
                            dm.put(odesc.get(j), ndesc.get(j));
                    }
                    case "Annotations" -> {
                        if (this.visibleAnnotations == null)
                            this.visibleAnnotations = new ArrayList<>();
                        ((List<String>) data.get("value")).forEach(d -> this.visibleAnnotations.add(new AnnotationNode(d)));
                    }
                }
            }
        }

        var ffr = new ArrayList<FieldNode>();
        fields:
        for (var field : this.fields) {
            for (var annotation : CollectionsHelper.combine(field.visibleAnnotations, field.invisibleAnnotations)) {
                var data = wrapAnnData(annotation.values);
                switch (processDescriptor(annotation.desc)) {
                    case "ru/DmN/bpl/annotations/Delete" -> {
                        ffr.add(field);
                        continue fields;
                    }
                    case "ru/DmN/bpl/annotations/Annotations" -> {
                        if (field.visibleAnnotations == null)
                            field.visibleAnnotations = new ArrayList<>();
                        ((List<String>) data.get("value")).forEach(d -> field.visibleAnnotations.add(new AnnotationNode(d)));
                    }
                    case "ru/DmN/bpl/annotations/FMRename" -> {
                        if  (data.containsKey("name"))
                            field.name = (String) data.get("name");
                        if  (data.containsKey("desc"))
                            field.desc = (String) data.get("desc");
                        if (data.containsKey("sign"))
                            field.signature = (String) data.get("sign");
                    }
                }
            }
        }
        ffr.forEach(f -> fields.remove(f));

        var mfr = new ArrayList<MethodNode>();
        methods:
        for (var method : this.methods) {
            var bcprocessed = false;

            for (var annotation : CollectionsHelper.combine(method.visibleAnnotations, method.invisibleAnnotations)) {
                var data = wrapAnnData(annotation.values);
                var desc = processDescriptor(annotation.desc);
                switch (desc) {
                    case "ru/DmN/bpl/annotations/ExtendSL" -> {
                        if (data.containsKey("stack"))
                            method.maxStack += (int) data.get("stack");
                        if (data.containsKey("local"))
                            method.maxLocals += (int) data.get("local");
                    }
                    case "ru/DmN/bpl/annotations/BytecodeProcessor" -> {
                        this.processBytecode(method);
                        bcprocessed = true;
                    }
                    case "ru/DmN/bpl/annotations/MakeConstructor" -> method.name = "<init>";
                    case "ru/DmN/bpl/annotations/MakeClassInit" -> {
                        for (var m : this.methods) {
                            if (m.name.equals("<clinit>")) {
                                this.methods.remove(m);
                                break;
                            }
                        }
                        method.name = "<clinit>";
                    }
                    case "ru/DmN/bpl/annotations/Delete" -> {
                        mfr.add(method);
                        continue methods;
                    }
                    case "ru/DmN/bpl/annotations/Annotations" -> {
                        if (method.visibleAnnotations == null)
                            method.visibleAnnotations = new ArrayList<>();
                        ((List<String>) data.get("value")).forEach(d -> method.visibleAnnotations.add(new AnnotationNode(d)));
                    }
                    case "ru/DmN/bpl/annotations/FMRename" -> {
                        if  (data.containsKey("name"))
                            method.name = (String) data.get("name");
                        if  (data.containsKey("desc")) {
                            method.desc = (String) data.get("desc");
                            if (data.containsKey("sign"))
                                method.signature = (String) data.get("sign");
                            else method.signature = method.desc;
                        } else if (data.containsKey("sign"))
                            method.signature = (String) data.get("sign");
                    }
                }
            }

            if (bcprocessor && !bcprocessed)
                this.processBytecode(method);

            if (cnm != null)
                remap(method, cnm, this.name);
            for (var entry : dm.entrySet())
                remap(method, entry.getKey(), entry.getValue());
        }
        mfr.forEach(m -> methods.remove(m));
    }

    public static void remap(MethodNode method, String o, String n) {
        for (var instance : method.instructions) {
            if (instance instanceof FieldInsnNode node) {
                node.owner = node.owner.replace(o, n);
                node.desc = node.desc.replace(o, n);
            } else if (instance instanceof MethodInsnNode node) {
                node.owner = node.owner.replace(o, n);
                node.desc = node.desc.replace(o, n);
            } else if (instance instanceof LdcInsnNode node) {
                if (node.cst instanceof String)
                    node.cst = ((String) node.cst).replace(o, n);
            } else if (instance instanceof InvokeDynamicInsnNode node) {
                node.desc = node.desc.replace(o, n);
                node.bsm = new Handle(node.bsm.getTag(), node.bsm.getOwner().replace(o, n), node.bsm.getName(), node.bsm.getDesc().replace(o, n), node.bsm.isInterface());
                for (int i = 0; i < node.bsmArgs.length; i++) {
                    var arg = node.bsmArgs[i];
                    if (arg instanceof String str)
                        node.bsmArgs[i] = str.replace(o, n);
                    else if (arg instanceof Handle handle)
                        node.bsmArgs[i] = new Handle(handle.getTag(), handle.getOwner().replace(o, n), handle.getName(), handle.getDesc().replace(o, n), handle.isInterface());
                }
            } else if (instance instanceof TypeInsnNode node)
                node.desc = node.desc.replace(o, n);
        }
    }

    public void processBytecode(MethodNode method) {
        var labels = new LabelMap(method);

        var delmode = false;
        for (int i = 0; i < method.instructions.size(); i++) {
            var instr0 = method.instructions.get(i);
            if (delmode) {
                method.instructions.remove(instr0);
                i--;
            }
            if (instr0 instanceof MethodInsnNode instr) {
                if (instr.owner.equals(BytecodeUtils.CLASS_NAME)) {
                    if (delmode && instr.name.equals("spec$endDelete"))
                        delmode = false;
                    switch (instr.name) {
                        case "spec$startDelete" -> {
                            method.instructions.remove(instr);
                            delmode = true;
                        }
                        case "spec$fakeUsage" -> {
                            method.instructions.remove(instr);
                            method.instructions.remove(method.instructions.get(--i));
                        }
                        case "spec$label" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, labels.get(((LdcInsnNode) inst).cst));
                        }
                        case "spec$opcode" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, new InsnNode((Integer) parseObject(inst)));
                        }
                        case "nop" -> replaceOpcode(method, instr, Opcodes.NOP);
                        case "ldc$mh" -> {
                            var inst0 = method.instructions.get(--i); // isInterface
                            method.instructions.remove(inst0);
                            var inst1 = method.instructions.get(--i); // desc
                            method.instructions.remove(inst1);
                            var inst2 = method.instructions.get(--i); // name
                            method.instructions.remove(inst2);
                            var inst3 = method.instructions.get(--i); // owner
                            method.instructions.remove(inst3);
                            var inst4 = method.instructions.get(--i); // tag
                            method.instructions.remove(inst4);
                            replaceOpcode(method, instr, new LdcInsnNode(new Handle(
                                    (int) parseObject(inst4),
                                    (String) parseObject(inst3),
                                    (String) parseObject(inst2),
                                    (String) parseObject(inst1),
                                    (int) parseObject(inst0) == 1
                            )));
                        }
                        case "ldc$mt" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, new LdcInsnNode(Type.getType((String) ((LdcInsnNode) inst).cst)));
                        }
                        case "ldc$class" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, new LdcInsnNode(Type.getType("L" + ((LdcInsnNode) inst).cst + ";")));
                        }
                        case "load" -> method.instructions.remove(instr);
                        case "pop" -> replaceOpcode(method, instr, Opcodes.POP);
                        case "pop2" -> replaceOpcode(method, instr, Opcodes.POP2);
                        case "dup" -> replaceOpcode(method, instr, Opcodes.DUP);
                        case "dup_x1" -> replaceOpcode(method, instr, Opcodes.DUP_X1);
                        case "dup_x2" -> replaceOpcode(method, instr, Opcodes.DUP_X2);
                        case "dup2" -> replaceOpcode(method, instr, Opcodes.DUP2);
                        case "dup2_x1" -> replaceOpcode(method, instr, Opcodes.DUP2_X1);
                        case "dup2_x2" -> replaceOpcode(method, instr, Opcodes.DUP2_X2);
                        case "swap" -> replaceOpcode(method, instr, Opcodes.SWAP);
                        case "jmp" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, new JumpInsnNode(Opcodes.GOTO, labels.get(((LdcInsnNode) inst).cst)));
                        }
                        case "alloc" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, new TypeInsnNode(Opcodes.NEW, (String) ((LdcInsnNode) inst).cst));
                        }
                        case "athrow" -> replaceOpcode(method, instr, Opcodes.ATHROW);
                        case "checkcast" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, new TypeInsnNode(Opcodes.CHECKCAST, (String) ((LdcInsnNode) inst).cst));
                        }
                        case "instanceOf" -> {
                            var inst = method.instructions.get(--i);
                            method.instructions.remove(inst);
                            replaceOpcode(method, instr, new TypeInsnNode(Opcodes.INSTANCEOF, (String) ((LdcInsnNode) inst).cst));
                        }
                        case "monitorenter" -> replaceOpcode(method, instr, Opcodes.MONITORENTER);
                        case "monitorexit" -> replaceOpcode(method, instr, Opcodes.MONITOREXIT);
                    }
                }
            } else if (instr0 instanceof TypeInsnNode instr) {
                if (instr.desc.equals(CallBuilder.CLASS_NAME)) {
                    method.instructions.remove(instr);
                    method.instructions.remove(method.instructions.get(i));
                    method.instructions.remove(method.instructions.get(i));
                    var actions = Action$parse(method, i, CallBuilderAction::new);
                    for (int j = 0; j < actions.size(); j++) {
                        var action = actions.get(j);
                        var aname = action.method.name;
                        switch (aname) {
                            case "alloc" -> {
                                replaceOpcode(method, action.method, Opcodes.DUP);
                                var _class = (LdcInsnNode) action.parameters.get(0);
                                replaceOpcode(method, _class, new TypeInsnNode(Opcodes.NEW, (String) _class.cst));
                            }
                            case "arg" -> method.instructions.remove(action.method);
                            case "invokeDynamic" -> {
                                var atomicJ = new AtomicInteger(j);
                                buildIndyLdc(method, actions, action, atomicJ, InvokeDynamicInsnNode::new);
                                j = atomicJ.get();;
                            }
                            case "ldc" -> {
                                var atomicJ = new AtomicInteger(j);
                                buildIndyLdc(method, actions, action, atomicJ, (n, d, b, a) -> new LdcInsnNode(new ConstantDynamic(n, d, b, a)));
                                j = atomicJ.get();;
                            }
                            default -> {
                                if (aname.startsWith("end"))
                                    method.instructions.remove(action.method);
                                else if (aname.startsWith("invoke")) {
                                    var name_ = (String) ((LdcInsnNode) action.parameters.get(0)).cst;
                                    var desc_ = (String) ((LdcInsnNode) action.parameters.get(1)).cst;
                                    var clazz_ = (String) ((LdcInsnNode) action.parameters.get(2)).cst;
                                    boolean isInterface_;
                                    int opcode;
                                    if (aname.equals("invokeInterface")) {
                                        isInterface_ = true;
                                        opcode = Opcodes.INVOKEINTERFACE;
                                    } else {
                                        isInterface_ = action.parameters.size() == 4 && action.parameters.get(3).getOpcode() != Opcodes.ICONST_0;
                                        opcode = switch (aname) {
                                            case "invokeVirtual" -> Opcodes.INVOKEVIRTUAL;
                                            case "invokeStatic" -> Opcodes.INVOKESTATIC;
                                            case "invokeSpecial" -> Opcodes.INVOKESPECIAL;
                                            default -> throw new Error("Не удалось распознать тип вызова!");
                                        };
                                    }
                                    method.instructions.set(action.method, new MethodInsnNode(opcode, clazz_, name_, desc_, isInterface_));
                                    action.parameters.forEach(method.instructions::remove);
                                }
                            }
                        }
                    }
                } else if (instr.desc.equals(FieldBuilder.CLASS_NAME)) {
                    method.instructions.remove(instr);
                    method.instructions.remove(method.instructions.get(i));
                    var inst =  method.instructions.get(i);
                    var owner_ = (String) ((LdcInsnNode) inst).cst;
                    method.instructions.remove(inst);
                    inst = method.instructions.get(i);
                    var name_ = (String) ((LdcInsnNode) inst).cst;
                    method.instructions.remove(inst);
                    inst = method.instructions.get(i);
                    var desc_ = (String) ((LdcInsnNode) inst).cst;
                    method.instructions.remove(inst);
                    inst = method.instructions.get(i);
                    var isStatic = false;
                    if (inst instanceof MethodInsnNode) {
                        isStatic = true;
                        method.instructions.remove(inst);
                    } else
                        method.instructions.remove(method.instructions.get(++i));
                    var actions = Action$parse(method, i, FieldBuilderAction::new);
                    for (FieldBuilderAction action : actions) {
                        if (action.method.name.startsWith("get"))
                            replaceOpcode(method, action.method, new FieldInsnNode(isStatic ? Opcodes.GETSTATIC : Opcodes.GETFIELD, owner_, name_, desc_));
                        else
                            replaceOpcode(method, action.method, new FieldInsnNode(isStatic ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD, owner_, name_, desc_));
                    }
                }
            }
        }
    }

    protected static void buildIndyLdc(MethodNode method, List<CallBuilderAction> actions, CallBuilderAction action, AtomicInteger j, BuildIndyLdcNode builder) {
        var args = new Object[action.parameters.size() < 6 ? 0 : (int) parseObject(action.parameters.get(5))];
        for (int k = 0; k < args.length; k++) {
            var act = actions.get(j.incrementAndGet());
            act.parameters.forEach(method.instructions::remove);
            args[k] = parseObject(act.parameters.get(0));
        }
        j.set(j.get() - args.length);
        var inst = builder.build(
                (String) ((LdcInsnNode) action.parameters.get(0)).cst, // name
                (String) ((LdcInsnNode) action.parameters.get(1)).cst, // desc
                new Handle(
                        Opcodes.H_INVOKESTATIC,
                        (String) ((LdcInsnNode) action.parameters.get(2)).cst, // class
                        (String) ((LdcInsnNode) action.parameters.get(3)).cst, // bootstrap name
                        (String) ((LdcInsnNode) action.parameters.get(4)).cst, // bootstrap desc
                        false
                ),
                args
        );
        action.parameters.forEach(method.instructions::remove);
        replaceOpcode(method, actions.get(j.getAndIncrement()).method, inst);
        method.instructions.remove(actions.get(j.get()).method);
    }

    public static Object parseObject(AbstractInsnNode node) {
        if (node.getOpcode() >= Opcodes.ICONST_0 && Opcodes.ICONST_5 >= node.getOpcode())
            return node.getOpcode() - Opcodes.ICONST_0;
        if (node instanceof IntInsnNode i)
            return i.operand;
//        if (node.getOpcode() == 1)
//            return null;
        return ((LdcInsnNode) node).cst;
    }

    public static <T extends AbstractAction> List<T> Action$parse(MethodNode method, int i, AbstractAction.Factory<T> factory) {
        var actions = new ArrayList<T>();
        var k = new AtomicInteger(i);
        do {
            actions.add(Action$parse(method, k, factory));
        } while (!actions.get(actions.size() - 1).isEnd());
        return actions;
    }

    public static <T extends AbstractAction> T Action$parse(MethodNode method, AtomicInteger i, AbstractAction.Factory<T> factory) {
        var list = new ArrayList<AbstractInsnNode>();
        while (true) {
            var instr = method.instructions.get(i.getAndIncrement());
            if (instr instanceof MethodInsnNode node)
                return factory.construct(list, node);
            list.add(instr);
        }
    }

    public static void replaceOpcode(MethodNode node, AbstractInsnNode old, int opcode) {
        node.instructions.set(old, new InsnNode(opcode));
    }

    public static void replaceOpcode(MethodNode node, AbstractInsnNode old, AbstractInsnNode n) {
        node.instructions.set(old, n);
    }

    public static Map<String, Object> wrapAnnData(List<Object> data) {
        Map<String, Object> map = new HashMap<>();
        if (data != null)
            for (int i = 0; i < data.size(); i += 2)
                map.put((String) data.get(i), data.get(i + 1));
        return map;
    }

    public static String processDescriptor(String descriptor) {
        return descriptor.substring(1, descriptor.length() - 1);
    }

    @FunctionalInterface
    public interface BuildIndyLdcNode {
        AbstractInsnNode build(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments);
    }
}
