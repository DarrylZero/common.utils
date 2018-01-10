package com.steammachine.common.commonspliterators;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vladimir Bogodukhov
 */
public class Iterator2SpliteratorBridgeCheck {
    @Test
    public void testNameIntegrity() {
        Assert.assertEquals("com.steammachine.common.commonspliterators.Iterator2SpliteratorBridge",
                Iterator2SpliteratorBridge.class.getName());
    }
}