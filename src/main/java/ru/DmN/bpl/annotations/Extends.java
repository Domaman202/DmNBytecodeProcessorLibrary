package ru.DmN.bpl.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Замена extends/implements в виде аннотации
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Extends {
    @NotNull String extend() default "";

    @NotNull String[] impl() default "";

    boolean deleteImpls() default false;
}
