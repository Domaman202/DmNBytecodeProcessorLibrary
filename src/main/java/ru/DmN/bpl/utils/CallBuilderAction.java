package ru.DmN.bpl.utils;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

public class CallBuilderAction extends AbstractAction {
    public CallBuilderAction(List<AbstractInsnNode> parameters, MethodInsnNode method) {
        super(parameters, method);
    }

    public boolean isEnd() {
        return method.name.startsWith("end");
    }
}
