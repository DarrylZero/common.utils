package com.steammachine.methodfinder;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import com.steammachine.common.lazyeval.LazyEval;
import com.steammachine.common.utils.commonutils.CommonUtils;
import com.steammachine.common.utils.metodsutils.MethodCaller;
import com.steammachine.common.utils.metodsutils.MethodUtils;
import com.steammachine.methodfinder.ClassMethodTable.SourceCodePosition;
import com.steammachine.methodfinder.MethodFinder.DefaultSignatures;
import com.steammachine.methodfinder.MethodFinder.Signatures;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static com.steammachine.common.utils.metodsutils.MethodUtils.findMethod;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 **/
public class MethodFinderCheck {

    private static final LazyEval<MethodCaller> getMethod = LazyEval.eval(() ->
            new MethodUtils.BaseMethodCaller(findMethod(MethodUtils.Level.PRIVATE, MethodFinder.class,
                    "getMethod", Method.class, Class.class, String.class, LazyEval.class, int.class)));

    private static final LazyEval<Signatures> CACHE = LazyEval.eval(DefaultSignatures::new);


    @Test
    public void nameMethodFinder() {
        Assert.assertEquals("com.steammachine.methodfinder.MethodFinder", MethodFinder.class.getName());
    }

    @Test(expected = NullPointerException.class)
    public void getMethod10() {
        getMethod(null, null, null, 1);
    }


    @Test(expected = NullPointerException.class)
    public void getMethod20() {
        getMethod(MethodFinderCheck.class, null, null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void getMethod30() {
        getMethod(null, "getMethod", null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void getMethod33() {
        getMethod(null, "getMethod", LazyEval.eval(DefaultSignatures::new), 1);
    }

    @Test
    public void getMethod40() {
        getMethod(MethodFinderCheck.class, "getMethod", LazyEval.eval(DefaultSignatures::new), 1);
    }


    @Test
    public void getMethod50() {
        Method expected = findMethod(MethodUtils.Level.PRIVATE, ClazzToFindMethods.class, "methodToFind", null);
        Method methodToFind = getMethod(ClazzToFindMethods.class, "methodToFind", CACHE, 17);
        Assert.assertEquals(expected, methodToFind);
    }

    @Test
    public void getMethod60() {
        Assert.assertEquals(
                findMethod(MethodUtils.Level.PRIVATE, ClazzToFindMethods.class, "methodToFind", null,
                        int.class),
                getMethod(ClazzToFindMethods.class, "methodToFind", CACHE, 21)
        );
    }

    @Test
    public void getMethod70() {
        Assert.assertEquals(
                findMethod(MethodUtils.Level.PRIVATE, ClazzToFindMethods.class, "methodToFind", null,
                        int.class, int.class),
                getMethod(ClazzToFindMethods.class, "methodToFind", CACHE, 25)
        );
    }


    @Test
    public void getMethod80() {
        Assert.assertEquals(
                findMethod(MethodUtils.Level.PRIVATE, ClazzToFindMethods.class, "methodToFind", null,
                        int.class, int.class, int.class),
                getMethod(ClazzToFindMethods.class, "methodToFind", CACHE, 32)
        );
    }

    private static Method methodAtPosition(int linePosition) {
        return getMethod(ClazzToFindMethods.class, "methodToFind", CACHE, linePosition);
    }


    @Test
    public void getMethod81() {
        Map<String, SourceCodePosition> map = LazyEval.eval(DefaultSignatures::new).value().get(ClazzToFindMethods.class);
        Assert.assertEquals(
                new HashSet<>(Arrays.asList(
                        "void <init>()",
                        "void methodToFind()",
                        "void methodToFind(int)",
                        "void methodToFind(int,int)",
                        "void methodToFind(int,int,int)",
                        "void methodToFind(int,int,int,int)",
                        "void nop()"
                )),
                map.keySet());
    }


    public static class InternalTestedClass {

        private void test() {
        }

        private void test2() {
            test();
        }

        private void test3() {
            test2();
        }

        private void test4() {
            test3();
        }

    }

    @Test
    public void getMethod82() {
        Map<String, SourceCodePosition> map = LazyEval.eval(DefaultSignatures::new).value().get(InternalTestedClass.class);
        Assert.assertEquals(
                new HashSet<>(Arrays.asList(
                        "void <init>()",
                        "void test3()",
                        "void test4()",
                        "void test()",
                        "void test2()"

                )),
                map.keySet());
    }

    @Test
    public void getMethod84() {
        Method expected = findMethod(MethodUtils.Level.PRIVATE, ClazzToFindMethods.class, "methodToFind",
                null,
                int.class, int.class, int.class);
        Assert.assertEquals(expected, methodAtPosition(32));
    }

    @Test
    public void getMethod85() {
        Method expected = findMethod(MethodUtils.Level.PRIVATE, ClazzToFindMethods.class, "methodToFind",
                null,
                int.class, int.class, int.class);

        Assert.assertEquals(null, methodAtPosition(29));
        Assert.assertEquals(expected, methodAtPosition(30));
        Assert.assertEquals(expected, methodAtPosition(31));
        Assert.assertEquals(expected, methodAtPosition(32));
        Assert.assertEquals(expected, methodAtPosition(33));
        Assert.assertEquals(expected, methodAtPosition(34));
        Assert.assertEquals(expected, methodAtPosition(35));
        Assert.assertEquals(expected, methodAtPosition(36));
        Assert.assertEquals(expected, methodAtPosition(37));
        Assert.assertEquals(expected, methodAtPosition(38));
        Assert.assertEquals(expected, methodAtPosition(39));
        Assert.assertEquals(expected, methodAtPosition(40));
        Assert.assertEquals(expected, methodAtPosition(41));
        Assert.assertEquals(expected, methodAtPosition(42));
        Assert.assertEquals(expected, methodAtPosition(43));
        Assert.assertEquals(expected, methodAtPosition(44));
        Assert.assertEquals(expected, methodAtPosition(44));
        Assert.assertEquals(expected, methodAtPosition(45));
        Assert.assertEquals(expected, methodAtPosition(46));
        Assert.assertEquals(expected, methodAtPosition(47));
        Assert.assertEquals(expected, methodAtPosition(48));
        Assert.assertEquals(expected, methodAtPosition(49));
        Assert.assertEquals(expected, methodAtPosition(50));
        Assert.assertEquals(expected, methodAtPosition(51));
        Assert.assertEquals(expected, methodAtPosition(52));
        Assert.assertEquals(expected, methodAtPosition(53));
        Assert.assertEquals(expected, methodAtPosition(54));
        Assert.assertEquals(expected, methodAtPosition(55));
        Assert.assertEquals(expected, methodAtPosition(56));
        Assert.assertEquals(expected, methodAtPosition(57));
        Assert.assertEquals(null, methodAtPosition(59));
        Assert.assertEquals(null, methodAtPosition(60));
        Assert.assertEquals(null, methodAtPosition(61));
    }

    @Test
    public void getMethod1000_speed_test() throws Exception {
        long measureTime = CommonUtils.measureTime(() -> {
            getMethod(ClazzToFindMethods.class, "methodToFind", CACHE, 34);
        }, 1_000);
        Assert.assertTrue(measureTime < 2000);
    }

    private static Method getMethod(Class clazz, String methodName, LazyEval<Signatures> map, int position) {
        return getMethod.value().invoke(null, clazz, methodName, map, position);
    }
}
