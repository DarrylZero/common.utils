package com.steammachine.common.commonspliterators;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Представляет собой простой сплитератор, который в однопоточном режиме превращает
 * переданный {@link java.util.Iterator} в последовательность элементов
 * <p>
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 * <p>
 * #DATE 30.08.2017 Добавить тестов и
 * {@link Iterator2SpliteratorBridge}
 * Iterator2SpliteratorBridge
 **/
public class Iterator2SpliteratorBridge<T> implements Spliterator<T> {

    private final Iterator<T> iterator;

    public Iterator2SpliteratorBridge(Iterator<T> iterator) {
        this.iterator = Objects.requireNonNull(iterator);
    }

    /**
     * фабричный метод создания экземпляра по переданному перечислите
     *
     * @param iterator -
     * @param <T>      -
     * @return - Новый экземпляра сплитератора
     */
    public static <T> Iterator2SpliteratorBridge<T> bridge(Iterator<T> iterator) {
        return new Iterator2SpliteratorBridge<>(iterator);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (!iterator.hasNext()) return false;
        action.accept(iterator.next());
        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE | ORDERED;
    }
}
