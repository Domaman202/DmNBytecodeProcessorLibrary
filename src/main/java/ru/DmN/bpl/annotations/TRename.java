package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Переименовывает класс
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TRename {
    String value();
}
