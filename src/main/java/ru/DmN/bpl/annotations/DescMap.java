package ru.DmN.bpl.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Заменяет старые дескрипторы (odesc) на новые (ndesc) в пределах помечанного класса
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface DescMap {
    /**
     * @return Старые дескрипторы
     */
    @NotNull String[] odesc();

    /**
     * @return Новые дескрипторы
     */
    @NotNull String[] ndesc();
}
