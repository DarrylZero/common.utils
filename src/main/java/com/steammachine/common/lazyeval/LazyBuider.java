package com.steammachine.common.lazyeval;

import com.steammachine.common.utils.commonutils.CommonUtils;

import java.util.Objects;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov 
 **/
public interface LazyBuider {

    @FunctionalInterface
    interface Evaluable<T> {
        T eval() throws Throwable;
    }

    /**
     *
     *
     * @param evaluable - evaluable (всегда не null)
     * @return - Возвращает объект с методом toString в вычислении значения которого, используется Lambda
     *
     * вычисление производится как только вызывается toString в потоке который вызывает toString
     */
    public static Object lazy(Evaluable<String> evaluable) {
        Objects.requireNonNull(evaluable);
        return new Object() {
            @Override
            public String toString() {
                return CommonUtils.suppressAll(evaluable::eval) ;
            }
        };
    }


}
