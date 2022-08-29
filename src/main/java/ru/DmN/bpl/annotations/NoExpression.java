package ru.DmN.bpl.annotations;

import java.lang.annotation.*;

/**
 * Требует чтобы в качестве параметра указывалась константа/переменная, запрещает использовать выражение
 * <pre>
 *     {@code
 *     new CallBuilder()
 *      .arg("Вася");
 *     }
 * </pre>
 * ИЛИ
 * <pre>
 *     {@code
 *     var name = "Вася";
 *     new CallBuilder()
 *      .arg(name);
 *     }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface NoExpression {
}
