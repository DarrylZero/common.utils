package com.steammachine.methodfinder;

import org.junit.Assert;
import org.junit.Test;
import com.steammachine.common.utils.commonutils.CommonUtils;
import com.steammachine.common.utils.metodsutils.MethodUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 **/
public class MethodStackCheck {

    private static final Predicate<Class> NOT_SYSTEM_CLASS =
            clazz -> !"java.lang".equals(clazz.getPackage().getName());

    @Test(expected = NullPointerException.class)
    public void findMethod10() {
        MethodFinder.call(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void findMethod20() {
        MethodFinder.call(NOT_SYSTEM_CLASS, null);
    }


    @Test(expected = NullPointerException.class)
    public void findMethod30() {
        MethodFinder.call(null, method -> false);
    }


    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TagI {
    }

    public static class TestedClass {
        private final Consumer<Object> consumer;

        public TestedClass(Consumer<Object> consumer) {
            this.consumer = consumer;
        }

        @TagI
        private void test() {
            consumer.accept(null);
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
    public void findMethod40() {
        new TestedClass(o -> {
            Method method = MethodFinder.call(NOT_SYSTEM_CLASS, m -> m.getDeclaredAnnotation(TagI.class) != null);
            Method expected = MethodUtils.findMethod(MethodUtils.Level.PRIVATE, TestedClass.class, "test",
                    null);
            Assert.assertEquals(expected, method);
        }).test4();
    }

    @Test
    public void findMethod_speed_test_10() throws Exception {
        Method expected = MethodUtils.findMethod(MethodUtils.Level.PRIVATE, TestedClass.class, "test", null);
        TestedClass testedClass =
                new TestedClass(o -> {
                    Method method = MethodFinder.call(NOT_SYSTEM_CLASS, m -> m.getDeclaredAnnotation(TagI.class) != null);
                    Assert.assertEquals(expected, method);
                });

        long measureTime = CommonUtils.measureTime(testedClass::test4, 1_000);
        Assert.assertTrue(measureTime <= 15000);
    }


    public static class TestedClass2 {
        private final Consumer<Object> consumer;

        public TestedClass2(Consumer<Object> consumer) {
            this.consumer = consumer;
        }

        @TagI
        private  void test() {
            consumer.accept(null);
        }

        private  void test2() {
            test();
        }

        private  void test3() {
            test2();
        }

        private  void test4() {
            test3();
        }

        private void test5() {
            test4();
        }

        private  void test6() {
            test5();
        }

        private  void test7() {
            test6();
        }

        private  void test8() {
            test7();
        }

        private void test9() {
            test8();
        }

        private void test10() {
            test9();
        }

        private void test11() {
            test10();
        }

        private void test12() {
            test11();
        }

        private void test13() {
            test12();
        }

        private void test14() {
            test13();
        }

        private void test15() {
            test14();
        }

        private void test16() {
            test15();
        }

        private void test17() {
            test16();
        }

        private void test18() {
            test17();
        }

        private void test19() {
            test18();
        }

        private void test20() {
            test19();
        }

        private void test21() {
            test20();
        }

        private void test22() {
            test21();
        }

        private void test23() {
            test22();
        }

        private void test24() {
            test23();
        }

        private void test25() {
            test24();
        }

        private void test26() {
            test25();
        }

        public void execute() {
            test26();
        }
    }


    @Test
    public void findMethod_speed_test_30() throws Exception {
        Method expected = MethodUtils.findMethod(MethodUtils.Level.PRIVATE, TestedClass2.class, "test", null);
        TestedClass2 testedClass =
                new TestedClass2(o -> {
                    Method method = MethodFinder.call(NOT_SYSTEM_CLASS, m -> m.getDeclaredAnnotation(TagI.class) != null);
                    Assert.assertEquals(expected, method);
                });

        long measureTime = CommonUtils.measureTime(testedClass::execute, 1_000);
        Assert.assertTrue(measureTime <= 15000);
    }


    /***
     *
     * проверка выполнения на методе с очень большой глубиной стека
     *
     * @throws Exception
     */
    @Test
    public void findMethod_speed_test_40() throws Exception {
        Method expected = MethodUtils.findMethod(MethodUtils.Level.PRIVATE, TestedClassRun.class, "test", null);
        TestedClass3 testedClass =
                new TestedClass3(o -> {
                    Method method = MethodFinder.call(NOT_SYSTEM_CLASS, m -> m.getDeclaredAnnotation(TagI.class) != null);
                    Assert.assertEquals(expected, method);
                });
        long measureTime = CommonUtils.measureTime(testedClass::execute, 10);
        Assert.assertTrue(measureTime <= 60000);
    }


}
