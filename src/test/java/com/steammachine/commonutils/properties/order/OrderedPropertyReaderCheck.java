package com.steammachine.commonutils.properties.order;

import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.common.utils.commonutils.CommonUtils;
import com.steammachine.org.junit5.extensions.dynamictests.DynamicTestsFactory;
import com.steammachine.org.junit5.extensions.dynamictests.TestInstanceFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.steammachine.commonutils.properties.order.StringPairParam.*;
import static com.steammachine.commonutils.properties.order.StringPairParam.comment;
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
    Stream<DynamicTest> parseValue10() throws IOException {

        return Stream.of(
                comment(""),
                of("", "", "", "", null)


                ).filter(StringPairParam::isEnabled).
                map(param -> DynamicTest.dynamicTest(param.getName(), () -> checkParse(param)));

    }

    private void checkParse(StringPairParam param) {
        try {
            OrderedProperties.parseValue(param.getString());
        } catch(Throwable t) {
            if (param.getExpectedError() == null) {
                throw t;
            } else if (!param.getExpectedError().isAssignableFrom(t.getClass())) {
                throw t;
            }
        }
    }


}