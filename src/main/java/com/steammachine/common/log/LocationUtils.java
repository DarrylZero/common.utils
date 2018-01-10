package com.steammachine.common.log;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Вспомогаетельный класс для обнаружения файлов класса по его имени и местонахождению
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 */
@Api(State.INTERNAL)
public class LocationUtils {


    private LocationUtils() {
    }

    /**
     * Находит полный путь до папки в которой расположен файла класса
     * Если файл расположен в архиве, результат папка архива, если файл класса лежит в чистом виде результат
     * дочерняя папка файла класса
     *
     * @param className полное имя класса с пакетом
     * @return полный путь до папки
     */
    public static String findClassFolder(String className) {
        if (className == null) {
            throw new NullPointerException("className is null");
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        if (!className.equals(clazz.getName())) {
            throw new IllegalStateException("!className.equals(clazz.getName())");
        }

        return findFolderOfClass(clazz);
    }


    /**
     * Находит полный путь до папки в которой расположен класс
     * Если файл расположен в архиве, результат папка архива, если файл класса лежит в чистом виде результат
     * дочерняя папка файла класса
     *
     * @param clazz класс
     * @return полный путь до папки
     */
    public static String findFolderOfClass(Class<?> clazz) {
        final URL classResource;
        final String packageName;
        {
            int lastDotIndex = clazz.getName().lastIndexOf('.');
            final String classResourceName = lastDotIndex == -1 ? clazz.getName() + ".class" :
                    clazz.getName().substring(lastDotIndex + 1, clazz.getName().length()) + ".class";
            packageName = lastDotIndex == -1 ? "" : clazz.getName().substring(0, lastDotIndex).replace(".", "/");
            classResource = clazz.getResource(classResourceName);
        }

        final URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
        try {

            switch (classResource.getProtocol().toLowerCase()) {
                case "zip":
                case "jar": {
                    return new File(url.toURI()).getParent();
                }

                case "file": {
                    return new File(new File(url.toURI()), packageName).getPath();
                }

                default: {
                    throw new IllegalStateException("unsupported protocol " + url.getProtocol());
                }
            }
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

}
