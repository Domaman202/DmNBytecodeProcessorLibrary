//package ru.DmN.bpl.test;
//
//import ru.DmN.bpl.CallBuilder;
//import ru.DmN.bpl.annotations.BytecodeProcessor;
//
//public class Main3 {
//    @BytecodeProcessor
//    public static void test() {
//        System.out.println(new CallBuilder().alloc("ru/DmN/bpl/test/Main3$A").arg(12).arg(21).invokeSpecial("<init>", "(II)V", "ru/DmN/bpl/test/Main3$A", false).endA());
//    }
//
//    @BytecodeProcessor
//    public static class A {
//        public int i;
//
//        public A(int a, int b) {
//            this.i = a + b;
//        }
//
//        public void foo() {
//            System.out.println("Foo!");
//        }
//
//        @Override
//        public String toString() {
//            return "A (" + i + ")";
//        }
//    }
//}
