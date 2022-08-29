package ru.DmN.bpl.test;

import org.junit.jupiter.api.Assertions;
import ru.DmN.bpl.BytecodeUtils;
import ru.DmN.bpl.CallBuilder;
import ru.DmN.bpl.annotations.BytecodeProcessor;

import java.lang.invoke.*;

@BytecodeProcessor
public class Tests {
    /**
     * Удаление кода
     */
    public static void test0() {
        BytecodeUtils.spec$startDelete();
        Assertions.fail();
        BytecodeUtils.spec$endDelete();
    }

    /**
     * Вставка `nop` опкода
     */
    public static void test1() {
        BytecodeUtils.nop();
    }

    /**
     * Вставка `goto` и `labels`
     */
    public static void test2() {
        int i = 0;
        BytecodeUtils.spec$label("loop"); // loop:
        i++;
        if (i < 100)
            BytecodeUtils.jmp("loop"); // goto loop
        Assertions.assertEquals(i, 100);
    }

    /**
     * CallBuilder (static)
     */
    public static void test3() {
        var a = 12;
        var b = 21;
        var c = new CallBuilder()
                .arg(a).arg(b)
                .invokeStatic("add", "(II)I", "ru/DmN/bpl/test/Tests$TestClass0", false)
                .endI();
        Assertions.assertEquals(a + b, c);
    }

    /**
     * CallBuilder (virtual)
     */
    public static void test4() {
        var a = new TestClass1(4);
        var b = 202;
        var c = new CallBuilder()
                .arg(a).arg(b)
                .invokeVirtual("add", "(I)I", "ru/DmN/bpl/test/Tests$TestClass1")
                .endI();
        Assertions.assertEquals(a.value + b, c);
    }

    /**
     * CallBuilder (indy) №1
     */
    public static void test5() {
        var a = 707;
        var b = 70;
        var c = new CallBuilder().arg(a).arg(b).invokeDynamic("add", "(II)I", "ru/DmN/bpl/test/Tests", "bootstrapA", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;").endI();
        Assertions.assertEquals(a + b, c);
    }

    /**
     * CallBuilder (indy) №2
     */
    public static void test6() {
        var a = new TestClass1(400);
        var b = 4;
        var c = new CallBuilder().arg(a).arg(b).invokeDynamic("add", "(Lru/DmN/bpl/test/Tests$TestClass1;I)I", "ru/DmN/bpl/test/Tests", "bootstrap$", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;ILjava/lang/Class;)Ljava/lang/invoke/CallSite;", 3).arg(TestClass1.class).arg(0).arg(Tests.class).endI();
        Assertions.assertEquals(a.value + b, c);
    }

    public static void test777() throws ClassNotFoundException, NoSuchFieldException {
        System.out.println("All success!");
    }

    public static CallSite bootstrapA(MethodHandles.Lookup lookup, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return new ConstantCallSite(lookup.findStatic(TestClass0.class, name, type));
    }

    public static CallSite bootstrap$(MethodHandles.Lookup lookup, String name, MethodType type, Class<?> clazz, int calltype, Class<?> caller) throws Exception {
        MethodHandle mh;
        if (name.equals("<init>"))
            mh = lookup.findConstructor(clazz, type);
        else if (calltype == 0)
            mh = lookup.findVirtual(clazz, name, type.dropParameterTypes(0, 1));
        else if (calltype == 1)
            mh = lookup.findStatic(clazz, name, type);
        else if (calltype == 2)
            mh = lookup.findSpecial(clazz, name, type.dropParameterTypes(0, 1), caller);
        else
            throw new IllegalStateException("Unexpected value: " + calltype);
        return new ConstantCallSite(mh);
    }

    public static class TestClass0 {
        public static int add(int a, int b) {
            return a + b;
        }
    }

    public record TestClass1(int value) {
        public int add(int b) {
            return value + b;
        }
    }
}
