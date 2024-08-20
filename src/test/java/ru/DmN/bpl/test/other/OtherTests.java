package ru.DmN.bpl.test.other;

import ru.DmN.bpl.annotations.BytecodeProcessor;
import ru.DmN.uu.Unsafe;

@SuppressWarnings("unused")
@BytecodeProcessor
public class OtherTests {
    public static void test() {
        try {
            System.out.println(Unsafe.unsafe);
        } catch (Throwable t) {
            System.out.println(t.getLocalizedMessage());
        }
    }
}
