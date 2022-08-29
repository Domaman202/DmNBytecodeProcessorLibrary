//package ru.DmN.bpl.test;
//
//import ru.DmN.bpl.annotations.Extends;
//
//public class Main10 {
//    public static void test() {
//        var foo = (IFoo) new IFooImpl();
//        foo.foo();
//    }
//
//    public interface IFoo {
//        void foo();
//    }
//
//    @Extends(impl = "ru/DmN/bpl/test/Main10$IFoo")
//    public static class IFooImpl {
//        public void foo() {
//            System.out.println("Foo!");
//        }
//    }
//}
