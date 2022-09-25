package ru.DmN.bpl.utils;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

public class FieldBuilderAction extends AbstractAction {
    public FieldBuilderAction(List<AbstractInsnNode> parameters, MethodInsnNode method) {
        super(parameters, method);
    }

    @Override
    public boolean isEnd() {
        return method.name.startsWith("get") || method.name.startsWith("set");
    }
}
