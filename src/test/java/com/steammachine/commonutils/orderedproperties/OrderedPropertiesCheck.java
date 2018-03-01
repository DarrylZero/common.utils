package com.steammachine.commonutils.orderedproperties;

import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.common.utils.commonutils.CommonUtils;
import org.junit.jupiter.api.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Stream;

import static com.steammachine.commonutils.orderedproperties.OrderedProperties.ItemType.COMMENT;
import static com.steammachine.commonutils.orderedproperties.StringPairParam.comment;
import static com.steammachine.commonutils.orderedproperties.StringPairParam.normal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderedPropertiesCheck {

    @Test
    void testNameIntegrity() {
        assertEquals("com.steammachine.commonutils.properties.order.OrderedProperties",
                OrderedProperties.class.getName());
    }

    @Tag("DebugRun")
    @Test
    void run10() throws IOException {


        Properties properties = new Properties();
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties.properties")) {
            properties.load(stream);
        }
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties2.properties")) {
            properties.load(stream);
        }

        try (FileOutputStream stream = new FileOutputStream(CommonUtils.getAbsoluteResourcePath(getClass(), "res/resource_properties.properties"))) {
            properties.store(stream, null);
        }

    }

    @Tag("DebugRun")
    @Test
    void run20() throws IOException {

        OrderedProperties properties = new OrderedProperties();
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties.properties")) {
            properties.load(stream);
        }
        try (InputStream stream = ResourceUtils.loadResourceByRelativePath(getClass(), "res/resource_properties2.properties")) {
            properties.load(stream);
        }

        try (FileOutputStream stream = new FileOutputStream(CommonUtils.getAbsoluteResourcePath(getClass(), "res/resource_properties.properties"))) {
            properties.store(stream, null);
        }

    }

    @Disabled
    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> parseValue10() throws IOException {
        return Stream.of(
                comment("normal execution"),
                comment("empty strings"),
                normal("empty element1", "", new OrderedProperties.Item(COMMENT, "", null, null)),
                comment("commented values"),
                normal("Comment10", "# a comment", new OrderedProperties.Item(COMMENT, "# a comment", null, null)).disable(),
                normal("Comment20", "   # a comment", new OrderedProperties.Item(COMMENT, "   # a comment", null, null)).disable(),
                comment("commented values"),
                normal("properties10", "prop6=       prop6", new OrderedProperties.Item(COMMENT, "%s=       %s", "prop6", "prop6")),
                comment("")
        ).filter(StringPairParam::isEnabled).
                map(param -> DynamicTest.dynamicTest(param.getName(), () -> checkParse(param)));
    }

    /* ---------------------------------------------------- parseValue ----------------------------------------------- */

    @Test
    void parseValueEmptyString10() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.NOT_FILLED, "", null, null),
                OrderedProperties.parseValue(""));
    }

    @Test
    void parseValueEmptyString20() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.NOT_FILLED, "     ", null, null),
                OrderedProperties.parseValue("     "));
    }

    @Test
    void parseValueComment10() {
        assertEquals(new OrderedProperties.Item(COMMENT, "# a comment", null, null),
                OrderedProperties.parseValue("# a comment"));
    }

    @Test
    void parseValueComment20() {
        assertEquals(new OrderedProperties.Item(COMMENT, "     # a comment", null, null),
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
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, " %s   =       %s", "s", "# prop3"),
                OrderedProperties.parseValue(" s # prop3"));
    }

    @Test
    void parseValueNoEqualSign20() {
        assertEquals(new OrderedProperties.Item(OrderedProperties.ItemType.VALUE, " %s   =       %s", "s", "# prop3=prop3"),
                OrderedProperties.parseValue(" s # prop3=prop3"));
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