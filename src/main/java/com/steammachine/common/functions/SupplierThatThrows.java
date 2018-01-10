package com.steammachine.common.functions;

/**
 * Аналог {@link java.util.function.Supplier} который также умеет выбрасывать исключения в момент выполнения.
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 **/
public interface SupplierThatThrows<T, E extends Throwable> {

    T get() throws E;
}
