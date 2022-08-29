//package ru.DmN.bpl.test;
//
//import ru.DmN.bpl.BytecodeUtils;
//import ru.DmN.bpl.CallBuilder;
//import ru.DmN.bpl.annotations.*;
//
//import java.io.IOException;
//import java.lang.invoke.CallSite;
//import java.lang.invoke.MethodHandle;
//import java.lang.invoke.MethodHandles;
//import java.lang.invoke.MethodType;
//
//public class Main2 {
//    public static final boolean printStackTrace = false;
//
//    public static void test() throws ClassNotFoundException, IOException {
//        callFoo();
//        callFoo();
//        new Thread(() -> {
//            callFoo();
//            callFoo();
//        }).start();
//    }
//
//    @BytecodeProcessor
//    public static void callFoo() {
//        new CallBuilder().invokeDynamic("foo", "()V", "ru/DmN/bpl/test/Main2", "bootstrap", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;").end();
//    }
//
//    public static void foo() {
//        System.out.printf("Foo! %s\n\n", Thread.currentThread().getName());
//    }
//
//    @BytecodeProcessor
//    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type) throws Exception {
//        return (java.lang.invoke.CallSite) new CallBuilder().alloc("java/lang/invoke/MyCallSite$").arg(caller.findStatic(Main2.class, name, type)).invokeSpecial("<init>", "(Ljava/lang/invoke/MethodHandle;)V", "java/lang/invoke/MyCallSite$", false).endA();
//    }
//
//    @MakeClassInit
//    @BytecodeProcessor
//    public static void __static__() {
//        new CallBuilder().arg(BytecodeUtils.checkcast(TestBootstrap.class.getClassLoader(), "ru/DmN/bpl/test/Tests$SmartClassLoader")).arg("ru.DmN.bpl.test.Main2$MyCallSite$").arg(true).arg(CallSite.class).invokeVirtual("smartLoad", "(Ljava/lang/String;ZLjava/lang/Class;)Ljava/lang/Class;", "ru/DmN/bpl/test/Tests$SmartClassLoader").endA();
//    }
//
//    @TRename("java/lang/invoke/MyCallSite$")
//    @DescMap(odesc = "ru/DmN/bpl/test/Main2$MyCallSite$", ndesc = "java/lang/invoke/CallSite")
//    @Extends(extend = "java/lang/invoke/CallSite")
//    public static class MyCallSite$ {
//        @Delete
//        public MyCallSite$(MethodHandle target) {
//        }
//
//        @MakeConstructor
//        @BytecodeProcessor
//        public void __init__(MethodHandle target) {
//            new CallBuilder().arg(this).arg(target).invokeSpecial("<init>", "(Ljava/lang/invoke/MethodHandle;)V", "java/lang/invoke/CallSite", false).end();
//            System.out.printf("CallSite created: %s\nCallSite loader: %s\nThread: %s\n\n", target.type(), this.getClass().getClassLoader(), Thread.currentThread().getName());
//        }
//
//        @Delete
//        public MethodHandle target;
//
//        public MethodHandle getTarget() {
//            System.out.printf("Method \"getTarget\" target called! %s\n", Thread.currentThread().getName());
//            if (printStackTrace)
//                Thread.dumpStack();
//            return target;
//        }
//
//        public void setTarget(MethodHandle newTarget) {
//            System.out.println("Method \"setTarget\" target called!");
//            if (printStackTrace)
//                Thread.dumpStack();
//            target = newTarget;
//        }
//
//        public MethodHandle dynamicInvoker() {
//            System.out.println("Method \"dynamicInvoker\" target called!");
//            if (printStackTrace)
//                Thread.dumpStack();
//            return target;
//        }
//    }
//}
