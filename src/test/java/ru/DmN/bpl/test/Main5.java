//package ru.DmN.bpl.test;
//
//import ru.DmN.bpl.CallBuilder;
//import ru.DmN.bpl.annotations.BytecodeProcessor;
//
//import java.lang.invoke.CallSite;
//import java.lang.invoke.ConstantCallSite;
//import java.lang.invoke.MethodHandles;
//import java.lang.invoke.MethodType;
//
//public class Main5 {
//    @BytecodeProcessor
//    public static void test() {
//        new CallBuilder().arg(new Main5()).arg("Вася").invokeDynamic("foo", "(Lru/DmN/bpl/test/Main5;Ljava/lang/String;)V", "ru/DmN/bpl/test/Main5", "bootstrap", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;", 3).arg("ru.DmN.bpl.test.Main5").arg("(Ljava/lang/String;)V").arg(12).end();
//    }
//
//    public void foo(String str) {
//        System.out.println("Привет, " + str + "!");
//    }
//
//    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type, String clazz, String rtype, int i) throws Exception {
//        System.out.println(i);
//        return new ConstantCallSite(caller.findVirtual(Class.forName(clazz), name, MethodType.fromMethodDescriptorString(rtype, caller.getClass().getClassLoader())));
//    }
//}
