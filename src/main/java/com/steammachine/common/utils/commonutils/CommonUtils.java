package com.steammachine.common.utils.commonutils;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Вспомогательные методы работы с простыми данными
 * <p>
 * <p>
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */
@SuppressWarnings("ALL")
@Api(State.INCUBATING)
public final class CommonUtils {

    private static final String UTF_8 = "utf-8";
    private static final BigInteger TWO = new BigInteger("2");
    public static final String CLASS_EXTENSION = ".class";

    /**
     * Вспомогательный функциональный интерфейс - обертка вокруг исполняемого кода.
     */
    @FunctionalInterface
    public interface Code {
        /**
         * Код выполняемого метода .
         *
         * @throws Exception ошибка при выполнении кода
         */
        void execute() throws Exception;
    }

    /**
     * Вспомогательный функциональный интерфейс - обертка вокруг исполняемого кода.
     */
    @FunctionalInterface
    public interface Executable {
        /**
         * Код выполняемого метода .
         *
         * @throws Throwable ошибка при выполнении кода
         */
        void execute() throws Throwable;
    }


    /**
     * Вспомогательный нитерфейс для определения кода - ошибки которого должны быть подавлены
     *
     * @param <T>
     */
    @FunctionalInterface
    public interface SupressedExceptionSupplier<T> {

        /**
         * Метод с выполняемым кодом
         *
         * @return -
         * @throws Exception
         */
        T execute() throws Exception;
    }


    /**
     * Вспомогательный интерфейс для определения кода в котором ВСЕ checked exceptions,
     * <p>
     * <p>
     * NB!!!  Предпочтительнее использовать методы с параметром {@link SupressedExceptionSupplier}
     * т.к. подавление Throwable следует выполнять лишь в исключительных случаях.
     *
     * @param <T> - тип возвращаемого значения.
     */
    @FunctionalInterface
    public interface SupressedThrowableSupplier<T> {
        /**
         * Метод с выполняемым кодом
         *
         * @return -
         * @throws Throwable
         */
        T execute() throws Throwable;
    }

    private static final String SPACE = " ";

    private CommonUtils() {
    }

    /**
     * Приведение пути к общему виду - если конечный символ "/" он удаляется
     *
     * @param path -
     * @return -
     */
    public static String excludeTrailingBackslash(String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }

        int lastIndex = path.lastIndexOf('/');
        if (lastIndex != -1 && lastIndex == path.length() - 1) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * отрезает первый символ "/" если он есть.
     *
     * @param path -
     * @return -
     */
    public static String excludeFirstBackslash(String path) {
        if (path == null) {
            throw new NullPointerException("path is null");
        }

        int indexOf = path.indexOf('/');
        if (indexOf == 0) {
            path = path.substring(1, path.length());
        }
        return path;
    }

