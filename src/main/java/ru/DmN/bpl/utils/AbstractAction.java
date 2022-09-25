package ru.DmN.bpl.utils;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;

public abstract class AbstractAction {
    public final List<AbstractInsnNode> parameters;
    public final MethodInsnNode method;

    public AbstractAction(List<AbstractInsnNode> parameters, MethodInsnNode method) {
        this.parameters = parameters;
        this.method = method;
    }

    public abstract boolean isEnd();

    @FunctionalInterface
    public interface Factory <T extends AbstractAction> {
        T construct(List<AbstractInsnNode> parameters, MethodInsnNode method);
    }
}
