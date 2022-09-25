package ru.DmN.bpl.annotations;

import java.lang.annotation.*;


/**
 * Расширяет/уменьшает макс. размер стека/кол-во констант в методе
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface ExtendSL {
    int stack() default 0;

    int local() default 0;
}