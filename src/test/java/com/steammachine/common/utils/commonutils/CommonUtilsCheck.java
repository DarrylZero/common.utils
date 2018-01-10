package com.steammachine.common.utils.commonutils;


import org.junit.Assert;
import org.junit.Test;
import com.steammachine.common.utils.ResourceUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.steammachine.common.utils.commonutils.CommonUtils.excludeFirstBackslash;
import static com.steammachine.common.utils.commonutils.CommonUtils.excludeTrailingBackslash;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */
@SuppressWarnings("EqualsWithItself")
public class CommonUtilsCheck {


    private static class StringWrapper {
        public final String value;

        StringWrapper(String value) {
            this.value = value;
        }
    }

    private static final Comparator<String> COMPARE_STRINGS = String::compareTo;
    private static final Comparator<Integer> COMPARE_INTEGERS = (i1, i2) -> {
        if (i1 == null && i2 == null) {
            return 0;
        } else if (i1 != null && i2 == null) {
            return 1;
        } else if (i1 == null) {
            return -1;
        } else {
            return i1.compareTo(i2);
        }
    };


    private static final Charset CHARSET = Charset.forName("utf-8");
    private static final byte[] BYTES = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
    private static final ByteArrayInputStream STREAM_REF = new ByteArrayInputStream(BYTES);

    @SuppressWarnings("SimplifiableIfStatement")
    private static final BiFunction<String, StringWrapper, Boolean> STRING_STRING_WRAPPER_COMPARATOR =
            (s, stringWrapper) -> {
                if (s == null && stringWrapper == null) {
                    return true;
                } else if ((s == null) != (stringWrapper == null)) {
                    return false;
                } else {
                    return s.equals(stringWrapper.value);
                }
            };


    private static final Predicate<String> TESTES_PREDICATE =
            Pattern.compile("^(array" + Pattern.quote("[") + "([0-9]+)" + Pattern.quote("]") + ")$",
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).
                    asPredicate();

    private static final Function<String, Integer> TESTES_ORDER_FUNCTION = s -> {
        if (!TESTES_PREDICATE.test(s)) {
            return null;
        }
        int start = s.indexOf('[');
        int end = s.lastIndexOf(']');

        try {
            return Integer.parseInt(s.substring(start + 1, end));
        } catch (NumberFormatException e) {
            return null;
        }
    };

