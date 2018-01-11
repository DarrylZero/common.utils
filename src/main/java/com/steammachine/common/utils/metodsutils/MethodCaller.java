package com.steammachine.common.utils.metodsutils;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

/**
 * Вспомогательный интерфейс вызова методов
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 **/
@Api(State.MAINTAINED)
public interface MethodCaller {

    /**
     *
     * Вызывает метод на объекте с передаваемыми параметрами
     *
     * Все проверяемые исключения (checked exceptions),
     * возникающие в процессе выполнения оборачиваются в {@link RuntimeException}
     *
     * @param o  объект на котором производится вызов
     * @param param параметры вызова
     * @param <T>   тип возвращаемого значения
     * @return приведенное значение
     */
    <T> T invoke(Object o, Object... param);


}

