package com.steammachine.common.utils;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Properties;

/**
 * Вспомогательный класс используется для загрузки локальных ресурсов из проекта
 * <p>
 * <p>
 * <p>
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */
@Api(State.EXPERIMENT)
public class ResourceUtils {

    private ResourceUtils() {
    }

    /**
     * Загрузить ресурс хранящийся по пути relativeResourcePath относительно класса startClass
     *
     * @param startClass           - класс, относительно которого ищется ресурс
     * @param relativeResourcePath относительный путь расположения ресурса
     * @return поток с ресурсом - после использования требуется закрытие
     * @throws IllegalArgumentException в случае если ресурс не может быть найден
     */
    public static InputStream loadResourceByRelativePath(Class startClass, String relativeResourcePath) {
        Objects.requireNonNull(startClass, "startClass is null");
        Objects.requireNonNull(relativeResourcePath, "relativeResourcePath is null");

        InputStream resource = startClass.getResourceAsStream(relativeResourcePath);

        if (resource == null) {
            /* ресурс не найден */
            throw new IllegalArgumentException("resource with path " + relativeResourcePath +
                    " is not found. search from class " + startClass);
        }

        return resource;
    }


    /**
     * Загрузить ресурс в виде строки.
     *
     * @param startClass   - класс, относительно которого ищется ресурс
     * @param resourcePath относительный путь расположения ресурса
     * @param charset      чарсет в котором представлены символы
     * @return resource as a string
     * @throws java.io.IOException в случае если ресурс не может быть прочитан
     */
    public static String loadResourceAsString(
            Class startClass,
            String resourcePath,
            Charset charset) throws IOException {

        byte[] buff = new byte[512];
        try (InputStream inputStream = loadResourceByRelativePath(startClass, resourcePath)) {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                int readCount = 0;
                while ((readCount = inputStream.read(buff)) != -1) {
                    stream.write(buff, 0, readCount);
                }
                return new String(stream.toByteArray(), charset);
            }
        }
    }

    /**
     * прочитать ресурс по относительному пути - как массив байт
     *
     * @param startClass   - класс относительно которого производится поиск
     * @param resourcePath - относительный путь
     * @return - массив байт
     * @throws java.io.IOException -
     */
    public static byte[] loadResourceAsBytes(Class startClass, String resourcePath) throws IOException {
        try (InputStream stream = loadResourceByRelativePath(startClass, resourcePath)) {
            byte[] buf = new byte[128];

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read;
            while ((read = stream.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
            return out.toByteArray();
        }
    }


    /**
     * Читает данные из потока в объект {@link java.util.Properties} внутри метода не производится закрытие потока.
     *
     * @param inputStream поток
     * @return объект свойств
     * @throws java.io.IOException - в случае невозможности прочтения
     */
    public static Properties loadProperties(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    /**
     * Загрузить ресурс в виде свойств {@link java.util.Properties})
     * *
     *
     * @param startClass   - класс, относительно которого ищется ресурс
     * @param resourcePath относительный путь расположения ресурса
     * @return
     * @throws IllegalArgumentException в случае если ресурс не может быть найден
     * @throws java.io.IOException      в случае если ресурс не может быть прочитан
     */
    public static Properties loadResourceAsProperties(
            Class startClass,
            String resourcePath) throws IOException {
        try (InputStream inputStream = loadResourceByRelativePath(startClass, resourcePath)) {
            return loadProperties(inputStream);
        }
    }


    /**
     * Получить имя файла класса
     *
     * @param clazz класс (не null)
     * @return (не null)
     */
    public static String classFileName(Class clazz) {
        Objects.requireNonNull(clazz);
        String className = clazz.getPackage() != null ?
                clazz.getName().substring((clazz.getPackage().getName() + ".").length()) : clazz.getName();
        return className + ".class";
    }


}