    @SuppressWarnings("SimplifiableIfStatement")
    private static final BiFunction<T1, T2, Boolean> T1_T2_OBJECT_COMPARATOR =
            (t1, t2) -> {
                if (t1 == null && t2 == null) {
                    return true;
                } else if ((t1 == null) != (t2 == null)) {
                    return false;
                } else if ((t1.i != t2.i)) {
                    return false;
                } else {
                    return t1.s == null ? t2.s == null : t1.s.equals(t2.s);
                }
            };
    private static final Function<String, String> TESTES_ORDER_FUNCTION_2 = s -> {
        char[] chars = s.toCharArray();
        int i = 0;
        for (char c : chars) {
            if (c == '[') {
                i++;
            }
        }
        if (i == 2) {
            int start = s.indexOf('[');
            if (start == -1) {
                return null;
            }
            int end = s.indexOf(']', start);
            if (end == -1) {
                return null;
            }
            try {
                return s.substring(start + 1, end);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    };

    private static boolean testes_all_ThreadsAreDone(Thread... threads) {
        if (threads == null) {
            throw new NullPointerException("threads is null");
        }
        for (Thread thread : threads) {
            if (thread == null) {
                throw new NullPointerException("thread is null");
            }
        }
        for (Thread thread : threads) {
            if (thread.isAlive()) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void expandListToSize40() {
        List<String> list = CommonUtils.expandListToSize(new ArrayList<>(), 1);
        list.set(0, "Adelante con faroles");
        Assert.assertEquals(
                Collections.singletonList("Adelante con faroles"),
                list
        );
    }

    @Test(expected = NullPointerException.class)
    public void getValueFunction10() {
        CommonUtils.getValueFunction(null, null);
    }


    @Test(expected = NullPointerException.class)
    public void getValueFunction20() {
        CommonUtils.getValueFunction("15", null);
    }

    /* --------------------------------------------------------------------------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void testNormalizeString() {
        CommonUtils.excludeTrailingBackslash(null);
    }


    @Test
    public void testNormalizeString2() {
        CommonUtils.excludeTrailingBackslash("http://one/two/three");
    }


    @Test
    public void testNormalizeString3() {
        Assert.assertEquals(
                CommonUtils.excludeTrailingBackslash("http://one/two/three"),
                "http://one/two/three"
        );
    }


    @Test
    public void testNormalizeString4() {
        Assert.assertEquals(
                CommonUtils.excludeTrailingBackslash("http://one/two/three/"),
                "http://one/two/three"
        );
    }


    @Test
    public void testNormalizeString5() {
        Assert.assertEquals(
                CommonUtils.excludeTrailingBackslash("/"),
                ""
        );
    }


    @Test
    public void testNormalizeString6() {
        Assert.assertEquals(
                CommonUtils.excludeTrailingBackslash(""),
                ""
        );
    }


    @Test
    public void testNormalizeString7() {
        Assert.assertEquals(
                CommonUtils.excludeTrailingBackslash(" "),
                " "
        );
    }


    /*  --------------------------------------  excludeFirstBackslash ---------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void excludeFirstBackslash10() {
        CommonUtils.excludeFirstBackslash(null);
    }

    @Test
    public void excludeFirstBackslash20() {
        Assert.assertEquals(
                CommonUtils.excludeFirstBackslash(" "),
                " "
        );
    }

    @Test
    public void excludeFirstBackslash30() {
        Assert.assertEquals(
                CommonUtils.excludeFirstBackslash("TTTE"),
                "TTTE"
        );
    }

    @Test
    public void excludeFirstBackslash40() {
        Assert.assertEquals(
                "TTTE",
                CommonUtils.excludeFirstBackslash("/TTTE")
        );
    }

    @Test
    public void excludeFirstBackslash50() {
        Assert.assertEquals(
                "TTTE/",
                CommonUtils.excludeFirstBackslash("/TTTE/")
        );
    }

    /*  --------------------------------------  allThreadsAreDone ----------------------------------------------- */

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void allThreadsAreDone_error() {
        Thread[] threads = null;
        Assert.assertEquals(true, CommonUtils.allThreadsAreDone(threads));
    }

    @Test(expected = NullPointerException.class)
    public void allThreadsAreDone_error2() {
        Thread[] threads = new Thread[]{null};
        Assert.assertTrue(CommonUtils.allThreadsAreDone(threads));
    }

    @Test
    public void allThreadsAreDone1() {
        Assert.assertTrue(CommonUtils.allThreadsAreDone());
    }

    @Test
    public void allThreadsAreDone2() throws InterruptedException {
        Thread thread = new Thread();
        thread.start();

        while (thread.isAlive()) {
            Thread.sleep(10L);
        }

        Assert.assertTrue(CommonUtils.allThreadsAreDone(thread));
    }

    @Test(timeout = 1000, expected = IllegalStateException.class)
    public void throwsExc2() throws Throwable {
        throw new IllegalStateException();
    }


    @Test(timeout = 1000)
    public void allThreadsAreDone3() throws Throwable {
        Thread[] threads = {new Thread(), new Thread()};
        for (Thread thread : threads) {
            thread.start();
        }

        while (!testes_all_ThreadsAreDone(threads)) {
            Thread.sleep(10L);
        }

        Assert.assertTrue(CommonUtils.allThreadsAreDone(threads));
    }

    @Test(timeout = 5000)
    public void allThreadsAreDone4() throws Throwable {
        Runnable target = () -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        };
        Thread[] threads = {new Thread(target), new Thread(target), new Thread(target), new Thread(target)};
        for (Thread thread : threads) {
            thread.start();
        }

        while (!testes_all_ThreadsAreDone(threads)) {
            Thread.sleep(10L);
        }
        Assert.assertTrue(CommonUtils.allThreadsAreDone(threads));
    }

    @Test
    public void getValueFunction30() {
        /* передаются лажовые данные - но в виду того что используется предикат,
        который всегда возвращает константу мы получаем константу */

        Assert.assertEquals(
                new Integer("100000"),
                CommonUtils.getValueFunction("lkdjfa;kdjf;a;dfadfj;adfj;a", s -> 100000)
        );
    }
    
    /*  ------------------------------------------ normalizeString -------------------------------------------------- */


    @Test(expected = NullPointerException.class)
    public void normalizeStringWrongParams() throws IOException {
        CommonUtils.normalizeString(null);
    }

    @Test
    public void normalizeString1() throws IOException {
        CommonUtils.normalizeString("");
    }


    @Test
    public void normalizeStringEmptyString10() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString(""),
                ""
        );
    }

    @Test
    public void normalizeStringSimpleString20() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString("A B C"),
                "A B C"
        );
    }

    @Test
    public void normalizeStringMoreSpaces30() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString("    A       B       C    "),
                "A B C"
        );
    }

    @Test
    public void normalizeStringWithReturns40() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString("    A   \r\n    B    \r\n   \r    C    "),
                "A B C"
        );
    }

    @Test
    public void normalizeStringWithReturns50() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_2.txt",
                        CHARSET)),
                "A V E DE Presa"
        );
    }

    @Test
    public void normalizeStringWithReturns60() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_3.txt",
                        CHARSET)),
                "A V E DE Presa"
        );
    }

    @Test
    public void normalizeStringWithReturns70() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_4.txt",
                        CHARSET)),
                "A V"
        );
    }


    @Test
    public void normalizeStringWithReturns80() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_5.txt",
                        CHARSET)),
                "A"
        );
    }

    @Test
    public void normalizeStringWithReturns90() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString("   1   2     3 4     5       6 7 \r\n  8"),
                "1 2 3 4 5 6 7 8"

        );
    }

    @Test
    public void normalizeStringWithReturns100() throws IOException {
        Assert.assertEquals(
                CommonUtils.normalizeString("Required:\n" + "1 600 population"),
                "Required: 1 600 population"
        );
    }

    /* -------------------------------------------- copyStream -------------------------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void copyStream_error1() throws IOException {
        CommonUtils.copyStreamContent(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void copyStream_error2() throws IOException {
        CommonUtils.copyStreamContent(null, new ByteArrayOutputStream());
    }

    @Test(expected = NullPointerException.class)
    public void copyStream_error3() throws IOException {
        CommonUtils.copyStreamContent(new ByteArrayInputStream(new byte[]{1, 2, 3}), null);
    }

    @Test
    public void copyStream_NormalExecution() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommonUtils.copyStreamContent(new ByteArrayInputStream(new byte[]{1, 2, 3}), outputStream);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, outputStream.toByteArray());
    }


    /* -------------------------------------------- compareStreams --------------------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void compareStreamsError1() throws IOException {
        CommonUtils.compareStreams(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void compareStreamsError2() throws IOException {
        CommonUtils.compareStreams(STREAM_REF, null);
    }

    @Test(expected = NullPointerException.class)
    public void compareStreamsError3() throws IOException {
        CommonUtils.compareStreams(null, STREAM_REF);
    }

    @Test
    public void compareStreams() throws IOException {
        try (InputStream stream = new ByteArrayInputStream(
                ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"))) {
            try (InputStream slowStream = new SlowInputStream(
                    ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"))) {
                Assert.assertEquals(CommonUtils.compareStreams(stream, slowStream), true);
            }
        }
    }

    @Test
    public void compareStreams2() throws IOException {
        try (InputStream stream = new ByteArrayInputStream(
                ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"))) {
            try (InputStream slowStream = new SlowInputStream(
                    ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_text_file_2.txt"))) {
                Assert.assertEquals(CommonUtils.compareStreams(stream, slowStream), false);
            }
        }
    }

    /* ------------------------------------------------------- objectsAreEqual ---------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void compareObjects10() {
        CommonUtils.objectsAreEqual(null, null, null);
    }

    @Test
    public void compareObjects20() {
        Assert.assertTrue(CommonUtils.objectsAreEqual(new Object(), "", (t, e) -> true));
    }

    @Test
    public void compareObjects30() {
        Assert.assertFalse(CommonUtils.objectsAreEqual(new Object(), "", (t, e) -> false));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution() {
        Assert.assertTrue(CommonUtils.objectsAreEqual(null, null, STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution2() {
        Assert.assertTrue(CommonUtils.objectsAreEqual("", new StringWrapper(""), STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution3() {
        Assert.assertTrue(CommonUtils.objectsAreEqual(null, null, STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution4() {
        Assert.assertTrue(CommonUtils.objectsAreEqual("Hasta la victoria. Siempre!",
                new StringWrapper("Hasta la victoria. Siempre!"), STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution5() {
        Assert.assertFalse(CommonUtils.objectsAreEqual(null,
                new StringWrapper("Hasta la victoria. Siempre!"), STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution6() {
        Assert.assertTrue(CommonUtils.objectsAreEqual(new T1(10, "Llorona de azul celeste"),
                new T2(10, "Llorona de azul celeste"),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution7() {
        Assert.assertFalse(CommonUtils.objectsAreEqual(new T1(11, "Llorona de azul celeste"),
                new T2(10, "Llorona de azul celeste"),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution8() {
        Assert.assertFalse(CommonUtils.objectsAreEqual(new T1(10, "Llorona de azul celeste"),
                new T2(10, "Llorona"),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution9() {
        Assert.assertTrue(CommonUtils.objectsAreEqual(new T1(10, null),
                new T2(10, null),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    public void diffTypesObjectsEqual_normalExecution10() {
        Assert.assertTrue(CommonUtils.objectsAreEqual(null, null, T1_T2_OBJECT_COMPARATOR));
    }

    /* ------------------------------------------ getAbsoluteResourcePath ------------------------------------------- */


    @Test(expected = NullPointerException.class)
    public void getAbsoluteResourcePath_error1() {
        CommonUtils.getAbsoluteResourcePath(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void getAbsoluteResourcePath_error2() {
        CommonUtils.getAbsoluteResourcePath(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void getAbsoluteResourcePath_error3() {
        CommonUtils.getAbsoluteResourcePath(CommonUtilsCheck.class, null);
    }

    @Test
    public void getAbsoluteResourcePath10() {
        Assert.assertEquals(
                getAbsoluteResourcePathEtalon(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"),
                CommonUtils.getAbsoluteResourcePath(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt")
        );
    }

    @Test
    public void getAbsoluteResourcePath20() throws IOException {
        Class<CommonUtilsCheck> clazz = CommonUtilsCheck.class;
        String expected = getAbsoluteResourcePathEtalon(clazz, "resources/sometext/resource_test_file_7.txt");
        Files.deleteIfExists(Paths.get(expected));
        Assert.assertEquals(false, Files.exists(Paths.get(expected))); /* проверяем что пока - файла нет */
        String absoluteResourcePath = CommonUtils.getAbsoluteResourcePath(clazz, "resources/sometext/resource_test_file_7.txt");
        Assert.assertEquals(
                expected,
                absoluteResourcePath
        );
    }

    @Test
    public void getAbsoluteResourcePath30() throws IOException {
        Class<CommonUtilsCheck> clazz = CommonUtilsCheck.class;
        String expected = getAbsoluteResourcePathEtalon(clazz, "resources/sometext/resource_test_file_7.txt");
        Files.deleteIfExists(Paths.get(expected));
        Assert.assertEquals(false, Files.exists(Paths.get(expected))); /* проверяем что пока - файла нет */
        String absoluteResourcePath = CommonUtils.getAbsoluteResourcePath(clazz, "/resources/sometext/resource_test_file_7.txt");
        Assert.assertEquals(
                expected,
                absoluteResourcePath
        );
    }

    /* ------------------------------------------ getResourceUri ---------------------------------------------------- */


    @Test(expected = NullPointerException.class)
    public void getResourceUri_error1() throws URISyntaxException {
        CommonUtils.getResourceUri(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void getResourceUri_error2() throws URISyntaxException {
        CommonUtils.getResourceUri(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void getResourceUri_error3() throws URISyntaxException {
        CommonUtils.getResourceUri(CommonUtilsCheck.class, null);
    }

    @Test
    public void getResourceUri_normal1() throws URISyntaxException {
        CommonUtils.getResourceUri(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt");
    }

    /* ----------------------------------------------- asArray --------------------------------------------- */

    @Test(expected = IllegalStateException.class)
    public void asArray_error1() {
        CommonUtils.asArray(1);
    }

    @Test(expected = IllegalStateException.class)
    public void asArray_error2() {
        CommonUtils.asArray(2, 0);
    }

    @Test
    public void asArray_0() {
        Assert.assertArrayEquals(new int[]{}, CommonUtils.asArray(0));
    }

    @Test
    public void asArray_1() {
        Assert.assertArrayEquals(new int[]{1}, CommonUtils.asArray(1, 1));
    }

    @Test
    public void asArray_2() {
        Assert.assertArrayEquals(new int[]{1, 2, 3}, CommonUtils.asArray(3, 1, 2, 3));
    }

    @Test
    public void asArray_3() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        Assert.assertArrayEquals(new int[]{i1, i2, i3}, CommonUtils.asArray(3, i1, i2, i3));
    }

    @Test
    public void asArray_4() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        int[] expecteds = {i1, i2, i3};
        int[] actuals = CommonUtils.asArray(3, i1, i2, i3);
        Assert.assertArrayEquals(expecteds, actuals);
        Assert.assertNotSame(expecteds, actuals);
    }

    /* ----------------------------------------------- checkToArray ---------------------------------------------------- */

    @Test
    public void checkToArray10() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        Integer[] expecteds = {i1, i2, i3};
        Integer[] actuals = CommonUtils.checkToArray(3, i1, i2, i3);
        Assert.assertArrayEquals(expecteds, actuals);
        Assert.assertNotSame(expecteds, actuals);
    }

    @Test
    public void checkToArray20() {
        Random random = new Random();

        long i1 = random.nextInt(1000);
        long i2 = random.nextInt(1000);
        long i3 = random.nextInt(1000);

        Long[] expecteds = {i1, i2, i3};
        Long[] actuals = CommonUtils.checkToArray(3, i1, i2, i3);
        Assert.assertArrayEquals(expecteds, actuals);
        Assert.assertNotSame(expecteds, actuals);
    }


    /* ----------------------------------------------- toArray ------------------------------------------------- */


    @Test
    public void toArray10() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        Integer[] expecteds = {i1, i2, i3};
        Integer[] actuals = CommonUtils.toArray(i1, i2, i3);
        Assert.assertArrayEquals(expecteds, actuals);
        Assert.assertNotSame(expecteds, actuals);
    }

    @Test
    public void toArray20() {
        Random random = new Random();

        long i1 = random.nextInt(1000);
        long i2 = random.nextInt(1000);
        long i3 = random.nextInt(1000);

        Long[] expecteds = {i1, i2, i3};
        Long[] actuals = CommonUtils.toArray(i1, i2, i3);
        Assert.assertArrayEquals(expecteds, actuals);
        Assert.assertNotSame(expecteds, actuals);
    }

    /* ------------------------------------------------ removeSpacesAndReturns ------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void removeSpaces10() {
        CommonUtils.removeSpacesAndReturns(null);
    }

    @Test
    public void removeSpaces20() {
        Assert.assertEquals("1nnnnnn", CommonUtils.removeSpacesAndReturns("1 nnn          nnn"));
    }

    @Test
    public void removeSpaces30() {
        Assert.assertEquals("1nnnnnn", CommonUtils.removeSpacesAndReturns("  1 nnn          nnn  "));
    }

    @Test
    public void removeSpaces40() {
        Assert.assertEquals("1nnnnnn", CommonUtils.removeSpacesAndReturns("  1 \n nnn \n nnn  "));
    }

    /* ------------------------------------------------------ openClassResource  ------------------------------------ */

    @Test(expected = NullPointerException.class)
    public void openClassResource10() throws IOException {
        CommonUtils.openClassResource(null);
    }

    @Test
    public void openClassResource20() throws IOException {
        try (InputStream stream = CommonUtils.openClassResource(getClass())) {
            byte[] b = new byte[4];
            int read = stream.read(b);
            Assert.assertEquals(b.length, read); /*  тут исходим из предположения,
            что такой короткий буффер читается за один раз и не требует дочитывания */
            Assert.assertArrayEquals(new byte[]{0xffffffca, 0xfffffffe, 0xffffffba, 0xffffffbe}, b);
            /*  элементарная проверка - CAFE - BABE - заголовок любого файла джава класса */
        }
    }

    /* ------------------------------------------------ loadResourceFromZipFile  ------------------------------------ */

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile10() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile20() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, null, "/somePath");
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile30() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, null, "/somepaRelativePath/zipfile.zip");
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile40() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, "somepaRelativePath/zipfile.zip", null);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile50() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, "somepaRelativePath/zipfile.zip", "/somePath");
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile60() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile70() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(), "somepaRelativePath/zipfile.zip", null);
    }

    @Test(expected = NullPointerException.class)
    public void loadResourceFromZipFile80() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(), null, "/somePath");
    }

    @Test
    public void loadResourceFromZipFile90() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip", "some_resource_inside_zip.txt");
        CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip", "directory/some_resource_inside_zip.txt");
        CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip", "directory/subdirectory/some_resource_inside_zip.txt");
    }

    @Test
    public void loadResourceFromZipFile100() throws IOException {
        try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip",
                "some_resource_inside_zip.txt")) {
            Assert.assertEquals("ABCD - 1", getUtfString(stream));
        }
    }

    @Test
    public void loadResourceFromZipFile110() throws IOException {
        try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip",
                "directory/some_resource_inside_zip.txt")) {
            Assert.assertEquals("ABCD - 2", getUtfString(stream));
        }
    }

    @Test
    public void loadResourceFromZipFile120() throws IOException {
        try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip",
                "directory/subdirectory/some_resource_inside_zip.txt")) {
            Assert.assertEquals("ABCD - 3", getUtfString(stream));
        }
    }

    @Test
    public void loadResourceFromZipFile130() throws IOException {
        for (int i = 0; i < 10000; i++) {
            try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                    "resources/zipfiles/resource_zip_file.zip",
                    "directory/subdirectory/some_resource_inside_zip.txt")) {
                Assert.assertEquals("ABCD - 3", getUtfString(stream));
            }
        }
    }



    /* ------------------------------------------- expandListToSize ----------------------------------------------- */


    @Test(expected = NullPointerException.class)
    public void expandListToSize() {
        CommonUtils.expandListToSize(null, 10);
    }

    @Test
    public void expandListToSize10() {
        /* обычное выполнение  - проверка что ничего не рухнет*/
        CommonUtils.expandListToSize(new ArrayList<>(), 1);
    }

    @Test
    public void expandListToSize30() {
        ArrayList<Object> list = new ArrayList<>();
        CommonUtils.expandListToSize(list, 10);
        Assert.assertEquals(10, list.size());
    }

    @Test
    public void getValueFunction31() {
        /* передаются лажовые данные - но в виду того что используется предикат,
        который всегда возвращает константу мы получаем константу */

        Assert.assertEquals(
                "100000",
                CommonUtils.getValueFunction("lkdjfa;kdjf;a;dfadfj;adfj;a", s -> "100000")
        );
    }


    /* ------------------------------------------- getValueFunction ------------------------------------------------------- */

    @Test
    public void getValueFunction40() {
        Assert.assertEquals(new Integer("12"), CommonUtils.getValueFunction("array[12]", TESTES_ORDER_FUNCTION));
    }

    @Test
    public void getValueFunction41() {
        Assert.assertEquals("Step", CommonUtils.getValueFunction("array[Step][12]", TESTES_ORDER_FUNCTION_2));
    }

    @Test
    public void getValueFunction50() {
        Assert.assertEquals(null, CommonUtils.getValueFunction("array[s1]", TESTES_ORDER_FUNCTION));
    }

    @Test
    public void getValueFunction51() {
        Assert.assertEquals("2", CommonUtils.getValueFunction("array[2][s1]", TESTES_ORDER_FUNCTION_2));
    }

    @Test
    public void getValueFunction60() {
        Assert.assertEquals(null, CommonUtils.getValueFunction("not_an_array[1111]", TESTES_ORDER_FUNCTION));
    }

    @Test
    public void getValueFunction61() {
        Assert.assertEquals("step", CommonUtils.getValueFunction("not_an_array[step][1111]", TESTES_ORDER_FUNCTION_2));
    }

    @Test
    public void getValueFunction70() {
        Assert.assertEquals(null, CommonUtils.getValueFunction("array[11111111111111111111111111111]", TESTES_ORDER_FUNCTION));
    }

    private static class SlowInputStream extends ByteArrayInputStream {
        SlowInputStream(byte[] buf) {
            super(buf);
        }

        @Override
        public synchronized int read(byte[] b, int off, int len) {
            len = len > 10 ? 10 : len;
            return super.read(b, off, len);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class T1 {
        public final int i;
        public final String s;

        T1(int i, String s) {
            this.i = i;
            this.s = s;
        }

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            T1 t1 = (T1) o;

            if (i != t1.i) return false;
            if (s != null ? !s.equals(t1.s) : t1.s != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + (s != null ? s.hashCode() : 0);
            return result;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class T2 {
        public final int i;
        public final String s;

        T2(int i, String s) {
            this.i = i;
            this.s = s;
        }
    }

    /* ------------------------------------------------- measureTime ----------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void measureTime10() throws Throwable {
        CommonUtils.measureTime(null);
    }

    @Test
    public void measureTime20() throws Throwable {
        Assert.assertEquals(true, around(CommonUtils.measureTime(() -> Thread.sleep(1000L)),
                1000L, 30));
    }

    @Test
    public void measureTime30() throws Throwable {
        long l = new Random().nextInt(1000) + 1000;
        Assert.assertEquals(true, around(CommonUtils.measureTime(() -> Thread.sleep(l)), l, 300));
    }

        /*  ----------------------------------------- compileString ---------------------------------------------------  */

    @Test(expected = NullPointerException.class)
    public void compileString_error1() {
        CommonUtils.compileString(null);
    }

    @Test(expected = NullPointerException.class)
    public void compileString_error2() {
        CommonUtils.compileString(null, null, "");
    }

    @Test(expected = NullPointerException.class)
    public void compileString_error3() {
        CommonUtils.compileString(null, "one");
    }

    @Test
    public void compileString10() {
        Assert.assertEquals("one_two_three", CommonUtils.compileString("one", "_", "two", "_", "three"));
    }

    @Test
    public void compileString20() {
        Object[] objects = {"_", "two", "_", "three"};
        Assert.assertEquals("one_two_three", CommonUtils.compileString("one", objects));
    }

    @Test
    public void compileString30() {
        Assert.assertEquals("one_two_three", CommonUtils.compileString("one", "_", "two", "_", "three"));
    }

    @Test
    public void compileString40() {
        Assert.assertEquals("two_three", CommonUtils.compileString("", "two", "_", "three"));
    }


/* --------------------------------------------------- checkDefined ------------------------------------------------*/

    @Test(expected = IllegalArgumentException.class)
    public void checkDefined10() {
        CommonUtils.checkDefined(null);
    }

    @Test
    public void checkDefined20() {
        CommonUtils.checkDefined("");
    }

    @Test
    public void checkDefined30() {
        Assert.assertEquals("15", CommonUtils.checkDefined("15"));
    }

    @Test
    public void checkDefined40() {
        CommonUtils.checkDefined("15", "param is not defined");
    }

/* --------------------------------------------------- skipAllExceptions ---------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void skipAllExceptions10() {
        CommonUtils.skipAllExceptions(null);
    }

    @Test
    public void skipAllExceptions20() {
        CommonUtils.skipAllExceptions(() -> {
            throw new Throwable();
        });
    }


/* --------------------------------------------------- skipExceptions ------------------------------------------*/

    @Test
    public void skipExceptions10() {
        CommonUtils.skipExceptions(() -> {
            throw new FileNotFoundException();
        });
    }

    @Test
    public void skipExceptions14() {
        CommonUtils.skipExceptions(() -> {
            @SuppressWarnings("UnusedDeclaration")
            String s = "" + "121312";
        });
    }

    @Test
    public void skipExceptions20() {
        Assert.assertEquals(CommonUtils.skipExceptions(() -> "11", "DefaultValue"), "11");
    }

    @Test
    public void skipExceptions30() {
        Assert.assertEquals(CommonUtils.skipExceptions(() -> {
            throw new FileNotFoundException();
        }, "DefaultValue"), "DefaultValue");
    }

    @Test
    public void skipExceptions40() {
        String[] strings = {""};
        CommonUtils.skipExceptions(
                (CommonUtils.Code) () -> {
                    throw new FileNotFoundException();
                },
                () -> {
                    strings[0] = "OK";
                });

        Assert.assertEquals("OK", strings[0]);
    }

    @Test
    public void skipExceptions50() {
        String[] strings = {""};
        CommonUtils.skipExceptions(
                (CommonUtils.Code) () -> {
                    throw new FileNotFoundException();
                },
                (Runnable) null);

        Assert.assertEquals("", strings[0]);
    }

    @Test
    public void skipExceptions60() {

        int i = CommonUtils.skipExceptions(
                (CommonUtils.SupressedExceptionSupplier<Integer>) () -> {
                    throw new Exception();
                },
                (Function<Exception, Integer>) e -> 0
        );
        Assert.assertEquals(i, 0);
    }

    @Test
    public void skipExceptions70() {
        int i = CommonUtils.skipExceptions(
                (CommonUtils.SupressedExceptionSupplier<Integer>) () -> 1,
                (Function<Exception, Integer>) e -> 0
        );
        Assert.assertEquals(i, 1);
    }

    @Test
    public void skipExceptions80() {
        int i = CommonUtils.skipExceptions(
                (CommonUtils.SupressedExceptionSupplier<Integer>) () -> 1,
                (Function<Exception, Integer>) e -> 0
        );
        Assert.assertEquals(i, 1);
    }

/* --------------------------------------------------- ensure ------------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void ensure10() {
        CommonUtils.ensure("1000", null);
    }

    @Test(expected = NullPointerException.class)
    public void ensure20() {
        CommonUtils.ensure(null, () -> null);
    }

    @Test
    public void ensure30() {
        Assert.assertEquals("1000", CommonUtils.ensure("1000", () -> null));
    }

    @Test
    public void ensure40() {
        Assert.assertEquals("100", CommonUtils.ensure(null, () -> "100"));
    }


    @Test(expected = NullPointerException.class)
    public void copyStreamContent10() throws IOException {
        CommonUtils.copyStreamContent(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void copyStreamContent20() throws IOException {
        CommonUtils.copyStreamContent(new ByteArrayInputStream(new byte[]{}), null);
    }

    @Test(expected = NullPointerException.class)
    public void copyStreamContent30() throws IOException {
        CommonUtils.copyStreamContent(null, new ByteArrayOutputStream());
    }

    @Test
    public void copyStreamContent40() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (FileInputStream inputStream = new FileInputStream(CommonUtils.getAbsoluteResourcePath(getClass(),
                "resources/text/resource_file_1.txt"))) {
            CommonUtils.copyStreamContent(inputStream, outputStream);
        }

        try (FileInputStream inputStream = new FileInputStream(CommonUtils.getAbsoluteResourcePath(getClass(),
                "resources/text/resource_file_1.txt"))) {
            Assert.assertEquals(true, CommonUtils.compareStreams(inputStream,
                    new ByteArrayInputStream(outputStream.toByteArray())));
        }
    }

    /* ------------------------------------------------------- suppress ------------------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void suppress10() {
        CommonUtils.suppress(null);
    }

    @Test
    public void suppress20() {
        CommonUtils.suppress(() -> 20003);
    }

    @Test(expected = IllegalStateException.class)
    public void suppress30() {
        CommonUtils.suppress(() -> {
            throw new IllegalStateException();
        });
    }

    @Test(expected = NumberFormatException.class)
    public void suppress40() {
        CommonUtils.suppress(() -> {
            throw new NumberFormatException();
        });
    }

    @Test(expected = RuntimeException.class)
    public void suppress50() {
        CommonUtils.suppress(() -> {
            throw new Exception();
        });
    }

    /* --------- suppress(SupressedExceptionSupplier<T> suppressedCode, Class<T> type) ---------------------------- */

    @Test(expected = NullPointerException.class)
    public void suppress100() {
        CommonUtils.suppress(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void suppress110() {
        CommonUtils.suppress(() -> true, null);
    }

    @Test(expected = NullPointerException.class)
    public void suppress120() {
        CommonUtils.suppress(null, String.class);
    }

    @Test
    public void suppress130() {
        CommonUtils.suppress(() -> 20003, Integer.class);
    }

    private static int retIntException() throws Exception {
        throw new Exception();
    }

    private static int retIntNumberFormatException() {
        throw new NumberFormatException();
    }

    @Test(expected = RuntimeException.class)
    public void suppress140() {
        CommonUtils.suppress(CommonUtilsCheck::retIntException, Integer.TYPE);
    }

    @Test(expected = NumberFormatException.class)
    public void suppress150() {
        CommonUtils.suppress(CommonUtilsCheck::retIntNumberFormatException, Integer.TYPE);
    }

/* ------------------------------------------------------- suppressAll  --------------------------------------------*/

    @Test(expected = NullPointerException.class)
    public void suppressAll10() {
        CommonUtils.suppressAll(null);
    }

    @Test
    public void suppressAll20() {
        CommonUtils.suppressAll(() -> 100);
    }

    @Test(expected = IllegalStateException.class)
    public void suppressAll30() {
        CommonUtils.suppressAll(() -> {
            throw new IllegalStateException();
        });
    }

    @Test(expected = NumberFormatException.class)
    public void suppressAll40() {
        CommonUtils.suppress(() -> {
            throw new NumberFormatException();
        });
    }

    @Test(expected = RuntimeException.class)
    public void suppressAll50() {
        CommonUtils.suppress(() -> {
            throw new Exception();
        });
    }

    @Test(expected = RuntimeException.class)
    public void suppressAll60() {
        CommonUtils.suppressAll(() -> {
            throw new Throwable();
        });
    }

    /* --------------------------------------------------- suppressWOResult ----------------------------------------*/


    @Test(expected = NullPointerException.class)
    public void suppressWOResult10() {
        CommonUtils.suppressWOResult(null);
    }

    @Test(expected = RuntimeException.class)
    public void suppressWOResult20() {
        CommonUtils.suppressWOResult(() -> {
            throw new RuntimeException();
        });
    }

    @Test(expected = IllegalStateException.class)
    public void suppressWOResult30() {
        CommonUtils.suppressWOResult(() -> {
            throw new IllegalStateException();
        });
    }

    @Test(expected = RuntimeException.class)
    public void suppressWOResult40() {
        CommonUtils.suppressWOResult(() -> {
            throw new IOException();
        });
    }



    /* --------------------------------------------------- arrayToList ---------------------------------------------*/


    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    @Test(expected = NullPointerException.class)
    public void arrayToList10() {
        CommonUtils.arrayToList(null);
    }

    @Test
    public void arrayToList20() {
        List<String> list = CommonUtils.arrayToList("en todos lados se cuecen habas!!!");
        list.add("112");
    }

    @Test
    public void arrayToList30() {
        Assert.assertEquals(Arrays.asList("1", "2", "3"), CommonUtils.arrayToList("1", "2", "3"));
    }

    @Test
    public void arrayToList40() {
        Assert.assertEquals(Arrays.asList("1", null, "3"), CommonUtils.arrayToList("1", null, "3"));
    }

    /* ------------------------------------------------------- reduceUnique --------------------------------------- */


    @Test(expected = IllegalArgumentException.class)
    public void reduceUnique10() {
        CommonUtils.reduceUnique(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void reduceUnique20() {
        CommonUtils.reduceUnique("12345678", "2131231");
    }

    @Test
    public void reduceUnique30() {
        Assert.assertEquals("12345678", CommonUtils.reduceUnique("12345678", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void reduceUnique40() {
        Stream.of("1", "1", "1", "1", "1", "1", "2").reduce(null, CommonUtils::reduceUnique);
    }

    @Test
    public void reduceUnique50() {
        Assert.assertEquals("1",
                Stream.of("1", "1", "1", "1", "1", "1", "1").reduce(null, CommonUtils::reduceUnique));
    }


    /* ------------------------------------------------------- intersectCollections ------------------------------ */


    @Test(expected = NullPointerException.class)
    public void intersectCollections10() {
        CommonUtils.intersectCollections(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void intersectCollections20() {
        CommonUtils.intersectCollections(null, Arrays.asList("1", "2"));
    }

    @Test(expected = NullPointerException.class)
    public void intersectCollections30() {
        CommonUtils.intersectCollections(Arrays.asList("1", "2"), null);
    }

    @Test
    public void intersectCollections40() {
        List<String> strings = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList("1", "2"),
                Arrays.asList("1", "2")));
        strings.sort(COMPARE_STRINGS);
        List<String> expected = new ArrayList<>(Arrays.asList("1", "2"));
        expected.sort(COMPARE_STRINGS);
        Assert.assertEquals(expected, strings);
    }

    @Test
    public void intersectCollections50() {
        List<String> strings = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList("1", "2", "3", "4"),
                Arrays.asList("1", "2", "3")));
        strings.sort(COMPARE_STRINGS);
        List<String> expected = new ArrayList<>(Arrays.asList("1", "2", "3"));
        expected.sort(COMPARE_STRINGS);
        Assert.assertEquals(expected, strings);
    }

    @Test
    public void intersectCollections60() {
        List<String> strings = new ArrayList<>(CommonUtils.intersectCollections());
        strings.sort(COMPARE_STRINGS);
        Assert.assertEquals(new ArrayList<>(), strings);
    }

    @Test
    public void intersectCollections70() {
        List<String> strings = new ArrayList<>(CommonUtils.intersectCollections(
                Arrays.asList("2", "3", "4", "5"),
                Arrays.asList("1", "3", "4", "5"),
                Arrays.asList("1", "2", "4", "5"),
                Arrays.asList("1", "2", "3", "5"),
                Arrays.asList("1", "2", "3", "4", "5"),
                Arrays.asList("1", "2", "3", "4", "5"))
        );
        strings.sort(COMPARE_STRINGS);
        List<String> expected = new ArrayList<>(Arrays.asList("5"));
        expected.sort(COMPARE_STRINGS);
        Assert.assertEquals(expected, strings);
    }

    @Test
    public void intersectCollections80() {
        List<Integer> ints = new ArrayList<>(CommonUtils.intersectCollections(
                Arrays.asList(2, 3, 4, 5),
                Arrays.asList(1, 3, 4, 5),
                Arrays.asList(1, 2, 4, 5),
                Arrays.asList(1, 2, 3, 5),
                Arrays.asList(1, 2, 3, 4, 5),
                Arrays.asList(1, 2, 3, 4, 5))
        );
        ints.sort(Integer::compareTo);
        List<Integer> expected = new ArrayList<>(Arrays.asList(5));
        expected.sort(Integer::compareTo);
        Assert.assertEquals(expected, ints);
    }

    @Test
    public void intersectCollections90() {
        List<Integer> ints = new ArrayList<>(CommonUtils.intersectCollections(
                Arrays.asList(2, 3, 4, 5, null),
                Arrays.asList(2, 3, 4, 5, null),
                Arrays.asList(1, 3, 4, 5, null),
                Arrays.asList(1, 2, 4, 5, null),
                Arrays.asList(1, 2, 3, 5, null),
                Arrays.asList(1, 2, 3, 4, 5, null),
                Arrays.asList(1, 2, 3, 4, 5, null))
        );

        ints.sort(COMPARE_INTEGERS);
        Assert.assertEquals(Arrays.asList(null, 5), ints);
    }

    @Test
    public void intersectCollections100() {
        List<Integer> ints = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList(2, 5, 5, 5, 5)));
        ints.sort(COMPARE_INTEGERS);
        Assert.assertEquals(Arrays.asList(2, 5), ints);
    }

    @Test
    public void intersectCollections110() {
        List<Integer> ints = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList(2, null, 5, 5, 5)));
        ints.sort(COMPARE_INTEGERS);
        Assert.assertEquals(Arrays.asList(null, 2, 5), ints);
    }

    /* ------------------------------------------------------ eval ---------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void eval10() {
        CommonUtils.eval(null);
    }

    @Test
    public void eval20() {
        Assert.assertEquals(Arrays.asList("1", "2", "3"),
                CommonUtils.eval(() -> {
                    List<String> strings = new ArrayList<>();
                    strings.add("1");
                    strings.add("2");
                    strings.add("3");
                    return strings;
                })
        );
    }


    /* ----------------------------------------- suppressAllWOResult ---------------------------------------------- */


    @Test(expected = NullPointerException.class)
    public void suppressAllWOResult10() {
        CommonUtils.suppressAllWOResult(null);
    }


    @Test(expected = RuntimeException.class)
    public void suppressAllWOResult20() {
        CommonUtils.suppressAllWOResult(() -> {
            throw new IOException();
        });
    }

    @Test(expected = RuntimeException.class)
    public void suppressAllWOResult30() {
        CommonUtils.suppressAllWOResult(() -> {
            throw new Throwable();
        });
    }

    @Test(expected = IllegalStateException.class)
    public void suppressAllWOResult40() {
        CommonUtils.suppressAllWOResult(() -> {
            throw new IllegalStateException();
        });
    }



   /* ------------------------------------------------------- check ------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void check10() {
        CommonUtils.check(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void check20() {
        CommonUtils.check(() -> "a".equals("a"), null);
    }

    @Test(expected = NullPointerException.class)
    public void check30() {
        CommonUtils.check(null, IllegalStateException::new);
    }

    private static class Exc extends RuntimeException {

    }

    @Test(expected = Exc.class)
    public void check40() {
        CommonUtils.check(() -> false, Exc::new);
    }

    @Test
    public void check50() {
        CommonUtils.check(() -> true, Exc::new);
    }

   /* ------------------------------------------------------- check2 ------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void check100() {
        CommonUtils.check(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void check110() {
        CommonUtils.check(() -> "a".equals("a"), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void check120() {
        CommonUtils.check(() -> "a", s -> true, null);
    }

    @Test(expected = NullPointerException.class)
    public void check130() {
        CommonUtils.check(null, s -> true, Exc::new);
    }

    @Test(expected = NullPointerException.class)
    public void check140() {
        CommonUtils.check(() -> "a", null, Exc::new);
    }

    @Test(expected = Exc.class)
    public void check150() {
        CommonUtils.check(() -> "a", s -> false, Exc::new);
    }

    @Test(expected = Exc.class)
    public void check160() {
        CommonUtils.check(() -> "a", s -> false, Exc::new);
    }

    @Test
    public void check170() {
        CommonUtils.check(() -> "a", s -> s.equals("a"), Exc::new);
    }

    @Test(expected = Exc.class)
    public void check180() {
        CommonUtils.check(() -> "a", s -> s.equals("c"), Exc::new);
    }

   /* ------------------------------------------------------- stackTrace ------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void stackTrace10() {
        CommonUtils.stackTrace(null);
    }

    @Test
    public void stackTrace20() {
        String stackTrace = CommonUtils.stackTrace(new Throwable());
        Assert.assertTrue(new BufferedReader(new StringReader(stackTrace)).lines().
                filter(s -> !s.contains("java.lang.Throwable")).
                allMatch(s1 -> s1.trim().startsWith("at")));
        Assert.assertTrue(new BufferedReader(new StringReader(stackTrace)).lines().
                anyMatch(s1 -> s1.contains("CommonUtilsCheck.stackTrace20")));
    }



   /* ------------------------------------------------------- skipErrors ------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void skipErrors10() {
        CommonUtils.skipErrors(null, null);
    }

    @Test
    public void skipErrors20() {
        CommonUtils.skipErrors(() -> {
        }, null);
    }

    @Test(expected = NullPointerException.class)
    public void skipErrors30() {
        CommonUtils.skipErrors(null, t -> {
        });
    }

    @Test
    public void skipErrors40() {
        AtomicReference<Throwable> reference = new AtomicReference<>();
        CommonUtils.skipErrors(() -> {
            throw new IllegalStateException();
        }, reference::set);
        Assert.assertTrue(reference.get() instanceof IllegalStateException);
    }

   /* ------------------------------------------------------- privates ------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void setCombinations10() {
        CommonUtils.setCombinations(null);
    }

    private enum Empty {
    }

    @Test
    public void setCombinations20() {
        Assert.assertEquals(new HashSet<>(Collections.singletonList(new HashSet<>())), combinations(Empty.class));
    }

    private enum OneElement {
        A
    }

    @Test
    public void setCombinations30() {
        Assert.assertEquals(new HashSet<>(Arrays.asList(
                new HashSet<>(),
                new HashSet<>(Arrays.asList(OneElement.A))
        )), combinations(OneElement.class));
    }


    private enum TwoOneElements {
        A, B
    }

    @Test
    public void setCombinations40() {
        Assert.assertEquals(new HashSet<>(Arrays.asList(
                new HashSet<>(),
                new HashSet<>(Arrays.asList(TwoOneElements.A)),
                new HashSet<>(Arrays.asList(TwoOneElements.B)),
                new HashSet<>(Arrays.asList(TwoOneElements.A, TwoOneElements.B))
        )), combinations(TwoOneElements.class));
    }

    private enum ThreeElements {
        A, B, C
    }

    @Test
    public void setCombinations50() {
        Assert.assertEquals(new HashSet<>(Arrays.asList(
                new HashSet<>(),
                new HashSet<>(Arrays.asList(ThreeElements.B, ThreeElements.A)),
                new HashSet<>(Arrays.asList(ThreeElements.A, ThreeElements.C)),
                new HashSet<>(Arrays.asList(ThreeElements.B, ThreeElements.C)),
                new HashSet<>(Arrays.asList(ThreeElements.A)),
                new HashSet<>(Arrays.asList(ThreeElements.B)),
                new HashSet<>(Arrays.asList(ThreeElements.C)),
                new HashSet<>(Arrays.asList(ThreeElements.A, ThreeElements.B, ThreeElements.C))
        )), combinations(ThreeElements.class));
    }


    private enum FourElements {
        A, B, C, D
    }

    @Test
    public void setCombinations60() {
        Assert.assertEquals(new HashSet<>(Arrays.asList(
                new HashSet<>(),
                new HashSet<>(Arrays.asList(FourElements.A)),
                new HashSet<>(Arrays.asList(FourElements.B)),
                new HashSet<>(Arrays.asList(FourElements.C)),
                new HashSet<>(Arrays.asList(FourElements.D)),

                new HashSet<>(Arrays.asList(FourElements.A, FourElements.D)),
                new HashSet<>(Arrays.asList(FourElements.B, FourElements.C)),
                new HashSet<>(Arrays.asList(FourElements.D, FourElements.C)),
                new HashSet<>(Arrays.asList(FourElements.A, FourElements.B)),
                new HashSet<>(Arrays.asList(FourElements.A, FourElements.C)),
                new HashSet<>(Arrays.asList(FourElements.D, FourElements.B)),

                new HashSet<>(Arrays.asList(FourElements.A, FourElements.B, FourElements.D)),
                new HashSet<>(Arrays.asList(FourElements.A, FourElements.D, FourElements.C)),
                new HashSet<>(Arrays.asList(FourElements.A, FourElements.B, FourElements.C)),
                new HashSet<>(Arrays.asList(FourElements.D, FourElements.B, FourElements.C)),

                new HashSet<>(Arrays.asList(FourElements.A, FourElements.B, FourElements.C, FourElements.D))
        )), combinations(FourElements.class));
    }






   /* ------------------------------------------------------- privates ------------------------------------------- */

    private boolean around(long expected, long real, int trashold) {
        long diff = expected - real;
        diff = diff > 0 ? diff : -diff;
        return diff < trashold;
    }

    private String getUtfString(InputStream stream) throws IOException {
        byte[] chunk = {10};
        int read;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(100);
        while ((read = stream.read(chunk)) != -1) {
            outputStream.write(chunk, 0, read);
        }
        return outputStream.toString("UTF-8");
    }

    private static String getAbsoluteResourcePathEtalon(Class<?> clazz, String resourceName) {
        URL resource = clazz.getResource(ResourceUtils.classFileName(clazz));
        if (resource == null) {
            throw new IllegalStateException("resource is null");
        }
        return Paths.get(excludeTrailingBackslash(new File(resource.getFile()).getParent()),
                "/", excludeFirstBackslash(resourceName)).
                normalize().toFile().getAbsolutePath();
    }

    private static <T extends Enum> Set<Set<T>> combinations(Class<T> clazz) {
        return StreamSupport.stream(CommonUtils.setCombinations(clazz).spliterator(), false).collect(Collectors.toSet());
    }


}
      

