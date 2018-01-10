package com.steammachine.common.lazyeval;

import org.junit.Assert;
import org.junit.Test;
import com.steammachine.common.utils.commonutils.CommonUtils;

import static com.steammachine.common.lazyeval.LazyBuider.lazy;

/**
 *
 * @author Vladimir Bogoduhov
 */
public class LazyBuiderCheck {


    @Test
    public void testName() {
        Assert.assertEquals("com.steammachine.common.lazyeval.LazyBuider", LazyBuider.class.getName());
    }

    @Test
    public void testPerformance10() throws Exception {
        long measureTime = CommonUtils.measureTime(() -> calc("" + System.nanoTime(), true), 10_000_000);
        Assert.assertTrue(measureTime < 20000);
    }

    @Test
    public void testPerformance20() throws Exception {
        long measureTime = CommonUtils.measureTime(() -> calc("" + System.nanoTime(), false), 10_000_000);
        Assert.assertTrue(measureTime < 17000);
    }

    @Test
    public void testPerformance30() throws Exception {
        long measureTime = CommonUtils.measureTime(() -> calc(lazy(() -> "" + System.nanoTime()), false), 10_000_000);
        Assert.assertTrue(measureTime < 2000);
    }

    @Test
    public void testPerformance40() throws Exception {
        Object lazyToString = lazy(() -> "" + System.nanoTime());
        long measureTime = CommonUtils.measureTime(() -> calc(lazyToString, false), 10_000_000);
        Assert.assertTrue(measureTime < 1000);
    }

    @Test
    public void testPerformance50() throws Exception {
        Object lazyToString = lazy(() -> "" + System.nanoTime());
        long measureTime = CommonUtils.measureTime(() -> calc(lazyToString, true), 10_000_000);
        Assert.assertTrue(measureTime < 23000);
    }


    @Test
    public void testPerformance60() throws Exception {
        long measureTime = CommonUtils.measureTime(() -> calc("" + valueWithDelay(), false), 100);
        Assert.assertTrue(measureTime < 2500);
    }

    @Test
    public void testPerformance70() throws Exception {
        long measureTime = CommonUtils.measureTime(() -> calc("" + valueWithDelay(), true), 100);
        Assert.assertTrue(measureTime < 2500);
    }


    @Test
    public void testPerformance80() throws Exception {
        Object lazyToString = lazy(() -> "" + valueWithDelay());
        long measureTime = CommonUtils.measureTime(() -> calc(lazyToString, false), 100);
        Assert.assertTrue(measureTime < 100);
    }

    @Test
    public void testPerformance90() throws Exception {
        Object lazyToString = lazy(() -> "" + valueWithDelay());
        long measureTime = CommonUtils.measureTime(() -> calc(lazyToString, true), 100);
        Assert.assertTrue(measureTime < 2500);
    }


    @Test
    public void testVal10() {
        Assert.assertEquals("111", "" + lazy(() -> "" + 111));
    }

    /* -------------------------------------------------- privates ------------------------------------------------- */

    private static void calc(Object o, boolean flag) {
        if (flag) {
            routine("" + o);
        }
    }

    private static void routine(String s) {
    }

    private static long valueWithDelay() {
        CommonUtils.delay(2);
        return System.nanoTime();
    }


}