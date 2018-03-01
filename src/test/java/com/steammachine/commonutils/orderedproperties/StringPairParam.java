package com.steammachine.commonutils.orderedproperties;

public class StringPairParam {

    private static final StringPairParam DISABLED = comment("");

    private final String name;
    private final boolean enabled;
    private final String string;
    private final Class<? extends Throwable> expectedError;
    private final OrderedProperties.Item expectedItem;

    private StringPairParam(
            boolean enabled,
            String name,
            String string,
            OrderedProperties.Item expectedItem,
            Class<? extends Throwable> error) {

        this.name = name;
        this.enabled = enabled;
        this.string = string;
        this.expectedItem = expectedItem;
        this.expectedError = error;
    }

    public static StringPairParam of(
            String name,
            String string,
            OrderedProperties.Item item,
            Class<? extends Throwable> error) {
        return new StringPairParam(true, name, string, item, error);
    }

    public static StringPairParam normal(String name, String string, OrderedProperties.Item item) {
        return of(name, string, item, null);
    }

    public static StringPairParam comment(String comment) {
        return DISABLED;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public String getString() {
        return string;
    }


    public OrderedProperties.Item getExpectedItem() {
        return expectedItem;
    }

    public Class<? extends Throwable> getExpectedError() {
        return expectedError;
    }

    public StringPairParam disable() {
        return DISABLED;
    }
}
