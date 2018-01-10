package com.steammachine.common.definitions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация - маркер используется для обозначения методов или полей класса которые, описаны и присутствуют в коде
 * только для обратной совместимости. Это как правило stub методы или поля - которые не делают ничего,
 * либо Deprecated сущности. Которые остаются в коде, но вот вот будут удалены.
 *
 * Аннотация видна только в исходном коде, не доступна через reflection.
 * <p>
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */

@Target({
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.SOURCE)
public @interface BackwardCompatibility {

    /**
     *
     * @return - Дополнительные описания.
     */
    String[] value() default {};


}
