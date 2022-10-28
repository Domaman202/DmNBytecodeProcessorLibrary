package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Меняет модификаторы
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Modifiers {
    int value();
}
