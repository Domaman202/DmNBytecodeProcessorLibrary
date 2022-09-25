package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Удаляет сгенерированный компилятором {@literal <clinit>} и делает из метода {@literal <clinit>}
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface MakeClassInit {
}
