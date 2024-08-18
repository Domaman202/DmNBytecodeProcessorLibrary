package ru.DmN.bpl;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import ru.DmN.bpl.exceptions.ProcessException;
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
                    case "Modifiers" -> this.access = (int) data.get("value");
                }
            }
        }

        var ffr = new ArrayList<FieldNode>();
        fields:
        for (var field : this.fields) {
            for (var annotation : CollectionsHelper.combine(field.visibleAnnotations, field.invisibleAnnotations)) {
                var desc = processDescriptor(annotation.desc);
                if (desc.startsWith("ru/DmN/bpl/annotations/")) {
                    var data = wrapAnnData(annotation.values);
                    switch (desc.substring(desc.lastIndexOf('/') + 1)) {
                        case "Delete" -> {
                            ffr.add(field);
                            continue fields;
                        }
                        case "Annotations" -> {
                            if (field.visibleAnnotations == null)
                                field.visibleAnnotations = new ArrayList<>();
                            ((List<String>) data.get("value")).forEach(d -> field.visibleAnnotations.add(new AnnotationNode(d)));
                        }
                        case "FMRename" -> {
                            if (data.containsKey("name"))
                                field.name = (String) data.get("name");
                            if (data.containsKey("desc"))
                                field.desc = (String) data.get("desc");
                            if (data.containsKey("sign"))
                                field.signature = (String) data.get("sign");
                        }
                        case "Modifiers" -> field.access = (int) data.get("value");
                    }
                }
            }
        }
        ffr.forEach(f -> fields.remove(f));

        var mfr = new ArrayList<MethodNode>();
        methods:
        for (int i = 0; i < this.methods.size(); i++) {
            var method = this.methods.get(i);
            var method$bcprocessor = bcprocessor;
            var deletes = new ArrayList<DeleteAnnotation>();

            for (var annotation : CollectionsHelper.combine(method.visibleAnnotations, method.invisibleAnnotations)) {
                var desc = processDescriptor(annotation.desc);
                if (desc.startsWith("ru/DmN/bpl/annotations/")) {
                    var data = wrapAnnData(annotation.values);
                    switch (desc.substring(desc.lastIndexOf('/') + 1)) {
                        case "ExtendSL" -> {
                            if (data.containsKey("stack"))
                                method.maxStack += (int) data.get("stack");
                            if (data.containsKey("local"))
                                method.maxLocals += (int) data.get("local");
                        }
                        case "BytecodeProcessor" -> method$bcprocessor = true;
                        case "MakeConstructor" -> {
                            for (int j = 0; j < this.methods.size(); j++) {
                                var it = this.methods.get(j);
                                if (it.name.equals("<init>") && it.desc.equals(method.desc)) {
                                    this.methods.remove(it);
                                    i--;
                                    break;
                                }
                            }
                            method.name = "<init>";
                        }
                        case "MakeClassInit" -> {
                            for (var m : this.methods) {
                                if (m.name.equals("<clinit>")) {
                                    this.methods.remove(m);
                                    break;
                                }
                            }
                            method.name = "<clinit>";
                        }
                        case "Delete" -> {
                            mfr.add(method);
                            continue methods;
                        }
                        case "DeleteLines" -> {
                            var starts = (List<Integer>) data.get("start");
                            var ends = (List<Integer>) data.get("end");
                            for (int j = 0; j < data.size() / 2; j++) {
                                deletes.add(new DeleteAnnotation(starts.get(j), ends.get(j)));
                            }
                        }
                        case "Annotations" -> {
                            if (method.visibleAnnotations == null)
                                method.visibleAnnotations = new ArrayList<>();
                            ((List<String>) data.get("value")).forEach(d -> method.visibleAnnotations.add(new AnnotationNode(d)));
                        }
                        case "FMRename" -> {
                            if (data.containsKey("name"))
                                method.name = (String) data.get("name");
                            if (data.containsKey("desc")) {
                                method.desc = (String) data.get("desc");
                                if (data.containsKey("sign")) {
                                    method.signature = (String) data.get("sign");
                                } else {
                                    method.signature = method.desc;
                                }
                            } else if (data.containsKey("sign")) {
                                method.signature = (String) data.get("sign");
                            }
                        }
                        case "Modifiers" -> method.access = (int) data.get("value");
                    }
                }
            }

            if (method$bcprocessor)
                this.processBytecode(method, deletes);

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

    public void processBytecode(MethodNode method, List<DeleteAnnotation> deletes) {
        var labels = new LabelMap(method);
        var instructions = method.instructions;

        for (int i = 0; i < instructions.size(); i++) {
            var instr0 = instructions.get(i);
            if (instr0 instanceof LineNumberNode instr) {
                for (var delete : deletes) {
                    if (delete.start >= instr.line) {
                        AbstractInsnNode instr1 = instr;
                        while (i + 2 < instructions.size()) {
                            instructions.remove(instr1);
                            instr1 = instructions.get(i);
                            if (instr1 instanceof LineNumberNode instr2 && instr2.line >= delete.end) {
                                break;
                            }
                        }
                        instructions.remove(instr1);
                    }
                }
            } else if (instr0 instanceof MethodInsnNode instr) {
                if (instr.owner.equals(BytecodeUtils.CLASS_NAME)) {
                    switch (instr.name) {
                        case "spec$startDelete" -> {
                            AbstractInsnNode instr1 = instr;
                            while (i + 2 < instructions.size()) {
                                instructions.remove(instr1);
                                instr1 = instructions.get(i);
                                if (instr1 instanceof MethodInsnNode instr2 && instr2.owner.equals(BytecodeUtils.CLASS_NAME) && instr2.name.equals("spec$endDelete")) {
                                    break;
                                }
                            }
                            instructions.remove(instr1);
                        }
                        case "spec$fakeUsage" -> {
                            instructions.remove(instr);
                            instructions.remove(instructions.get(--i));
                        }
                        case "spec$label" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, labels.get(((LdcInsnNode) inst).cst));
                        }
                        case "spec$opcode" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, new InsnNode((Integer) parseObject(inst)));
                        }
                        case "ldc$mh" -> {
                            // IsInterface
                            while (instructions.get(i - 1).getOpcode() == -1) i--;
                            var inst0 = instructions.get(--i);
                            instructions.remove(inst0);
                            // Desc
                            while (instructions.get(i - 1).getOpcode() == -1) i--;
                            var inst1 = instructions.get(--i);
                            instructions.remove(inst1);
                            // Name
                            while (instructions.get(i - 1).getOpcode() == -1) i--;
                            var inst2 = instructions.get(--i);
                            instructions.remove(inst2);
                            // Owner
                            while (instructions.get(i - 1).getOpcode() == -1) i--;
                            var inst3 = instructions.get(--i);
                            instructions.remove(inst3);
                            // Tag
                            while (instructions.get(i - 1).getOpcode() == -1) i--;
                            var inst4 = instructions.get(--i);
                            instructions.remove(inst4);
                            //
                            replaceOpcode(
                                    method,
                                    instr,
                                    new LdcInsnNode(
                                            new Handle(
                                                    (int) parseObject(inst4),
                                                    (String) parseObject(inst3),
                                                    (String) parseObject(inst2),
                                                    (String) parseObject(inst1),
                                                    (int) parseObject(inst0) == 1
                                            )
                                    )
                            );
                        }
                        case "ldc$mt" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, new LdcInsnNode(Type.getType((String) ((LdcInsnNode) inst).cst)));
                        }
                        case "ldc$class" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, new LdcInsnNode(Type.getType("L" + ((LdcInsnNode) inst).cst + ";")));
                        }
                        case "load" -> instructions.remove(instr);
                        case "jmp" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, new JumpInsnNode(Opcodes.GOTO, labels.get(((LdcInsnNode) inst).cst)));
                        }
                        case "alloc" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, new TypeInsnNode(Opcodes.NEW, (String) ((LdcInsnNode) inst).cst));
                        }
                        case "athrow" -> replaceOpcode(method, instr, Opcodes.ATHROW);
                        case "checkcast" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, new TypeInsnNode(Opcodes.CHECKCAST, (String) ((LdcInsnNode) inst).cst));
                        }
                        case "instanceOf" -> {
                            var inst = instructions.get(--i);
                            instructions.remove(inst);
                            replaceOpcode(method, instr, new TypeInsnNode(Opcodes.INSTANCEOF, (String) ((LdcInsnNode) inst).cst));
                        }
                        case "monitorenter" -> replaceOpcode(method, instr, Opcodes.MONITORENTER);
                        case "monitorexit" -> replaceOpcode(method, instr, Opcodes.MONITOREXIT);
                    }
                }
            } else if (instr0 instanceof TypeInsnNode instr) {
                if (instr.desc.equals(CallBuilder.CLASS_NAME)) {
                    instructions.remove(instr);
                    instructions.remove(instructions.get(i));
                    var instr1 = instructions.get(i);
                    var name_ = (String) ((LdcInsnNode) instr1).cst;
                    instructions.remove(instr1);
                    instr1 = instructions.get(i);
                    var desc_ = (String) ((LdcInsnNode) instr1).cst;
                    instructions.remove(instr1);
                    instr1 = instructions.get(i);
                    var clazz_ = (String) ((LdcInsnNode) instr1).cst;
                    instructions.remove(instr1);
                    instructions.remove(instructions.get(i));
                    var actions = Action$parse(method, i, CallBuilderAction::new);
                    for (int j = 0; j < actions.size(); j++) {
                        var action = actions.get(j);
                        var aname = action.method.name;
                        switch (aname) {
                            case "alloc" -> {
                                var node = new TypeInsnNode(Opcodes.NEW, clazz_);
                                method.instructions.set(action.method, node);
                                method.instructions.insert(node, new InsnNode(Opcodes.DUP));
                            }
                            case "arg" -> instructions.remove(action.method);
                            case "invokeDynamic" -> {
                                var atomicJ = new AtomicInteger(j);
                                buildIndyLdc(method, actions, action, atomicJ, InvokeDynamicInsnNode::new, name_, desc_, clazz_);
                                j = atomicJ.get();;
                            }
                            case "ldc" -> {
                                var atomicJ = new AtomicInteger(j);
                                buildIndyLdc(method, actions, action, atomicJ, (n, d, b, a) -> new LdcInsnNode(new ConstantDynamic(n, d, b, a)), name_, desc_, clazz_);
                                j = atomicJ.get();;
                            }
                            default -> {
                                if (aname.startsWith("end"))
                                    instructions.remove(action.method);
                                else if (aname.startsWith("invoke")) {
                                    boolean isInterface_;
                                    int opcode;
                                    if (aname.equals("invokeInterface")) {
                                        isInterface_ = true;
                                        opcode = Opcodes.INVOKEINTERFACE;
                                    } else {
                                        isInterface_ = action.parameters.size() == 1 && action.parameters.get(0).getOpcode() != Opcodes.ICONST_0;
                                        opcode = switch (aname) {
                                            case "invokeVirtual" -> Opcodes.INVOKEVIRTUAL;
                                            case "invokeStatic" -> Opcodes.INVOKESTATIC;
                                            case "invokeSpecial" -> Opcodes.INVOKESPECIAL;
                                            default -> throw new ProcessException("Не удалось распознать тип вызова!");
                                        };
                                    }
                                    instructions.set(action.method, new MethodInsnNode(opcode, clazz_, name_, desc_, isInterface_));
                                    action.parameters.forEach(instructions::remove);
                                }
                            }
                        }
                    }
                } else if (instr.desc.equals(FieldBuilder.CLASS_NAME)) {
                    instructions.remove(instr);
                    instructions.remove(instructions.get(i));
                    var inst =  instructions.get(i);
                    var owner_ = (String) ((LdcInsnNode) inst).cst;
                    instructions.remove(inst);
                    inst = instructions.get(i);
                    var name_ = (String) ((LdcInsnNode) inst).cst;
                    instructions.remove(inst);
                    inst = instructions.get(i);
                    var desc_ = (String) ((LdcInsnNode) inst).cst;
                    instructions.remove(inst);
                    inst = instructions.get(i);
                    var isStatic = false;
                    if (inst instanceof MethodInsnNode) {
                        isStatic = true;
                        instructions.remove(inst);
                    } else
                        instructions.remove(instructions.get(++i));
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

    protected static void buildIndyLdc(MethodNode method, List<CallBuilderAction> actions, CallBuilderAction action, AtomicInteger j, BuildIndyLdcNode builder, String name_, String desc_, String clazz_) {
        var args = new Object[action.parameters.size() < 3 ? 0 : (int) parseObject(action.parameters.get(2))];
        for (int k = 0; k < args.length; k++) {
            var act = actions.get(j.incrementAndGet());
            act.parameters.forEach(method.instructions::remove);
            args[k] = parseObject(act.parameters.get(0));
        }
        j.set(j.get() - args.length);
        var inst = builder.build(
                name_,
                desc_,
                new Handle(
                        Opcodes.H_INVOKESTATIC,
                        clazz_,
                        (String) ((LdcInsnNode) action.parameters.get(0)).cst, // bootstrap name
                        (String) ((LdcInsnNode) action.parameters.get(1)).cst, // bootstrap desc
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
            if (instr.getOpcode() == -1)
                continue;
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
