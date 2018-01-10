package com.steammachine.common.utils.resourceutulstest;

import org.junit.Assert;
import org.junit.Test;
import com.steammachine.common.utils.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 *
 *
 * @author Vladimir Bogodukhov
 */
public class ResourceUtilsCheck {
    private static final Charset CHARSET = Charset.forName("utf-8");
    private static final Map<String, String> PROPERTIES = Collections.unmodifiableMap(
            new HashMap<String, String>() {
                {
                    put("property1", "value1");
                    put("property2", "value2");
                    put("property3", "value3");
                }
            });
    private static final Set<String> PROPERTY_KEYS = PROPERTIES.keySet();


    /* ----------------------------------------- loadResourceByRelativePath -------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void testLoadResourceByRelativePath_checkError() {
        ResourceUtils.loadResourceByRelativePath(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testLoadResourceByRelativePath_1() {
        ResourceUtils.loadResourceByRelativePath(ResourceUtilsCheck.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void testLoadResourceByRelativePath_2() {
        ResourceUtils.loadResourceByRelativePath(null, "");
    }

    /**
     * тут проверяется что находится ресурс по обозначенному пути НЕ Находится
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadResourceByRelativePath_3() {
        ResourceUtils.loadResourceByRelativePath(ResourceUtilsCheck.class, "resources/resource_that_cannot_be_found.txt");
    }

    /**
     * Тут проверяется что находится ресурс по обозначенному пути НЕ Находится
     * resourse_that_cannot_be_found_1.txt
     * <p>
     * При вызове в ручном режиме тест не отрабатывает
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoadResourceByRelativePath_4() {
        ResourceUtils.loadResourceByRelativePath(ResourceUtilsCheck.class, "resources/resourse_that_cannot_be_found_1.txt");
    }

    /**
     * тут проверяется что ресурс по обозначенному пути может быть получен
     */
    @Test
    public void testLoadResourceByRelativePath_ResourceIsNOtFound() {
        ResourceUtils.loadResourceByRelativePath(ResourceUtilsCheck.class, "res/resource_text_file_1.txt");
    }


    /* --------------------------------------------- loadResourceAsString ---------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void loadResourceAsString_wrong_params1() throws IOException {
        ResourceUtils.loadResourceAsString(null, null, null);
    }


    @Test(expected = NullPointerException.class)
    public void loadResourceAsString_wrong_params2() throws IOException {
        ResourceUtils.loadResourceAsString(null, null, CHARSET);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceAsString_wrong_params3() throws IOException {
        ResourceUtils.loadResourceAsString(null, "", CHARSET);
    }

    @Test
    public void loadResourceAsString_normal_execution() throws IOException {
        ResourceUtils.loadResourceAsString(ResourceUtilsCheck.class, "res/resource_utf-text.txt", CHARSET);
    }

    @Test
    public void loadResourceAsString_normal_execution2() throws IOException {
        Assert.assertEquals(
                ResourceUtils.loadResourceAsString(ResourceUtilsCheck.class, "res/resource_utf-text.txt", CHARSET),
                "UNO DOS TRES"
        );
    }


    /* ------------------------------------------  loadResourceAsProperties ---------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void loadResourceAsProperties_Error1() throws IOException {
        ResourceUtils.loadResourceAsProperties(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceAsProperties_Error2() throws IOException {
        ResourceUtils.loadResourceAsProperties(ResourceUtilsCheck.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceAsProperties_Error3() throws IOException {
        ResourceUtils.loadResourceAsProperties(null, "resources/resource_properties1.properties"); /*< --- этот ресурс есть */
    }

    @Test
    public void loadResourceAsPropertiesNormalExecution1() throws IOException {
        ResourceUtils.loadResourceAsProperties(ResourceUtilsCheck.class, "res/resource_properties1.properties");
    }

    @Test
    public void loadResourceAsPropertiesNormalExecution2() throws IOException {
        Properties properties = ResourceUtils.
                loadResourceAsProperties(ResourceUtilsCheck.class, "res/resource_properties1.properties");
        Enumeration<?> enumeration = properties.propertyNames();
        for (; enumeration.hasMoreElements(); ) {
            String element = (String) enumeration.nextElement();
            Assert.assertTrue(PROPERTY_KEYS.contains(element));
        }
    }

    @Test
    public void loadResourceAsPropertiesNormalExecution3() throws IOException {
        Properties properties = ResourceUtils.
                loadResourceAsProperties(ResourceUtilsCheck.class, "res/resource_properties1.properties");

        Enumeration<?> enumeration = properties.propertyNames();
        for (; enumeration.hasMoreElements(); ) {
            String element = (String) enumeration.nextElement();
            Assert.assertTrue(PROPERTY_KEYS.contains(element));
            Assert.assertEquals(PROPERTIES.get(element), properties.getProperty(element));
        }
    }

    /* -------------------------------------------- loadResourceAsBytes  --------------------------------------------*/
    @Test(expected = NullPointerException.class)
    public void loadResourceAsBytesError1() throws IOException {
        ResourceUtils.loadResourceAsBytes(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceAsBytesError2() throws IOException {
        ResourceUtils.loadResourceAsBytes(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceAsBytesError3() throws IOException {
        ResourceUtils.loadResourceAsBytes(ResourceUtilsCheck.class, null);
    }

    @Test
    public void loadResourceAsBytes5() throws IOException {
        Assert.assertArrayEquals(
                ResourceUtils.loadResourceAsBytes(ResourceUtilsCheck.class, "res/resource_utf-text.txt"),
                new byte[]{85, 78, 79, 32, 68, 79, 83, 32, 84, 82, 69, 83}
        );
    }

    /* -------------------------------------------- loadProperties  --------------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void loadProperties10() throws IOException {
        ResourceUtils.loadProperties(null);
    }

    @Test
    public void loadProperties20() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream("res/resource_properties1.properties")) {
            Properties properties = ResourceUtils.loadProperties(stream);
            Assert.assertEquals("value1", properties.getProperty("property1"));
            Assert.assertEquals("value2", properties.getProperty("property2"));
            Assert.assertEquals("value3", properties.getProperty("property3"));
        }

    }

/* --------------------------------------------- classFileName --------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void classFileName10() {
        ResourceUtils.classFileName(null);
    }

    @Test
    public void classFileName20() {
        Assert.assertEquals("ResourceUtilsCheck.class", ResourceUtils.classFileName(ResourceUtilsCheck.class));
    }

    private class C1 {
    }

    @Test
    public void classFileName30() {
        Assert.assertEquals("ResourceUtilsCheck$C1.class", ResourceUtils.classFileName(C1.class));
    }

    private static class C2 {
    }

    @Test
    public void classFileName40() {
        Assert.assertEquals("ResourceUtilsCheck$C2.class", ResourceUtils.classFileName(C2.class));
    }

}