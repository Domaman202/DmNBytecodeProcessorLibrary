package ru.DmN.bpl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Делает из метода конструктор
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface MakeConstructor {
}
