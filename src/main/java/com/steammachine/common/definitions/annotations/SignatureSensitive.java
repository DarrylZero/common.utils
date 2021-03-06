package com.steammachine.common.definitions.annotations;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация - маркер используется для обозначения методов или полей класса которые, чувствительны к изменению
 * своей сигнатуры (методы) или наименованию и типу (поля).
 * Поля или методы, помеченные этой аннотацией проверяются из тестов или другого функционала по имени и параметрам.
 *
 * Вызов метода или получение значения поля может получаться из рефлексии.
 *
 * Аннотация видна только в исходном коде, не доступна через reflection.
 * <p>
 *
 * @author Vladimir Bogodukhov
 */

@Target({
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.SOURCE)
@Api(State.MAINTAINED)
public @interface SignatureSensitive {

    /**
     *
     * @return - Дополнительные описания.
     */
    String[] value() default {};


}
