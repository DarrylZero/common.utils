package com.steammachine.common.lazyeval;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vladimir Bogodukhov .
 */
public class LazyEvalCheck {

    @Test
    public void testName() {
        Assert.assertEquals("com.steammachine.common.lazyeval.LazyEval", LazyEval.class.getName());
    }

    @Test
    public void testName2() {
        int[] i = {0};
        LazyEval<String> eval = LazyEval.eval(() -> {
            i[0]++;
            return "111";
        });
        Assert.assertEquals("111", eval.value());
    }

    @Test
    public void testName20() {
        int[] i = {0};
        LazyEval<String> eval = LazyEval.eval(() -> {
            i[0]++;
            return "111";
        });
        Assert.assertEquals(0, i[0]);
        eval.value();
        Assert.assertEquals(1, i[0]);
        eval.value();
        Assert.assertEquals(1, i[0]);
        eval.value();
        Assert.assertEquals(1, i[0]);
        eval.value();
        Assert.assertEquals(1, i[0]);
    }



}