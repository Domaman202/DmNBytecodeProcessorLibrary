package ru.DmN.bpl.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Задаёт аннотации типу/методу/полю
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
public @interface Annotations {
    /**
     * @return Дескрипторы аннотаций
     */
    @NotNull String[] value();
}
