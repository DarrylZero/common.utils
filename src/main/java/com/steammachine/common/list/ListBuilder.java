package com.steammachine.common.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 * {@link ListBuilder}
 **/
public class ListBuilder<T> {

    private final List<T> list = new ArrayList<>();

    private ListBuilder() {
    }

    public static <T> ListBuilder<T> of() {
        return new ListBuilder<>();
    }

    /**
     * Очистка содержимого
     *
     * @return
     */
    public final ListBuilder<T> clear() {
        list.clear();
        return this;
    }

    /**
     * Добавление элемегт
     * @param t
     * @return ссылка на этот объект построителя
     */
    @SafeVarargs
    public final ListBuilder<T> add(T... t) {
        list.addAll(Arrays.asList(t));
        return this;
    }

    /**
     * Конфигурационная процедура
     *
     * @param consumer консумер (Всегда не null)
     * @return ссылка на этот объект построителя
     */
    public final ListBuilder<T> conf(Consumer<ListBuilder<T>> consumer) {
        consumer.accept(this);
        return this;
    }

    /**
     * Последовательное добавление списков
     *
     * @param lists Список списков
     * @return ссылка на этот объект построителя
     */
    @SafeVarargs
    public final ListBuilder<T> addList(List<T>... lists) {
        Stream.of(lists).forEachOrdered(this.list::addAll);
        return this;
    }

    public ListBuilder<T> merge(ListBuilder<T> builder) {
        if (this != builder) {
            this.list.addAll(builder.list);
        }
        return this;
    }

    /**
     * отдает немодифицируемый список из элементов собранных
     * @return
     */
    public List<T> unmodifiedList() {
        return Collections.unmodifiableList(list);
    }

}
