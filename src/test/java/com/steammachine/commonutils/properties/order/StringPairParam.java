package com.steammachine.commonutils.properties.order;

public class StringPairParam {

    private final String name;
    private final boolean enabled;
    private final String string;
    private final String expectedKey;
    private final String expectedValue;
    private final Class<? extends Throwable> expectedError;

    private StringPairParam(boolean enabled, String name, String string, String expectedKey, String expectedValue, Class<? extends Throwable> error) {
        this.name = name;
        this.enabled = enabled;
        this.string = string;
        this.expectedKey = expectedKey;
        this.expectedValue = expectedValue;
        this.expectedError = error;
    }

    public static StringPairParam of(String name, String string, String expectedKey, String expectedValue, Class<? extends Throwable> error) {
        return new StringPairParam(true, name, string, expectedKey, expectedValue, error);
    }

    public static StringPairParam comment(String comment) {
        return new StringPairParam(false, null, null, null, null, null);
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

    public String getExpectedKey() {
        return expectedKey;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public Class<? extends Throwable> getExpectedError() {
        return expectedError;
    }
}
