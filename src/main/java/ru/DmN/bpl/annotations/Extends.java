package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Замена extends/implements в виде аннотации
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Extends {
    String extend() default "";

    String[] impl() default "";

    boolean deleteImpls() default false;
}
