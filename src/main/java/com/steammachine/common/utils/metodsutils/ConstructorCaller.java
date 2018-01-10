package com.steammachine.common.utils.metodsutils;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */
public interface ConstructorCaller<T> {

    /**
     * Все проверяемые исключения (checked exceptions),
     * возникающие в процессе выполнения оборачиваются в {@link RuntimeException}
     * @param param параметры вызова
     * @param <T>   тип возвращаемого значения
     * @return приведенное значение
     */
    <T> T newInstance(Object... param);

}
