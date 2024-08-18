package ru.DmN.bpl.test;

import org.junit.jupiter.api.Assertions;
import org.objectweb.asm.Opcodes;
import ru.DmN.bpl.BytecodeUtils;
import ru.DmN.bpl.CallBuilder;
import ru.DmN.bpl.FieldBuilder;
import ru.DmN.bpl.annotations.*;
import ru.DmN.uu.Unsafe;

import java.lang.invoke.*;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
@BytecodeProcessor
public class JavaTests {
    @FMRename(desc = "Ljava/lang/Class;")
    @Modifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)
    public static Object test$field0;

    static {
        var clazz = JavaTests.class;
        new FieldBuilder("ru/DmN/bpl/test/JavaTests", "test$field0", "Ljava/lang/Class;").set(clazz);
    }

    /**
     * Удаление кода №1
     */
    public static void test0() {
        BytecodeUtils.spec$startDelete();
        Assertions.fail();
        BytecodeUtils.spec$endDelete();
    }

    /**
     * Удаление кода №2
     */
    @DeleteLines(start = {40}, end = {41})
    public static void test17() {
        Assertions.fail();
    }

    /**
     * Вставка `nop` опкода
     */
    public static void test1() {
        BytecodeUtils.spec$opcode(0x0); // NOP
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
        var c = new CallBuilder("add", "(II)I", "ru/DmN/bpl/test/JavaTests$TestClass0")
                .arg(a)
                .arg(b)
                .invokeStatic(false)
                .endI();
        Assertions.assertEquals(a + b, c);
    }

    /**
     * CallBuilder (virtual)
     */
    public static void test4() {
        var a = new TestClass1(4);
        var b = 202;
        var c = new CallBuilder("add", "(I)I", "ru/DmN/bpl/test/JavaTests$TestClass1")
                .arg(a)
                .arg(b)
                .invokeVirtual()
                .endI();
        Assertions.assertEquals(a.value + b, c);
    }

    /**
     * CallBuilder (indy) №1
     */
    public static void test5() {
        var a = 707;
        var b = 70;
        var c = new CallBuilder("add", "(II)I", "ru/DmN/bpl/test/JavaTests")
                .arg(a)
                .arg(b)
                .invokeDynamic("bootstrapA", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;")
                .endI();
        Assertions.assertEquals(a + b, c);
    }

    /**
     * CallBuilder (indy) №2
     */
    public static void test6() {
        var a = new TestClass1(400);
        var b = 4;
        var c = new CallBuilder("add", "(Lru/DmN/bpl/test/JavaTests$TestClass1;I)I", "ru/DmN/bpl/test/JavaTests")
                .arg(a)
                .arg(b)
                .invokeDynamic("bootstrap$", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/Class;ILjava/lang/Class;)Ljava/lang/invoke/CallSite;", 3)
                .arg(TestClass1.class)
                .arg(0)
                .arg(JavaTests.class)
                .endI();
        Assertions.assertEquals(a.value + b, c);
    }

    /**
     * CallBuilder (alloc)
     */
    public static void test18() {
        var obj = new CallBuilder("<init>", "(I)V", "ru/DmN/bpl/test/JavaTests$TestClass1")
                .alloc()
                .arg(202)
                .invokeSpecial(false)
                .endA();
        Assertions.assertEquals(((TestClass1) obj).value, 202);
    }


    /**
     * FieldBuilder №1
     * Получение статического поля
     */
    public static void test7() {
        var value = new FieldBuilder("java/lang/System", "out", "Ljava/io/PrintStream;").getA();
        Assertions.assertEquals(System.out, value);
    }

    /**
     * FieldBuilder №2
     * Получение обычного поля
     */
    public static void test8() {
        var obj = new TestClass1(777);
        var value = new FieldBuilder("ru/DmN/bpl/test/JavaTests$TestClass1", "value", "I", obj).getI();
        Assertions.assertEquals(obj.value, value);
    }

    /**
     * FieldBuilder №3
     * Установка статического поля
     */
    public static void test9() {
        new FieldBuilder("ru/DmN/bpl/test/JavaTests$TestClass2", "GLOBAL_COUNTER", "I").set(202);
        Assertions.assertEquals(TestClass2.GLOBAL_COUNTER, 202);

        new FieldBuilder("ru/DmN/bpl/test/JavaTests$TestClass2", "GLOBAL_COUNTER", "I").set(303);
        Assertions.assertEquals(TestClass2.GLOBAL_COUNTER, 303);
    }

    /**
     * FieldBuilder №4
     * Установка обычного поля
     */
    public static void test10() {
        var obj = new TestClass2(228);
        new FieldBuilder("ru/DmN/bpl/test/JavaTests$TestClass2", "value", "I", obj).set(337);
        Assertions.assertEquals(obj.value, 337);
    }

    /**
     * InstanceOf
     */
    public static void test11() {
        var f = BytecodeUtils.instanceOf(new Object(), "java/lang/String");
        Assertions.assertFalse(f);
        var t = BytecodeUtils.instanceOf("", "java/lang/String");
        Assertions.assertTrue(t);
    }

    /**
     * CheckCast
     */
    public static void test12() {
        Object obj = 12;
        Integer i = BytecodeUtils.checkcast(obj, "java/lang/Integer");
        Assertions.assertEquals(i, 12);
    }

    /**
     * Throw
     */
    public static void test13() {
        try {
            var throwable = new Throwable("Success!");
            BytecodeUtils.athrow(throwable); // throw throwable
            Assertions.fail();
        } catch (Throwable throwable) {
            Assertions.assertEquals(throwable.getMessage(), "Success!");
        }
    }

    /**
     * Ldc №1
     * Class
     */
    public static void test14() throws ClassNotFoundException {
        var cfn = Class.forName("java.lang.Object");
        var ldc = BytecodeUtils.ldc$class("java/lang/Object");
        Assertions.assertEquals(cfn, ldc);
    }

    /**
     * Ldc №2
     * MethodType
     */
    public static void test15() {
        var obj = MethodType.methodType(void.class, int.class);
        var ldc = BytecodeUtils.ldc$mt("(I)V");
        Assertions.assertEquals(obj, ldc);
    }

    /**
     * Ldc №3
     * MethodHandle
     */
    public static void test16() throws NoSuchMethodException, IllegalAccessException {
        var obj = MethodHandles.lookup().findStatic(TestClass0.class, "add", MethodType.methodType(int.class, int.class, int.class));
        var ldc = BytecodeUtils.ldc$mh(Opcodes.H_INVOKESTATIC, "ru/DmN/bpl/test/JavaTests$TestClass0", "add", "(II)I", false);
        Assertions.assertNotEquals(obj, ldc); // obj ptr != ldc ptr
        Assertions.assertEquals(obj.toString(), ldc.toString()); // obj method == ldc method
    }

    /**
     * Множественное наследование
     */
    public static void test20() {
        var obj0 = new TestClass3();
        var obj1 = new TestClass4();
        Assertions.assertEquals(obj0.foo(), 21);
        Unsafe.unsafe.putLong(TestClass4.class, 16L, Unsafe.unsafe.getLong(Runnable.class, 16L));
        Assertions.assertTrue(TestClass4.class.isInterface());
        Assertions.assertEquals(obj1.foo(), 33);
    }

    public static void test777() {
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

    public static class TestClass2 {
        public static int GLOBAL_COUNTER = (int) (Math.random() * 10);
        public int value;

        public TestClass2(int value) {
            this.value = value;
        }

        public int add(int b) {
            return value + b;
        }
    }

    public static class TestClass3 {
        public int foo() {
            return 21;
        }
    }

    @BytecodeProcessor
    @Extends(extend = "ru/DmN/bpl/test/JavaTests$TestClass3")
    public static class TestClass4 {
        @MakeConstructor
        public void init() {
            new CallBuilder("<init>", "()V", "ru/DmN/bpl/test/JavaTests$TestClass3")
                    .arg(this)
                    .invokeSpecial(false)
                    .end();
        }

        public int foo() {
            return new CallBuilder("foo", "()I", "ru/DmN/bpl/test/JavaTests$TestClass3")
                    .arg(this)
                    .invokeSpecial(false)
                    .endI() + 12;
        }
    }
}
