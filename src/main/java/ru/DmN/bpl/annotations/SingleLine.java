package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Требует чтобы операции с классом производились в одну строку, запрещает запись класса в переменную
 * <pre>
 *     {@code
 *     new CallBuilder()
 *      .arg("Вася")
 *      .indy("test", "(Ljava/lang/String;)V", "ru/test/Main1", "bootstrapA", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false)
 *      .indyV();
 *     }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SingleLine {
}
