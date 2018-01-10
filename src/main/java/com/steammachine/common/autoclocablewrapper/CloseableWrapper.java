package com.steammachine.common.autoclocablewrapper;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 *
 * {@link CloseableWrapper}
 **/
public class CloseableWrapper<T> implements Closeable {


    private final T nexus;
    private final Consumer<T> consumer;


    public static <T> CloseableWrapper<T> closeable(T nexus, Consumer<T> consumer) {
        return new CloseableWrapper<>(nexus, consumer);
    }

    public CloseableWrapper(T nexus, Consumer<T> consumer) {
        this.nexus = nexus;
        this.consumer = Objects.requireNonNull(consumer);
    }

    public T nexus() {
        return nexus;
    }

    @Override
    public void close() throws IOException {
       consumer.accept(nexus);
    }
}