    /**
     * Ожидать выполнения всех потоков
     */
    public static boolean allThreadsAreDone(Thread... threads) {
        if (threads == null) {
            throw new NullPointerException("threads is null");
        }
        for (Thread thread : threads) {
            if (thread == null) {
                throw new NullPointerException("thread is null");
            }
        }

        for (Thread thread : threads) {
            if (thread.isAlive()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Приводит строку к нормализованному состоянию. Исключает лишние пробелы и переносы строк.
     * Может использоваться для сравнения "почти" одинаковых строк
     * <p>
     * Пример :
     * Сравниваемые строки
     * "1231 ONE"
     * "1231 O N E"
     * "1 2 3 1             O N E"
     * "1 2 3 1             ONE"
     * Будут идентичны
     *
     * @param data -
     * @return -
     */
    public static String normalizeString(String data) {
        StringBuilder builder = new StringBuilder();
        for (StringTokenizer tokenizer = new StringTokenizer(data, " \t\n\r\f\u00a0", false);
             tokenizer.hasMoreTokens(); ) {
            String token = tokenizer.nextToken();

            builder.append(token);
            builder.append(SPACE);
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - SPACE.length());
        }
        return builder.toString();
    }

    /**
     * Приводит строку к нормализованному состоянию.
     * Исключает все пробелы и все переносы строк.
     * Может использоваться для сравнения "почти" одинаковых строк
     * <p>
     * Пример :
     * Сравниваемые строки
     * "1231 ONE"
     * "1231 O N E"
     * "1 2 3 1             O N E"
     * "1 2 3 1             ONE"
     * <p>
     * "1231             ONE"
     * "1 2 3 1             O  N   E"
     * <p>
     * Будут идентичны
     *
     * @param data -
     * @return -
     */
    public static String removeSpacesAndReturns(String data) {
        StringBuilder builder = new StringBuilder();
        for (StringTokenizer tokenizer = new StringTokenizer(data, " \t\n\r\f\u00A0", false);
             tokenizer.hasMoreTokens(); ) {
            String next = tokenizer.nextToken().
                    replace(" ", "");
            builder.append(next);
        }
        return builder.toString();
    }

    /**
     * Метод сравнения содержимого 2х потоков
     *
     * @param stream1 поток 1
     * @param stream2 поток 2
     * @return если содержимое одного равно содержимому другого
     * @throws IOException -
     */
    public static boolean compareStreams(
            InputStream stream1,
            InputStream stream2) throws IOException {
        Objects.requireNonNull(stream1, "stream1 is null");
        Objects.requireNonNull(stream2, "stream2 is null");

        if (stream1 == stream2) {
            return true;
        }

        int read;
        while ((read = stream1.read()) == stream2.read()) {
            if (read == -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Сравнивает два объекта разных типов
     *
     * @param f    Функция сравнения двух объектов разных типов
     *             функция должна всегда выдавать не null результат
     *             (ссылка на функцию всегда не null)
     * @param <T1> - первый тип
     * @param <T2> - второй тип
     * @return результат сравнения {@code true} если объекты (условно) равны
     */
    public static <T1, T2> boolean objectsAreEqual(T1 t1, T2 t2, BiFunction<T1, T2, Boolean> f) {
        return Objects.requireNonNull(f).apply(t1, t2);
    }


    /**
     * Получить URI локального ресурса по относительному пути
     *
     * @param clazz                - класс, относительно которого производится поиск ресурса
     * @param relativeResourcePath - относительный путь до ресурса
     * @return -
     * @throws URISyntaxException
     */
    public static URI getResourceUri(
            Class<?> clazz,
            String relativeResourcePath) throws URISyntaxException {
        URL resource = clazz.getResource(relativeResourcePath);
        if (resource == null) {
            throw new IllegalStateException("clazz.getResource(" + relativeResourcePath + ") == null");
        }
        return resource.toURI();
    }

    /**
     * Получить абсолютный путь к ресурсу по его пути относительно класса.
     *
     * @param clazz        - класс от которого считается ресурс
     * @param resourceName - наименование ресурса
     * @return абсолютный путь к ресурсу
     * <p>
     * <p>
     */
    public static String getAbsoluteResourcePath(Class<?> clazz, String resourceName) {
        URL resource = clazz.getResource(clazz.getSimpleName() + CLASS_EXTENSION);
        if (resource == null) {
            throw new IllegalStateException("resource is null");
        }
        return Paths.get(excludeTrailingBackslash(new File(resource.getFile()).getParent()),
                "/", excludeFirstBackslash(resourceName)).
                normalize().toFile().getAbsolutePath();
    }


    /**
     * простой метод ( shortcut )преобразования в массив
     *
     * @param length -  проверочная длина массива
     * @param i      - массив
     * @return копия массива
     * <p>
     * выбрасывает исключение в том случае  если значение передаваемой длины массива не соответствует фактической  длине
     */
    public static int[] asArray(int length, int... i) {
        if (i == null) {
            throw new NullPointerException("i is null");
        }
        if (length != i.length) {
            throw new IllegalStateException("length of an array " + Arrays.toString(i) +
                    " does not correspond to " + length);
        }
        return i.clone();
    }

    /**
     * Простой метод ( shortcut )преобразования в массив
     *
     * @param length -  длина массива для проверки
     * @param items  - массив
     * @return копия массива
     * <p>
     * выбрасывает исключение в том случае  если значение передаваемой длины массива не соответствует фактической  длине
     */
    @SafeVarargs
    public static <T> T[] checkToArray(int length, T... items) {
        if (length != Objects.requireNonNull(items, "items is null").length) {
            throw new IllegalStateException("length of an array " + Arrays.toString(items) +
                    " does not correspond to " + length);
        }
        return items.clone();
    }

    /**
     * Простой метод ( shortcut )преобразования в массив
     *
     * @param items - массив
     * @return копия массива
     * <p>
     * выбрасывает исключение в том случае  если значение передаваемой длины массива не соответствует фактической  длине
     */
    @SafeVarargs
    public static <T> T[] toArray(T... items) {
        return checkToArray(Objects.requireNonNull(items, "items is null").length, items);
    }


    /**
     * Получает и открывает поток для чтения с содержиммым класса.
     *
     * @param clazz - класс
     * @return поток для чтения с содержиммым класса
     * @throws IOException
     */
    public static InputStream openClassResource(Class<?> clazz) throws IOException {
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }

        CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
        if (codeSource == null) {
            /* тот случай когда для классса нет codeSource */
            throw new IllegalStateException("clazz.getProtectionDomain().getCodeSource() is null");
        }

        final URL url = codeSource.getLocation();
        final int lastDotIndex = clazz.getName().lastIndexOf('.');
        final String classResourceName = lastDotIndex == -1 ? clazz.getName() + CLASS_EXTENSION :
                clazz.getName().substring(lastDotIndex + 1, clazz.getName().length()) + CLASS_EXTENSION;
        final String packageName = lastDotIndex == -1 ? "" : clazz.getName().substring(0, lastDotIndex).replace('.', '/');
        final URL classResource = clazz.getResource(classResourceName);

        try {
            switch (classResource.getProtocol().toLowerCase()) {
                case "zip":
                case "jar": {
                    try (ZipFile zip = new ZipFile(url.getFile())) {
                        ZipEntry zipEntry = zip.getEntry(packageName + "/" + classResourceName);
                        try (InputStream inputStream = zip.getInputStream(zipEntry)) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            CommonUtils.copyStreamContent(inputStream, outputStream);
                            outputStream.flush();
                            return new ByteArrayInputStream(outputStream.toByteArray());
                        }
                    }
                }

                case "file": {
                    return new FileInputStream(url.getFile() + System.getProperty("file.separator") +
                            packageName + System.getProperty("file.separator") + classResourceName);
                }

                default: {
                    throw new IllegalStateException("unsupported protocol " + url.getProtocol());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Получает копию ресурса из zip файла - по пути относительному к классу и пути внутри zip файла
     *
     * @param clazz        Класс относительно которого производится поиск ресурса
     * @param relativePath - относительный (класса) путь до zip ресурса
     * @param zipItemPath  - наименование пути ВНУТРИ zip
     * @return поток с копией ресурса - содержащегося по пути внутри
     * @throws IOException при невозможности прочитать элемент из архива
     */
    public static InputStream loadResourceFromZipFile(
            Class<?> clazz,
            String relativePath,
            String zipItemPath) throws IOException {

        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        if (relativePath == null) {
            throw new NullPointerException("relativePath is null");
        }
        if (zipItemPath == null) {
            throw new NullPointerException("zipItemPath is null");
        }
        String absolutePathToZip = getAbsoluteResourcePath(clazz, relativePath);

        try (ZipFile zip = new ZipFile(absolutePathToZip)) {
            ZipEntry entry = zip.getEntry(zipItemPath);
            if (entry == null) {
                throw new IllegalStateException("zip.getEntry(" + zipItemPath + ") == null. Item " + zipItemPath +
                        " not found in zip file " + zipItemPath);
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try (InputStream inputStream = new BufferedInputStream(zip.getInputStream(entry), 1024)) {
                copyStreamContent(inputStream, buffer);
                buffer.flush();
            }
            return new ByteArrayInputStream(buffer.toByteArray());
        }
    }

    /**
     * Расширяет имеющиийся список до указанного размера - если передаваемый список содержит меньшее количество
     *
     * @param list список
     * @param size размер - положительное число
     * @return переданная ссылка на список
     */
    public static <T> List<T> expandListToSize(List<T> list, int size) {
        while (list.size() < size) {
            list.add(null);
        }
        return list;
    }

    /**
     * Получает значение индификатора из передаваемой строки
     *
     * @param data - строка - не null
     * @return null - если из переданной строки нельзя получить числа - число - если число получить можно.
     */
    public static <T, K> T getValueFunction(
            K data,
            Function<K, T> function) {

        if (data == null) {
            throw new NullPointerException("data is null");
        }
        return function.apply(data);
    }

    /**
     * измеряет время выполнения переданного кода
     *
     * @param code  - код
     * @param count количество выполнений кода
     * @return время выполнения
     * @throws Exception - ошибка внутри выполняемого кода
     */
    public static long measureTime(Code code, int count) throws Exception {
        Objects.requireNonNull(code, "code is null");
        if (count <= 0) {
            throw new IllegalArgumentException("count must be a positive number");
        }
        long l = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            code.execute();
        }
        return System.currentTimeMillis() - l;
    }

    /**
     * измеряет время выполнения переданного кода
     *
     * @param code - код
     * @return время выполнения
     * @throws Exception - ошибка внутри выполняемого кода
     */
    public static long measureTime(Code code) throws Exception {
        return measureTime(code, 1);
    }

    /**
     * Вызывается для подавления результата выполнения метода,
     * выполнение которого создает какой либо побочный эффект.
     *
     * @param t   - параметр
     * @param <T> - тип параметра
     */
    public static <T> void suppressResult(@SuppressWarnings("UnusedParameters") T t) {
        /* by design */
    }

    /**
     * Собрать строку из передаваемых объектов
     * при сборке используется toString() каждого объекта.
     * каждый объект должен быть не null;
     *
     * @param testStepInfoParts -
     * @return - конкатенированная строка
     * <p>
     */
    public static String compileString(Object mandatoryPart, Object... testStepInfoParts) {
        Objects.requireNonNull(mandatoryPart, "mandatoryPart is null");
        Objects.requireNonNull(testStepInfoParts, "testStepInfoParts is null");
        for (Object o : testStepInfoParts) {
            Objects.requireNonNull(o, "one of params is null " + Arrays.toString(testStepInfoParts));
        }
        StringBuilder builder = new StringBuilder();
        builder.append(mandatoryPart);
        for (Object o : testStepInfoParts) {
            builder.append(o);
        }
        return "" + builder;
    }


    /**
     * Проверяет, что передаваемый объект определен (не null).
     *
     * @param t        - объект для проверки.
     * @param messages - набор сообщений для.
     * @param <T>      - тип объекта.
     * @return - объект переданный в параметре.
     */
    public static <T> T checkDefined(T t, Object... messages) {
        if (t == null) {
            throw new IllegalArgumentException(CommonUtils.compileString(messages));
        }
        return t;
    }


    /**
     * Открытие проверяемых исключений метода.
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Если при выполнении кода выбрасываются ошибки - эти ошибки оборачиваются
     * {@link java.lang.IllegalStateException}
     *
     * @param code выполняемый код
     */
    public static void suppressExceptions(Code code) {
        try {
            code.execute();
        } catch (Throwable throwable) {
            throw new IllegalStateException(throwable);
        }

    }

    /**
     * Выполнить ожидание в течении delay миллисекунд
     *
     * @param delay {@link java.lang.Thread#sleep(long)}
     */
    public static void delay(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Подавление проверяемых исключений метода.
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Если при выполнении кода выбрасываются ошибки - эти ошибки оборачиваются
     * {@link java.lang.IllegalStateException}
     *
     * @param code         выполняемый код
     * @param <T>          возвращаемый тип данных
     * @param defaultValue - значение по умолчанию, выдаваемое в  случае выброса исключения.
     * @return результат выполнения
     */
    public static <T> T skipExceptions(SupressedExceptionSupplier<T> code, T defaultValue) {
        return skipExceptions(code, (Function<Exception, T>) e -> defaultValue);
    }

    /**
     * Подавление проверяемых исключений метода.
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Если при выполнении кода выбрасываются ошибки - эти ошибки оборачиваются
     * {@link java.lang.IllegalStateException}
     *
     * @param code         выполняемый код
     * @param <T>          возвращаемый тип данных
     * @param defaultValue - значение по умолчанию, выдаваемое в  случае выброса исключения.
     * @return результат выполнения
     */
    public static <T> T skipExceptions(SupressedExceptionSupplier<T> code, Function<Exception, T> defaultValue) {
        try {
            return code.execute();
        } catch (Exception e) {
            return defaultValue.apply(e);
        }
    }

    /**
     * Подавление проверяемых исключение метода.
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Ошибки, выбрасываемые в выполняемом коде, игнорируются.
     *
     * @param code          выполняемый код
     * @param errorCaseCode код, выполняемый при возникновении ошибки (если определен)
     */
    public static void skipExceptions(Code code, Runnable errorCaseCode) {
        try {
            code.execute();
        } catch (Throwable e) {
            if (errorCaseCode != null) {
                errorCaseCode.run();
            }
        }
    }

    /**
     * Подавление проверяемых исключение метода.
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Ошибки, выбрасываемые в выполняемом коде, игнорируются.
     *
     * @param code          выполняемый код
     * @param errorCaseCode код, выполняемый при возникновении ошибки (если определен)
     */
    public static void skipExceptions(Code code, Consumer<Throwable> errorCaseCode) {
        try {
            code.execute();
        } catch (Throwable e) {
            if (errorCaseCode != null) {
                errorCaseCode.accept(e);
            }
        }
    }

    /**
     * Подавление проверяемых исключение метода.
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Ошибки, выбрасываемые в выполняемом коде, игнорируются.
     *
     * @param code выполняемый код
     */
    public static void skipExceptions(Code code) {
        skipExceptions(code, (Runnable) null);
    }

    /**
     * Подавление проверяемых исключение метода.
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Ошибки, выбрасываемые в выполняемом коде, игнорируются.
     *
     * @param code выполняемый код
     */
    public static void skipAllExceptions(Executable code) {
        Objects.requireNonNull(code, "code is null");
        try {
            code.execute();
        } catch (Throwable ignored) {
            /* для этого и предназначен метод */
        }
    }


    /**
     * Подавить ошибки выполняемого кода - используется в тех случаях когда требуется не
     * пробрасывать Checked exception по всему коду или окружать его try catch.
     *
     * @param supplier supplier выполняемого кода
     * @param <T>      тип возвращаемого значения
     * @return результат выполнения
     */
    public static <T> T suppress(SupressedExceptionSupplier<T> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return supplier.execute();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Подавить ошибки выполняемого кода - используется в тех случаях когда требуется не
     * пробрасывать Checked exception по всему коду или окружать его try catch.
     *
     * @param supplier supplier выполняемого кода
     * @param <T>      тип возвращаемого значения
     */
    public static void suppressWOResult(Code code) {
        Objects.requireNonNull(code);
        try {
            code.execute();
        } catch (RuntimeException e) {
            throw ((RuntimeException) e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Подавить ошибки выполняемого кода - используется в тех случаях когда требуется не
     * пробрасывать Checked exception по всему коду или окружать его try catch.
     *
     * @param supplier supplier выполняемого кода
     * @param <T>      тип возвращаемого значения
     */
    public static void suppressAllWOResult(Executable code) {
        Objects.requireNonNull(code);
        try {
            code.execute();
        } catch (RuntimeException e) {
            throw ((RuntimeException) e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Подавить ошибки выполняемого кода - используется в тех случаях когда требуется не
     * пробрасывать Checked exception по всему коду или окружать его try catch.
     *
     * @param supplier supplier выполняемого кода
     * @param <T>      тип возвращаемого значения
     * @param type     тип возвращаемого значения - класс к которому производится неявное приведение
     * @return результат выполнения
     */
    public static <T> T suppress(SupressedExceptionSupplier<T> suppressedCode, Class<T> type) {
        Objects.requireNonNull(type);
        return suppress(suppressedCode);
    }

    /**
     * Подавить ошибки выполняемого кода - используется в тех случаях когда требуется не пробрасывать Checked
     * exception по всему коду
     * <p>
     * метод добавляет все ошибки в том числе Throwable
     * В большинстве случаев требуется использовать {@link #suppress(SupressedExceptionSupplier)}
     *
     * @param supplier supplier выполняемого кода
     * @param <T>      тип возвращаемого значения
     * @return результат
     */
    public static <T> T suppressAll(SupressedThrowableSupplier<T> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return supplier.execute();
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Делает абсолютно тоже что и метод {@link #suppressAll(SupressedThrowableSupplier)} только с
     * явным (жестким) приведением результата к требуемому типу.
     *
     * @param supplier supplier выполняемого кода
     * @param <T>      тип возвращаемого значения
     * @return результат
     */
    public static <T> T suppressAll(SupressedThrowableSupplier<T> supplier, Class<T> resutType) {
        return suppressAll(supplier);
    }

    /**
     * Проверяет что передаваемый объект не null если это так - возвращает его.
     * Eсли нет -- объект возвращается  поставщиком (supplier)
     *
     * @param t        проверяемый объект
     * @param supplier поставщик нового объекта - должен быть определен и возвращать не нулевое значение при вызове
     *                 его метода.
     * @param <T>      тип объекта
     * @return объект
     */
    public static <T> T ensure(T t, Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return t != null ? t : Objects.requireNonNull(supplier.get());
    }


    /**
     * Метод берет данные из потока ввода и записывает в поток вывода,
     * при перемещении данных используется небольшой буфер.
     * Внутри метода производится только операции чтения и записей потоков.
     * Явных вызовов операций close и flush не производится внутри метода.
     *
     * @param inputStream  Поток ввода(всегда не null).
     * @param outputStream Поток вывода(всегда не null).
     * @throws IOException В случае ошибки выполнения операций работы с потоком.
     */
    public static void copyStreamContent(InputStream inputStream, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(inputStream, "inputStream is null");
        Objects.requireNonNull(outputStream, "outputStream is null");

        byte[] buffer = new byte[128];
        int actuallyRead;
        while ((actuallyRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, actuallyRead);
        }
    }


    /**
     * Преобразование массива к списку
     *
     * @param arr - массив (всегда не null)
     *            * @param <T> тип списка
     * @return изменяемый список
     */
    public static <T> List<T> arrayToList(T... arr) {
        Objects.requireNonNull(arr, "arr is null");
        return new ArrayList<>(Arrays.asList(arr));
    }

    /**
     * Метод осуществляет "слияние" двух объектов - проверяя при этом на уникальность передаваемых параметров -
     * <p>
     * допускается один аргумент null
     * если оба аргумента не null то проверяется что это один и тот же объект.
     * <p>
     * Метод предназначен для удобства использования внутри операций свертки
     * {@link java.util.stream.Stream#reduce(Object, BinaryOperator)}
     * {@link java.util.stream.Stream#reduce(BinaryOperator)}
     * <p>
     * в тех случаях когда предполагается, что все элементы обрабатываемые потоком одинаковые
     * (либо обрабатыватся только один элемент)
     *
     * @param t1  -
     * @param t2  -
     * @param <T> -
     * @return -
     */
    public static <T> T reduceUnique(T t1, T t2) {
        if (t1 == null && t2 == null) {
            throw new IllegalArgumentException("one of arguments must be defined.");
        }

        if (t1 != null && t2 != null && t1 != t2) {
            throw new IllegalArgumentException("defined arguments must be equal.");
        }
        return t1 == null ? t2 : t1;
    }


    /**
     * Возвращает пересечение всех входящих множеств.
     * Если элемент встречается более одного раза - в результирующей коллекции останется только один.
     *
     * @param set1 множество 1
     * @param set2 множество 1
     * @param <T>  тип элемента
     * @return пересечение всех входящих множеств (всегда не null)
     */
    @SafeVarargs
    public static <T> Collection<T> intersectCollections(Collection<T>... sets) {
        Objects.requireNonNull(sets);
        Stream.of(sets).forEach(Objects::requireNonNull);
        Set<T> collect = Stream.of(sets).collect(HashSet::new, (c1, c2) -> c1.addAll(c2), (c1, c2) -> c1.addAll(c2));
        Stream.of(sets).forEach(collect::retainAll);
        return collect;
    }


    /**
     * Вычисление значения за один проход
     * <p>
     * <p>
     * <p>
     * <p>
     * пример использования
     * <b>
     * <p>
     * <p>
     * <p>
     * </b>
     * <p>
     * public void example() {
     * <p>
     * List<String> eval = eval(() -> {
     * <p>
     * ArrayList<String> strings = new ArrayList<String>();
     * <p>
     * <p>
     * return strings
     * <p>
     * });
     * <p>
     * }
     *
     * @param supplier выполняемый код (всегда не null)
     * @param <T>      тип возвращяемого значения.
     * @return возвращяемое значение.
     */
    public static <T> T eval(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return supplier.get();
    }


    /**
     * Проверить условие и выбросить исключение если условие НЕ истинно
     *
     * @param condition - фабрика условия
     * @param error     - фабрика исключения
     */
    public static void check(BooleanSupplier condition, Supplier<? extends RuntimeException> error) {
        Objects.requireNonNull(error);
        Objects.requireNonNull(condition);
        if (!condition.getAsBoolean()) {
            throw error.get();
        }
    }

    /**
     * Произвести проверку и выдать результат
     * <p>
     * По принципу работы  аналогично методу {@link Objects#requireNonNull(Object, String)}
     * Когда проверяется что переданный аргумент не null  и он же выдается в качестве результата
     *
     * @param supplier  - supplier для предоставления результата
     * @param predicate - функционал проверки полученоого аргумента
     * @param error     - supplier  для выдачи исключения соотв вида
     * @param <T>
     * @return проверенного значение полученное в результате вызова supplier
     */
    public static <T> T check(
            Supplier<T> supplier,
            Predicate<T> predicate,
            Supplier<? extends RuntimeException> error) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(error);

        T result = supplier.get();
        if (!predicate.test(result)) {
            throw error.get();
        }
        return result;

    }


    /**
     * Подавление проверяемых исключение метода.
     * если при выполнении кода возникает исключение - экземпляр этого исключения передается в {@link Consumer}
     * (если он определен)
     * <p>
     * Используется для подавления выброса ошибки в пользовательском коде.
     * Ошибки, выбрасываемые в выполняемом коде, игнорируются.
     *
     * @param code          выполняемый код (всегда не null)
     * @param errorCaseCode код, выполняемый при возникновении ошибки (если определен)
     */
    public static void skipErrors(CommonUtils.Code code, Consumer<Throwable> errorCaseCode) {
        Objects.requireNonNull(code);
        try {
            code.execute();
        } catch (Throwable e) {
            if (errorCaseCode != null) {
                errorCaseCode.accept(e);
            }
        }
    }


    /**
     * Получить записанный stacktrace для исключения
     *
     * @param t - исключение
     * @return строка с данными stacktrace
     */
    public static String stackTrace(Throwable t) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            try (PrintStream s = new PrintStream(stream)) {
                t.printStackTrace(s);
                return CommonUtils.suppress(() -> stream.toString(UTF_8));
            }
        } finally {
            CommonUtils.suppressWOResult(stream::close);
        }
    }


    /**
     * Метод, который выдает {@link Iterable}, {@link Iterable#iterator()} которого последовательно перечисляет все
     * возможные комбинации перечисления, начиная с пустого множества.
     * Внимание!!! получаемый при вызове {@link Iterable#iterator()} метод имеет высокую стоимость выполнения для
     * большого количества элментов, которая в общем случае, считается как n - нная степень двойки + 1.
     * Где n это количество всех элементов перечисления.
     *
     * @param clazz - класс перечисления
     * @param <T>   - тип перечисления
     * @return {@link Iterable} всегда не null
     */
    public static <T extends Enum> Iterable<Set<T>> setCombinations(Class<T> clazz) {
        CommonUtils.check(clazz::isEnum, IllegalStateException::new);

        class Pair<T> {
            final int i;
            final T t;

            Pair(int i, T t) {
                this.i = i;
                this.t = t;
            }

            public int i() {
                return i;
            }

            public T t() {
                return t;
            }
        }

        return new Iterable<Set<T>>() {

            final List<T> enumElements = Arrays.asList(clazz.getEnumConstants());
            final BigInteger max = TWO.pow(enumElements.size()).subtract(BigInteger.ONE);
            final Map<Integer, T> association = Stream.iterate(0, i -> i + 1).limit(enumElements.size()).
                    map(n -> new Pair<T>(n, enumElements.get(n))).collect(Collectors.toMap(p -> p.i(), p -> p.t()));

            @Override
            public Iterator<Set<T>> iterator() {
                return new Iterator<Set<T>>() {
                    BigInteger current = BigInteger.ZERO;

                    @Override
                    public boolean hasNext() {
                        return max.compareTo(current) >= 0;
                    }

                    @Override
                    public Set<T> next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }

                        BigInteger cur = this.current;
                        this.current = this.current.add(BigInteger.ONE);
                        return BigInteger.ZERO.equals(cur) ?
                                Collections.emptySet() :
                                Collections.unmodifiableSet(Stream.of(cur).
                                        flatMap(this::getStream).collect(Collectors.toSet()));
                    }

                    private Stream<T> getStream(BigInteger number) {
                        Stream.Builder<T> builder = Stream.builder();
                        for (int bitNo = 0; bitNo < number.bitLength(); bitNo++) {
                            if (number.testBit(bitNo)) {
                                builder.add(association.computeIfAbsent(bitNo, integer -> {
                                    throw new IllegalStateException();
                                }));
                            }
                        }

                        return builder.build();
                    }
                };
            }
        };
    }


}
