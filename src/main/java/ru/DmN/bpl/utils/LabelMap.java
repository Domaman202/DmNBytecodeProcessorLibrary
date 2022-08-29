package ru.DmN.bpl.utils;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class LabelMap extends HashMap<String, LabelNode> {
    public final MethodNode node;

    public LabelMap(MethodNode node) {
        this.node = node;
    }

    public Label getLabel(Object key) {
        return get(key).getLabel();
    }

    @Override
    public LabelNode get(Object key) {
        if (this.containsKey(key))
            return super.get(key);
        LabelNode label;
        try {
            var m = MethodNode.class.getDeclaredMethod("getLabelNode", Label.class);
            m.setAccessible(true);
            label = (LabelNode) m.invoke(this.node, new Label());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.put((String) key, label);
        return label;
    }
}
