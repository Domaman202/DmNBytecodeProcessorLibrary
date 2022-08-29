//package ru.DmN.bpl.test;
//
//import ru.DmN.bpl.annotations.Annotations;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.Arrays;
//
//@Annotations("Ljava/lang/Deprecated;")
//public class Main9 {
//    @Annotations("Lru/DmN/bpl/test/Main9$TAnnotation;")
//    public static void test() throws NoSuchMethodException {
//        System.out.println(Arrays.toString(Main9.class.getAnnotations()));
//        System.out.println(Arrays.toString(Main9.class.getMethod("test").getAnnotations()));
//    }
//
//    @Retention(RetentionPolicy.RUNTIME)
//    @Target(ElementType.METHOD)
//    public @interface TAnnotation {
//    }
//}
