package com.steammachine.common.validation;

import com.steammachine.common.utils.commonutils.CommonUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Вспомогательный класс проверки значений.
 * <p>
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 *
 * {@link Validation}
 * Validation
 **/
public class Validation {

    private Validation() {
    }

    /**
     * Метод проверяет, что переданное значение мапы
     * 1. не null
     * 2. не пусто, содержит хотя бы один элемент
     *
     * @param map - проверяемого значение
     * @param <K> тип ключа
     * @param <V> тип значения
     * @return проверяемое значение
     * @exception ValidationFailed в случае непройденной проверки
     */
    public static <K, V> Map<K, V> notEmpty(Map<K, V> map) {
        CommonUtils.check(() -> map != null, () -> new ValidationFailed("passed map can't be null"));
        CommonUtils.check(() -> !map.isEmpty(), () -> new ValidationFailed("passed map can't be empty"));
        return map;
    }

    /*
     * Проверка list значения
     *
     * Метод проверяет, что переданное значение коллекции
     * 1. не null
     * 2. не пусто, содержит хотя бы один элемент
     * 
     * @param collection коллекции
     * @param <T> тип элемента коллекции
     * @param <V> тип коллекции
     * @return проверяемое значение
     * @exception ValidationError в случае непройденной проверки
     */
    public static <T, V extends Collection<T>> V notEmpty(V collection) {
        CommonUtils.check(() -> collection != null, () -> new ValidationFailed("passed collection can't be null"));
        CommonUtils.check(() -> !collection.isEmpty(), () -> new ValidationFailed("passed collection can't be empty"));
        return collection;
    }

    /**
     * Проверка значения строки
     * <p>
     * Метод проверяет, что переданное значение строки
     * 1. не null
     * 2. не пусто, содержит хотя бы один символ
     *
     * @param value - проверяемое значение
     * @exception ValidationFailed в случае непройденной проверки
     */
    public static String notEmpty(String value) {
        CommonUtils.check(() -> value != null, () -> new ValidationFailed("passed string can't be null"));
        CommonUtils.check(() -> !value.isEmpty(), () -> new ValidationFailed("passed string can't be empty"));
        return value;
    }


    /**
     * Проверка значения объекта {@link File}
     *
     * Метод проверяет, что переданный объект
     * 1. не null
     * 2. указывает на не пустой путь
     * 3. файл или каталог по пути существует
     *
     *
     * @param file - проверяемый файл
     * @exception ValidationFailed в случае непройденной проверки
     */
    public static File fileOrDirExists(File file) {
        CommonUtils.check(() -> file != null, () -> new ValidationFailed("passed file can't be null"));
        CommonUtils.check(() -> !file.getPath().trim().isEmpty(), () -> new ValidationFailed("passed file path can't be empty"));
        CommonUtils.check(file::exists, () -> new ValidationFailed("No such file or directory -> \r\n" +
                file.getAbsolutePath()));
        return file;
    }

}
