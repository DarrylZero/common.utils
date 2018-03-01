package com.steammachine.commonutils.orderedproperties;

import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.common.utils.commonutils.CommonUtils;
import org.junit.jupiter.api.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;

import static com.steammachine.commonutils.orderedproperties.OrderedProperties.ItemType.COMMENT;
import static com.steammachine.commonutils.orderedproperties.StringPairParam.comment;
import static com.steammachine.commonutils.orderedproperties.StringPairParam.normal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderedPropertiesCheck {

    @Test
    void testNameIntegrity() {
        assertEquals("com.steammachine.commonutils.orderedproperties.OrderedProperties",
                OrderedProperties.class.getName());
    }

    @Tag("DebugRun")
    @Test
    void run10() throws IOException {
        Properties properties = new Properties();
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties.properties")) {
            properties.load(stream);
        }
        properties.getProperty("");

    }

    @Tag("DebugRun")
    @Test
    void run20() throws IOException {

        OrderedProperties properties = new OrderedProperties();
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties.properties")) {
            properties.load(stream);
        }

        String fileToWrite = CommonUtils.getAbsoluteResourcePath(getClass(), "res/" + new SimpleDateFormat("hh_mm_ss").format(new Date()) + ".properties");
        try (OutputStream stream = new FileOutputStream(fileToWrite)) {
            properties.store(stream);
            stream.flush();
        }

        properties.getProperty("");
    }

    /* ---------------------------------------------------- toProperties ----------------------------------------------- */

    @Test
    void toProperties10() throws IOException {
        Properties properties;
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties.properties")) {
            OrderedProperties p = new OrderedProperties();
            p.load(stream);
            properties = p.toProperties();
        }

        Properties properties2 = new Properties();
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties.properties")) {
            properties2.load(stream);
        }

        Assertions.assertEquals(properties, properties2);
    }

    /* ------------------------------------------------ setPoperty ---------------------------------------------------*/

    @Test
    void setPoperty10() throws IOException {
        OrderedProperties properties = new OrderedProperties();
        properties.setPoperty("s", "2");
        Assertions.assertEquals("2", properties.getProperty("s"));
        properties.setPoperty("s", "3");
        Assertions.assertEquals("3", properties.getProperty("s"));
        properties.setPoperty("s", null);
        Assertions.assertEquals(null, properties.getProperty("s"));
    }

    /* ---------------------------------------------------- parseValue ----------------------------------------------- */

    @Test
    void parseValueEmptyString10() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.NOT_FILLED, "", "", ""),
                OrderedProperties.parseValue(""));
    }

    @Test
    void parseValueEmptyString20() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.NOT_FILLED, "     ", "", ""),
                OrderedProperties.parseValue("     "));
    }

    @Test
    void parseValueComment10() {
        assertEquals(new OrderedProperties.Item(COMMENT, "# a comment", "", ""),
                OrderedProperties.parseValue("# a comment"));
    }

    @Test
    void parseValueComment20() {
        assertEquals(new OrderedProperties.Item(COMMENT, "     # a comment", "", ""),
                OrderedProperties.parseValue("     # a comment"));
    }

    @Test
    void parseValueEqualSign0() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s=%s", "prop6", "prop6"),
                OrderedProperties.parseValue("prop6=prop6"));
    }

    @Test
    void parseValueEqualSign10() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s= %s", "prop6", "prop6"),
                OrderedProperties.parseValue("prop6= prop6"));
    }

    @Test
    void parseValueEqualSign20() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s=       %s", "prop6", "prop6"),
                OrderedProperties.parseValue("prop6=       prop6"));
    }

    @Test
    void parseValueEqualSign30() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s =       %s", "prop6", "prop6"),
                OrderedProperties.parseValue("prop6 =       prop6"));
    }

    @Test
    void parseValueEqualSign40() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, " %s   =       %s", "prop6", "value6"),
                OrderedProperties.parseValue(" prop6   =       value6"));
    }

    @Test
    void parseValueNoEqualSign10() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, " %s %s", "s", "# prop3"),
                OrderedProperties.parseValue(" s # prop3"));
    }

    @Test
    void parseValueNoEqualSign20() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, " %s %s", "s", "# prop3=prop3"),
                OrderedProperties.parseValue(" s # prop3=prop3"));
    }

    @Test
    void parseValueNoEqualSign30() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s=%s", "11", "12   "),
                OrderedProperties.parseValue("11=12   "));
    }

    @Test
    void parseValueNoEqualSign40() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, " %s          %s", "ss", "# prop35=prop35"),
                OrderedProperties.parseValue(" ss          # prop35=prop35"));
    }

    @Test
    void parseValueOnlyKey10() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s%s", "key", ""),
                OrderedProperties.parseValue("key"));
    }

    @Test
    void parseValueOnlyKey1s() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s        %s", "pro3", "p12      =   pro  p12      "),
                OrderedProperties.parseValue("pro3        p12      =   pro  p12      "));
    }


    /* ------------------------------------------------- getLine -------------------------------------------------- */

    @Test
    void getLine10() {
        assertEquals("     # a comment",
                OrderedProperties.getLine(new OrderedProperties.Item(COMMENT, "     # a comment", "", "")));
    }

    @Test
    void getLine20() {
        assertEquals("       ",
                OrderedProperties.getLine(new OrderedProperties.Item(OrderedProperties.ItemType.NOT_FILLED, "       ", "", "")));
    }

    @Test
    void getLine30() {
        assertEquals("prop6=       prop6",
                OrderedProperties.getLine(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s=       %s", "prop6", "prop6")));
    }

    @Test
    void getLine40() {
        assertEquals("prop6 =       prop6",
                OrderedProperties.getLine(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, "%s =       %s", "prop6", "prop6")));
    }


    /* ---------------------------------------------------- privates ----------------------------------------------- */

    private static void checkParse(StringPairParam param) {
        OrderedProperties.Item item;
        try {
            item = OrderedProperties.parseValue(param.getString());
        } catch (Throwable t) {
            if (param.getExpectedError() == null) {
                throw t;
            } else if (!param.getExpectedError().isAssignableFrom(t.getClass())) {
                throw t;
            } else {
                return;
            }
        }
        assertEquals(param.getExpectedItem(), item);
    }


}