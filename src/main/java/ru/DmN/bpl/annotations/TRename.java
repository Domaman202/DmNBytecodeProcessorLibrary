package ru.DmN.bpl.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Переименовывает класс
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TRename {
    @NotNull String value();
}
