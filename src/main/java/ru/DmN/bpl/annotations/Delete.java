package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Удаляет поле/метод/конструктор
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
public @interface Delete {
}
