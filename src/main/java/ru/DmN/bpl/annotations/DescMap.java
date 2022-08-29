package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Заменяет старые дескрипторы (odesc) на новые (ndesc) в пределах помечанного класса
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface DescMap {
    String[] odesc();
    String[] ndesc();
}
