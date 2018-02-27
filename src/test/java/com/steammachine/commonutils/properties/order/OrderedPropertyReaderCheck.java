package com.steammachine.commonutils.properties.order;

import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.common.utils.commonutils.CommonUtils;
import org.junit.jupiter.api.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Stream;

import static com.steammachine.commonutils.properties.order.OrderedProperties.ItemType.COMMENT;
import static com.steammachine.commonutils.properties.order.StringPairParam.comment;
import static com.steammachine.commonutils.properties.order.StringPairParam.normal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderedPropertyReaderCheck {

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

        properties.getProperty("")
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

    @TestFactory
    @Tag("DebugRun")
    Stream<DynamicTest> parseValue10() throws IOException {

        return Stream.of(
                comment("normal execution"),
                comment("commented values"),
                normal("Comment10", "# a comment", new OrderedProperties.Item(COMMENT, "# a comment", null, null)).disable(),
                normal("Comment20", "   # a comment", new OrderedProperties.Item(COMMENT, "   # a comment", null, null)).disable(),
                comment("commented values"),
                normal("properties10", "prop6=       prop6", new OrderedProperties.Item(COMMENT, "%s=       %s", "prop6", "prop6")),

                comment("")


        ).filter(StringPairParam::isEnabled).
                map(param -> DynamicTest.dynamicTest(param.getName(), () -> checkParse(param)));

    }

    private void checkParse(StringPairParam param) {
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
        Assertions.assertEquals(param.getExpectedItem(), item);
    }


}