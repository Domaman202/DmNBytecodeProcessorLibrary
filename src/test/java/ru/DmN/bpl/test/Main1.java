//package ru.DmN.bpl.test;
//
//import ru.DmN.bpl.BytecodeUtils;
//import ru.DmN.bpl.CallBuilder;
//import ru.DmN.bpl.FieldBuilder;
//import ru.DmN.bpl.annotations.BytecodeProcessor;
//
//import java.lang.invoke.CallSite;
//import java.lang.invoke.ConstantCallSite;
//import java.lang.invoke.MethodHandles;
//import java.lang.invoke.MethodType;
//
//public class Main1 {
//    public static int i = 0;
//
//    @BytecodeProcessor
//    public static void test() {
//        System.out.println("\n[TEST] №1");
//        //
//        new CallBuilder().arg("Вася").invokeDynamic("foo", "(Ljava/lang/String;)V", "ru/DmN/bpl/test/Main1", "bootstrap", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;").end();
//        System.out.println(new CallBuilder().arg(12).arg(21).invokeDynamic("add", "(II)I", "ru/DmN/bpl/test/Main1", "bootstrap", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;").endI());
//        //
//        System.out.println("\n[TEST] №2");
//        //
//        new CallBuilder().arg("Вася").invokeStatic("foo", "(Ljava/lang/String;)V", "ru/DmN/bpl/test/Main1", false).end();
//        System.out.println(new CallBuilder().arg(12).arg(21).invokeStatic("add", "(II)I", "ru/DmN/bpl/test/Main1", false).endI());
//        //
//        System.out.println("\n[TEST] №3.1");
//        //
//        new FooImplA().foo();
//        //
//        System.out.println("\n[TEST] №3.2");
//        //
//        new FooImplB().foo();
//        //
//        System.out.println("\n[TEST] №4");
//        //
//        new CallBuilder().invokeStatic("staticFoo", "()V", "ru/DmN/bpl/test/Main1$IFoo", true).end();
//        //
//        System.out.println("\n[TEST] №5");
//        //
//        var obj = BytecodeUtils.alloc("java/lang/Object");
//        new CallBuilder().arg(obj).invokeSpecial("<init>", "()V", "java/lang/Object", false).end();
//        System.out.println(obj);
//        //
//        System.out.println("\n[TEST] №5");
//        //
//        BytecodeUtils.jmp("exit");
//        System.out.println("Бан!");
//        BytecodeUtils.spec$label("exit");
//        //
//        System.out.println("\n[TEST] №6");
//        //
//        try {
//            BytecodeUtils.athrow(new InterruptedException("Успех!"));
//            throw new InterruptedException("Ошибка!");
//        } catch (InterruptedException e) {
//            System.out.println(e.getMessage());
//        }
//        //
//        System.out.println("\n[TEST] №7");
//        //
//        System.out.println(BytecodeUtils.<Object>checkcast(33, "java/lang/Object"));
//        System.out.println(BytecodeUtils.instanceOf(12.21d, "java/lang/Float"));
//        //
//        System.out.println("\n[TEST] №8");
//        //
//        var stream = new FieldBuilder("java/lang/System", "out", "Ljava/io/PrintStream;").getA();
//        new CallBuilder()
//                .arg(stream)
//                .arg("Сало!")
//                .invokeVirtual("println", "(Ljava/lang/String;)V", "java/io/PrintStream")
//                .end();
//        //
//        System.out.println("\n[TEST] №9");
//        //
//        System.out.println(i);
//        new FieldBuilder("ru/DmN/bpl/test/Main1", "i", "I").set(12);
//        System.out.println(i);
//        //
//        System.out.println("\n[TEST] №10");
//        //
//        var container = new IntContainer();
//        System.out.println(container.value);
//        new FieldBuilder("ru/DmN/bpl/test/Main1$IntContainer", "value", "I", container).set(202);
//        System.out.println(new FieldBuilder("ru/DmN/bpl/test/Main1$IntContainer", "value", "I", container).getI());
//    }
//
//    public static void foo(String str) {
//        System.out.println("Привет, " + str + "!");
//    }
//
//    public static int add(int a, int b) {
//        return a + b;
//    }
//
//    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type) throws Exception {
//        return new ConstantCallSite(caller.findStatic(Main1.class, name, type));
//    }
//
//    public interface IFoo {
//        @BytecodeProcessor
//        default void foo() {
//            new CallBuilder().arg(this).invokeInterface("printFoo", "()V", "ru/DmN/bpl/test/Main1$IFoo").end();
//        }
//
//        default void printFoo() {
//            System.out.println("Foo Interface!");
//        }
//
//        static void staticFoo() {
//            System.out.println("Static Foo!");
//        }
//    }
//
//    public static class FooImplA implements IFoo {
//        @Override
//        @BytecodeProcessor
//        public void printFoo() {
//            System.out.println("Foo ImplA!");
//        }
//    }
//
//    public static class FooImplB extends FooImplA implements IFoo {
//        @Override
//        @BytecodeProcessor
//        public void foo() {
//            new CallBuilder().arg(this).invokeSpecial("printFoo", "()V", "ru/DmN/bpl/test/Main1$IFoo", true).end();
//            new CallBuilder().arg(this).invokeSpecial("printFoo", "()V", "ru/DmN/bpl/test/Main1$FooImplA", false).end();
//            super.foo();
//            new CallBuilder().arg(this).invokeSpecial("printFoo", "()V", "ru/DmN/bpl/test/Main1$FooImplB", false).end();
//            new CallBuilder().arg(this).invokeVirtual("printFoo", "()V", "ru/DmN/bpl/test/Main1$FooImplA").end();
//            new CallBuilder().arg(this).invokeInterface("printFoo", "()V", "ru/DmN/bpl/test/Main1$IFoo").end();
//        }
//
//        @Override
//        public void printFoo() {
//            System.out.println("Foo ImplB!");
//        }
//    }
//
//    public static class IntContainer {
//        public int value;
//    }
//}
