package com.steammachine.common.autoclocablewrapper;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Vladimir Bogodukhov
 */
public class CloseableWrapperCheck {

    @Test
    public void testName() {
        Assert.assertEquals("com.steammachine.common.autoclocablewrapper.CloseableWrapper",
                CloseableWrapper.class.getName());
    }

    private static class ToClose {
        private boolean closed;

        public boolean isClosed() {
            return closed;
        }
    }

    @Test
    public void test20() throws IOException {
        final ToClose toClose;
        try (CloseableWrapper<ToClose> closeable = CloseableWrapper.closeable(new ToClose(), tc -> tc.closed = true)) {
            toClose = closeable.nexus();
            Assert.assertFalse(toClose.isClosed());
        }
        Assert.assertTrue(toClose.isClosed());

    }


}