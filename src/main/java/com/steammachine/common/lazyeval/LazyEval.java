package com.steammachine.common.lazyeval;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Интерфейс ленивого вычисления значения.
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 * {@link com.steammachine.common.lazyeval.LazyEval}
 * com.steammachine.common.lazyeval.LazyEval
 *
 **/
@FunctionalInterface
public interface LazyEval<T> {


    /**
     * @return вычисляемое значение
     */
    T value();

    /**
     *
     * интерфейс не предполагает реализующий классов - только один в методе {@link #eval(Supplier)}
     *
     * @return признак того было ли произведено вычисление
     */
    default boolean evaluated() {
        return false;
    }

    /**
     * Создает лямбда выражение для единожды вычисляемого значения
     *
     * @param eval - {@link Supplier} выражение для вычисления значения.
     * @param <T>  -
     * @return - VALUE
     */
    static <T> LazyEval<T> eval(Supplier<T> eval) {
        Objects.requireNonNull(eval);
        return new LazyEval<T>() {
            private T t;
            private volatile boolean evaluated;

            @Override
            public T value() {
                if (!evaluated) {
                    t = eval.get();
                    evaluated = true;
                }
                return t;
            }

            @Override
            public boolean evaluated() {
                return evaluated;
            }
        };
    }
}
