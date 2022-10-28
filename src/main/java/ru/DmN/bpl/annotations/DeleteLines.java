package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Удаляет код в методе с `start` строчки по `end` строчку
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface DeleteLines {
    int[] start();

    int[] end();
}
