package com.steammachine.common.definitions.annotations;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vladimir Bogodukhov
 **/
public class SignatureSensitiveCheck {


    @Test
    public void testName() {
        Assert.assertEquals(
                "com.steammachine.common.definitions.annotations.SignatureSensitive",
                SignatureSensitive.class.getName()
        );
    }
}
