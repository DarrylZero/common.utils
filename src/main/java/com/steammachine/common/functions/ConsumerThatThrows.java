package com.steammachine.common.functions;

/**
 *
 * Аналог {@link java.util.function.Consumer} который также умеет выбрасывать исключения в момент выполнения.
 *
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 **/
public interface ConsumerThatThrows<T, EX extends Throwable> {

    void accept(T t) throws EX;
}
