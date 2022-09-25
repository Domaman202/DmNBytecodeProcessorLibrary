package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Требует чтобы параметр был константой, а не переменной/выражением.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface Const {
}
