package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Указывает загрузчику классов о требовании обработать класс/метод байткод-процессором
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
public @interface BytecodeProcessor {
}
