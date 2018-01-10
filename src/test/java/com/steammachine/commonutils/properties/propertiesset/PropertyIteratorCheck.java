package com.steammachine.commonutils.properties.propertiesset;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import com.steammachine.common.utils.ResourceUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

import static com.steammachine.common.utils.commonutils.CommonUtils.getAbsoluteResourcePath;

/**
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 */
public class PropertyIteratorCheck {


    @Test
    public void testName() {
        Assert.assertEquals("com.steammachine.commonutils.properties.propertiesset.PropertyIterator",
                PropertyIterator.class.getName());
    }

    /* ------------------------ iterateProperties ------------------------------------ */

    @Test
    @Ignore
    public void iterateProperties10() {
        Iterable<PropertiesItem<String, Properties>> iterable =
                PropertyIterator.propertiesSet(Paths.get(getAbsoluteResourcePath(getClass(),
                "res/resource_props1.properties")));
        for (PropertiesItem<String, Properties> item : iterable) {
            switch (item.key()) {

            }


        }



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
    public static String _getAbsoluteResourcePath(Class<?> clazz, String resourceName) {
        URL resource = clazz.getResource(ResourceUtils.classFileName(clazz));
        if (resource == null) {
            throw new IllegalStateException("resource is null");
        }
        return Paths.get((new File(resource.getFile()).getParent()), resourceName).
                normalize().toFile().getAbsolutePath();
    }


}
