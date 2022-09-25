package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Переименовывает поля/методы (не проводит mapping)
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface FMRename {
    String name() default "";

    String desc() default "";

    String sign() default "";
}
