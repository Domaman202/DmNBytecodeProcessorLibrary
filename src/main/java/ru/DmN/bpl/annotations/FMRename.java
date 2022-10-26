package ru.DmN.bpl.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Переименовывает поля/методы (не проводит mapping)
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface FMRename {
    @NotNull String name() default "";

    @NotNull String desc() default "";

    @NotNull String sign() default "";
}
