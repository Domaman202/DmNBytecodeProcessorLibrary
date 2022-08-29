//package ru.DmN.bpl.test;
//
//import ru.DmN.bpl.BytecodeUtils;
//import ru.DmN.bpl.CallBuilder;
//import ru.DmN.bpl.FieldBuilder;
//import ru.DmN.bpl.annotations.BytecodeProcessor;
//
//import java.lang.invoke.MethodHandles;
//import java.lang.invoke.VarHandle;
//
//public class Main7 {
//    public static int i = 12;
//
//    @BytecodeProcessor
//    public static void test() {
//        new CallBuilder().ldc("i", "Ljava/lang/invoke/VarHandle;", "ru/DmN/bpl/test/Main7", "bootstrap", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/invoke/VarHandle;", 1).arg("ru.DmN.bpl.test.Main7").end();
//        BytecodeUtils.dup();
//        // get and print
//        new CallBuilder().invokeVirtual("get", "()I", "java/lang/invoke/VarHandle").end();
//        new CallBuilder().invokeStatic("valueOf", "(I)Ljava/lang/String;", "java/lang/String", false).end();
//        new FieldBuilder("java/lang/System", "out", "Ljava/io/PrintStream;").get();
//        BytecodeUtils.dup_x2();
//        BytecodeUtils.swap();
//        new CallBuilder().invokeVirtual("println", "(Ljava/lang/String;)V", "java/io/PrintStream").end();
//        // set
//        BytecodeUtils.dup();
//        new CallBuilder().arg(21).invokeVirtual("set", "(I)V", "java/lang/invoke/VarHandle").end();
//        // get and print
//        new CallBuilder().invokeVirtual("get", "()I", "java/lang/invoke/VarHandle").end();
//        new CallBuilder().invokeStatic("valueOf", "(I)Ljava/lang/String;", "java/lang/String", false).end();
//        new CallBuilder().invokeVirtual("println", "(Ljava/lang/String;)V", "java/io/PrintStream").end();
//    }
//
//    public static VarHandle bootstrap(MethodHandles.Lookup caller, String name, Class<?> type, String clazz) throws Exception {
//        return caller.unreflectVarHandle(Class.forName(clazz).getDeclaredField(name));
//    }
//}
