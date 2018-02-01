package com.steammachine.common.utils.commonutils;


import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */
@SuppressWarnings("EqualsWithItself")
class CommonUtilsCheck {


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
    void expandListToSize40() {
        List<String> list = CommonUtils.expandListToSize(new ArrayList<>(), 1);
        list.set(0, "Adelante con faroles");
        assertEquals(Collections.singletonList("Adelante con faroles"), list);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void getValueFunction10() {
        CommonUtils.getValueFunction(null, null);
    }


    @Test
    @Expected(expected = NullPointerException.class)
    void getValueFunction20() {
        CommonUtils.getValueFunction("15", null);
    }

    /* --------------------------------------------------------------------------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void testNormalizeString() {
        CommonUtils.excludeTrailingBackslash(null);
    }


    @Test
    void testNormalizeString2() {
        CommonUtils.excludeTrailingBackslash("http://one/two/three");
    }


    @Test
    void testNormalizeString3() {
        assertEquals(
                CommonUtils.excludeTrailingBackslash("http://one/two/three"),
                "http://one/two/three"
        );
    }


    @Test
    void testNormalizeString4() {
        assertEquals(
                CommonUtils.excludeTrailingBackslash("http://one/two/three/"),
                "http://one/two/three"
        );
    }


    @Test
    void testNormalizeString5() {
        assertEquals(
                CommonUtils.excludeTrailingBackslash("/"),
                ""
        );
    }


    @Test
    void testNormalizeString6() {
        assertEquals(
                CommonUtils.excludeTrailingBackslash(""),
                ""
        );
    }


    @Test
    void testNormalizeString7() {
        assertEquals(
                CommonUtils.excludeTrailingBackslash(" "),
                " "
        );
    }


    /*  --------------------------------------  excludeFirstBackslash ---------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void excludeFirstBackslash10() {
        CommonUtils.excludeFirstBackslash(null);
    }

    @Test
    void excludeFirstBackslash20() {
        assertEquals(
                CommonUtils.excludeFirstBackslash(" "),
                " "
        );
    }

    @Test
    void excludeFirstBackslash30() {
        assertEquals(
                CommonUtils.excludeFirstBackslash("TTTE"),
                "TTTE"
        );
    }

    @Test
    void excludeFirstBackslash40() {
        assertEquals(
                "TTTE",
                CommonUtils.excludeFirstBackslash("/TTTE")
        );
    }

    @Test
    void excludeFirstBackslash50() {
        assertEquals(
                "TTTE/",
                CommonUtils.excludeFirstBackslash("/TTTE/")
        );
    }

    /*  --------------------------------------  allThreadsAreDone ----------------------------------------------- */

    @SuppressWarnings("ConstantConditions")
    @Test
    @Expected(expected = NullPointerException.class)
    void allThreadsAreDone_error() {
        Thread[] threads = null;
        assertEquals(true, CommonUtils.allThreadsAreDone(threads));
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void allThreadsAreDone_error2() {
        Thread[] threads = new Thread[]{null};
        assertTrue(CommonUtils.allThreadsAreDone(threads));
    }

    @Test
    void allThreadsAreDone1() {
        assertTrue(CommonUtils.allThreadsAreDone());
    }

    @Test
    void allThreadsAreDone2() throws InterruptedException {
        Thread thread = new Thread();
        thread.start();

        while (thread.isAlive()) {
            Thread.sleep(10L);
        }

        assertTrue(CommonUtils.allThreadsAreDone(thread));
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void throwsExc2() throws Throwable {
        throw new IllegalStateException();
    }


    @Test
    void allThreadsAreDone3() throws Throwable {
        Assertions.assertTimeoutPreemptively(Duration.of(1000, ChronoUnit.MILLIS), () -> {
            Thread[] threads = {new Thread(), new Thread()};
            for (Thread thread : threads) {
                thread.start();
            }

            while (!testes_all_ThreadsAreDone(threads)) {
                Thread.sleep(10L);
            }

            assertTrue(CommonUtils.allThreadsAreDone(threads));
        });

    }

    @Test
    void allThreadsAreDone4() throws Throwable {
        Assertions.assertTimeoutPreemptively(Duration.of(5000, ChronoUnit.MILLIS), () -> {
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
            assertTrue(CommonUtils.allThreadsAreDone(threads));
        });
    }

    @Test
    void getValueFunction30() {
        /* передаются лажовые данные - но в виду того что используется предикат,
        который всегда возвращает константу мы получаем константу */

        assertEquals(
                new Integer("100000"),
                CommonUtils.getValueFunction("lkdjfa;kdjf;a;dfadfj;adfj;a", s -> 100000)
        );
    }
    
    /*  ------------------------------------------ normalizeString -------------------------------------------------- */


    @Test
    @Expected(expected = NullPointerException.class)
    void normalizeStringWrongParams() throws IOException {
        CommonUtils.normalizeString(null);
    }

    @Test
    void normalizeString1() throws IOException {
        CommonUtils.normalizeString("");
    }


    @Test
    void normalizeStringEmptyString10() throws IOException {
        assertEquals(
                CommonUtils.normalizeString(""),
                ""
        );
    }

    @Test
    void normalizeStringSimpleString20() throws IOException {
        assertEquals(
                CommonUtils.normalizeString("A B C"),
                "A B C"
        );
    }

    @Test
    void normalizeStringMoreSpaces30() throws IOException {
        assertEquals(
                CommonUtils.normalizeString("    A       B       C    "),
                "A B C"
        );
    }

    @Test
    void normalizeStringWithReturns40() throws IOException {
        assertEquals(
                CommonUtils.normalizeString("    A   \r\n    B    \r\n   \r    C    "),
                "A B C"
        );
    }

    @Test
    void normalizeStringWithReturns50() throws IOException {
        assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_2.txt",
                        CHARSET)),
                "A V E DE Presa"
        );
    }

    @Test
    void normalizeStringWithReturns60() throws IOException {
        assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_3.txt",
                        CHARSET)),
                "A V E DE Presa"
        );
    }

    @Test
    void normalizeStringWithReturns70() throws IOException {
        assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_4.txt",
                        CHARSET)),
                "A V"
        );
    }


    @Test
    void normalizeStringWithReturns80() throws IOException {
        assertEquals(
                CommonUtils.normalizeString(ResourceUtils.loadResourceAsString(CommonUtilsCheck.class,
                        "resources/sometext/resource_text_file_5.txt",
                        CHARSET)),
                "A"
        );
    }

    @Test
    void normalizeStringWithReturns90() throws IOException {
        assertEquals(
                CommonUtils.normalizeString("   1   2     3 4     5       6 7 \r\n  8"),
                "1 2 3 4 5 6 7 8"

        );
    }

    @Test
    void normalizeStringWithReturns100() throws IOException {
        assertEquals(
                CommonUtils.normalizeString("Required:\n" + "1 600 population"),
                "Required: 1 600 population"
        );
    }

    /* -------------------------------------------- copyStream -------------------------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void copyStream_error1() throws IOException {
        CommonUtils.copyStreamContent(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void copyStream_error2() throws IOException {
        CommonUtils.copyStreamContent(null, new ByteArrayOutputStream());
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void copyStream_error3() throws IOException {
        CommonUtils.copyStreamContent(new ByteArrayInputStream(new byte[]{1, 2, 3}), null);
    }

    @Test
    void copyStream_NormalExecution() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommonUtils.copyStreamContent(new ByteArrayInputStream(new byte[]{1, 2, 3}), outputStream);
        assertArrayEquals(new byte[]{1, 2, 3}, outputStream.toByteArray());
    }


    /* -------------------------------------------- compareStreams --------------------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void compareStreamsError1() throws IOException {
        CommonUtils.compareStreams(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void compareStreamsError2() throws IOException {
        CommonUtils.compareStreams(STREAM_REF, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void compareStreamsError3() throws IOException {
        CommonUtils.compareStreams(null, STREAM_REF);
    }

    @Test
    void compareStreams() throws IOException {
        try (InputStream stream = new ByteArrayInputStream(
                ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"))) {
            try (InputStream slowStream = new SlowInputStream(
                    ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"))) {
                assertEquals(CommonUtils.compareStreams(stream, slowStream), true);
            }
        }
    }

    @Test
    void compareStreams2() throws IOException {
        try (InputStream stream = new ByteArrayInputStream(
                ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"))) {
            try (InputStream slowStream = new SlowInputStream(
                    ResourceUtils.loadResourceAsBytes(CommonUtilsCheck.class, "resources/sometext/resource_text_file_2.txt"))) {
                assertEquals(CommonUtils.compareStreams(stream, slowStream), false);
            }
        }
    }

    /* ------------------------------------------------------- objectsAreEqual ---------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void compareObjects10() {
        CommonUtils.objectsAreEqual(null, null, null);
    }

    @Test
    void compareObjects20() {
        assertTrue(CommonUtils.objectsAreEqual(new Object(), "", (t, e) -> true));
    }

    @Test
    void compareObjects30() {
        assertFalse(CommonUtils.objectsAreEqual(new Object(), "", (t, e) -> false));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution() {
        assertTrue(CommonUtils.objectsAreEqual(null, null, STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution2() {
        assertTrue(CommonUtils.objectsAreEqual("", new StringWrapper(""), STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution3() {
        assertTrue(CommonUtils.objectsAreEqual(null, null, STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution4() {
        assertTrue(CommonUtils.objectsAreEqual("Hasta la victoria. Siempre!",
                new StringWrapper("Hasta la victoria. Siempre!"), STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution5() {
        assertFalse(CommonUtils.objectsAreEqual(null,
                new StringWrapper("Hasta la victoria. Siempre!"), STRING_STRING_WRAPPER_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution6() {
        assertTrue(CommonUtils.objectsAreEqual(new T1(10, "Llorona de azul celeste"),
                new T2(10, "Llorona de azul celeste"),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution7() {
        assertFalse(CommonUtils.objectsAreEqual(new T1(11, "Llorona de azul celeste"),
                new T2(10, "Llorona de azul celeste"),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution8() {
        assertFalse(CommonUtils.objectsAreEqual(new T1(10, "Llorona de azul celeste"),
                new T2(10, "Llorona"),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution9() {
        assertTrue(CommonUtils.objectsAreEqual(new T1(10, null),
                new T2(10, null),
                T1_T2_OBJECT_COMPARATOR));
    }

    @Test
    void diffTypesObjectsEqual_normalExecution10() {
        assertTrue(CommonUtils.objectsAreEqual(null, null, T1_T2_OBJECT_COMPARATOR));
    }

    /* ------------------------------------------ getAbsoluteResourcePath ------------------------------------------- */


    @Test
    @Expected(expected = NullPointerException.class)
    void getAbsoluteResourcePath_error1() {
        CommonUtils.getAbsoluteResourcePath(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void getAbsoluteResourcePath_error2() {
        CommonUtils.getAbsoluteResourcePath(null, "");
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void getAbsoluteResourcePath_error3() {
        CommonUtils.getAbsoluteResourcePath(CommonUtilsCheck.class, null);
    }

    @Test
    void getAbsoluteResourcePath10() {
        assertEquals(
                getAbsoluteResourcePathEtalon(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt"),
                CommonUtils.getAbsoluteResourcePath(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt")
        );
    }

    @Test
    void getAbsoluteResourcePath20() throws IOException {
        Class<CommonUtilsCheck> clazz = CommonUtilsCheck.class;
        String expected = getAbsoluteResourcePathEtalon(clazz, "resources/sometext/resource_test_file_7.txt");
        Files.deleteIfExists(Paths.get(expected));
        assertEquals(false, Files.exists(Paths.get(expected))); /* проверяем что пока - файла нет */
        String absoluteResourcePath = CommonUtils.getAbsoluteResourcePath(clazz, "resources/sometext/resource_test_file_7.txt");
        assertEquals(
                expected,
                absoluteResourcePath
        );
    }

    @Test
    void getAbsoluteResourcePath30() throws IOException {
        Class<CommonUtilsCheck> clazz = CommonUtilsCheck.class;
        String expected = getAbsoluteResourcePathEtalon(clazz, "resources/sometext/resource_test_file_7.txt");
        Files.deleteIfExists(Paths.get(expected));
        assertEquals(false, Files.exists(Paths.get(expected))); /* проверяем что пока - файла нет */
        String absoluteResourcePath = CommonUtils.getAbsoluteResourcePath(clazz, "/resources/sometext/resource_test_file_7.txt");
        assertEquals(
                expected,
                absoluteResourcePath
        );
    }

    /* ------------------------------------------ getResourceUri ---------------------------------------------------- */


    @Test
    @Expected(expected = NullPointerException.class)
    void getResourceUri_error1() throws URISyntaxException {
        CommonUtils.getResourceUri(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void getResourceUri_error2() throws URISyntaxException {
        CommonUtils.getResourceUri(null, "");
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void getResourceUri_error3() throws URISyntaxException {
        CommonUtils.getResourceUri(CommonUtilsCheck.class, null);
    }

    @Test
    void getResourceUri_normal1() throws URISyntaxException {
        CommonUtils.getResourceUri(CommonUtilsCheck.class, "resources/sometext/resource_test_file_6.txt");
    }

    /* ----------------------------------------------- asArray --------------------------------------------- */

    @Test
    @Expected(expected = IllegalStateException.class)
    void asArray_error1() {
        CommonUtils.asArray(1);
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void asArray_error2() {
        CommonUtils.asArray(2, 0);
    }

    @Test
    void asArray_0() {
        assertArrayEquals(new int[]{}, CommonUtils.asArray(0));
    }

    @Test
    void asArray_1() {
        assertArrayEquals(new int[]{1}, CommonUtils.asArray(1, 1));
    }

    @Test
    void asArray_2() {
        assertArrayEquals(new int[]{1, 2, 3}, CommonUtils.asArray(3, 1, 2, 3));
    }

    @Test
    void asArray_3() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        assertArrayEquals(new int[]{i1, i2, i3}, CommonUtils.asArray(3, i1, i2, i3));
    }

    @Test
    void asArray_4() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        int[] expecteds = {i1, i2, i3};
        int[] actuals = CommonUtils.asArray(3, i1, i2, i3);
        assertArrayEquals(expecteds, actuals);
        assertNotSame(expecteds, actuals);
    }

    /* ----------------------------------------------- checkToArray ---------------------------------------------------- */

    @Test
    void checkToArray10() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        Integer[] expecteds = {i1, i2, i3};
        Integer[] actuals = CommonUtils.checkToArray(3, i1, i2, i3);
        assertArrayEquals(expecteds, actuals);
        assertNotSame(expecteds, actuals);
    }

    @Test
    void checkToArray20() {
        Random random = new Random();

        long i1 = random.nextInt(1000);
        long i2 = random.nextInt(1000);
        long i3 = random.nextInt(1000);

        Long[] expecteds = {i1, i2, i3};
        Long[] actuals = CommonUtils.checkToArray(3, i1, i2, i3);
        assertArrayEquals(expecteds, actuals);
        assertNotSame(expecteds, actuals);
    }


    /* ----------------------------------------------- toArray ------------------------------------------------- */


    @Test
    void toArray10() {
        Random random = new Random();

        int i1 = random.nextInt(1000);
        int i2 = random.nextInt(1000);
        int i3 = random.nextInt(1000);

        Integer[] expecteds = {i1, i2, i3};
        Integer[] actuals = CommonUtils.toArray(i1, i2, i3);
        assertArrayEquals(expecteds, actuals);
        assertNotSame(expecteds, actuals);
    }

    @Test
    void toArray20() {
        Random random = new Random();

        long i1 = random.nextInt(1000);
        long i2 = random.nextInt(1000);
        long i3 = random.nextInt(1000);

        Long[] expecteds = {i1, i2, i3};
        Long[] actuals = CommonUtils.toArray(i1, i2, i3);
        assertArrayEquals(expecteds, actuals);
        assertNotSame(expecteds, actuals);
    }

    /* ------------------------------------------------ removeSpacesAndReturns ------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void removeSpaces10() {
        CommonUtils.removeSpacesAndReturns(null);
    }

    @Test
    void removeSpaces20() {
        assertEquals("1nnnnnn", CommonUtils.removeSpacesAndReturns("1 nnn          nnn"));
    }

    @Test
    void removeSpaces30() {
        assertEquals("1nnnnnn", CommonUtils.removeSpacesAndReturns("  1 nnn          nnn  "));
    }

    @Test
    void removeSpaces40() {
        assertEquals("1nnnnnn", CommonUtils.removeSpacesAndReturns("  1 \n nnn \n nnn  "));
    }

    /* ------------------------------------------------------ openClassResource  ------------------------------------ */

    @Test
    @Expected(expected = NullPointerException.class)
    void openClassResource10() throws IOException {
        CommonUtils.openClassResource(null);
    }

    @Test
    void openClassResource20() throws IOException {
        try (InputStream stream = CommonUtils.openClassResource(getClass())) {
            byte[] b = new byte[4];
            int read = stream.read(b);
            assertEquals(b.length, read); /*  тут исходим из предположения,
            что такой короткий буффер читается за один раз и не требует дочитывания */
            assertArrayEquals(new byte[]{0xffffffca, 0xfffffffe, 0xffffffba, 0xffffffbe}, b);
            /*  элементарная проверка - CAFE - BABE - заголовок любого файла джава класса */
        }
    }

    /* ------------------------------------------------ loadResourceFromZipFile  ------------------------------------ */

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile10() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile20() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, null, "/somePath");
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile30() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, null, "/somepaRelativePath/zipfile.zip");
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile40() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, "somepaRelativePath/zipfile.zip", null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile50() throws IOException {
        CommonUtils.loadResourceFromZipFile(null, "somepaRelativePath/zipfile.zip", "/somePath");
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile60() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(), null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile70() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(), "somepaRelativePath/zipfile.zip", null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void loadResourceFromZipFile80() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(), null, "/somePath");
    }

    @Test
    void loadResourceFromZipFile90() throws IOException {
        CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip", "some_resource_inside_zip.txt");
        CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip", "directory/some_resource_inside_zip.txt");
        CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip", "directory/subdirectory/some_resource_inside_zip.txt");
    }

    @Test
    void loadResourceFromZipFile100() throws IOException {
        try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip",
                "some_resource_inside_zip.txt")) {
            assertEquals("ABCD - 1", getUtfString(stream));
        }
    }

    @Test
    void loadResourceFromZipFile110() throws IOException {
        try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip",
                "directory/some_resource_inside_zip.txt")) {
            assertEquals("ABCD - 2", getUtfString(stream));
        }
    }

    @Test
    void loadResourceFromZipFile120() throws IOException {
        try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                "resources/zipfiles/resource_zip_file.zip",
                "directory/subdirectory/some_resource_inside_zip.txt")) {
            assertEquals("ABCD - 3", getUtfString(stream));
        }
    }

    @Test
    void loadResourceFromZipFile130() throws IOException {
        for (int i = 0; i < 10000; i++) {
            try (InputStream stream = CommonUtils.loadResourceFromZipFile(getClass(),
                    "resources/zipfiles/resource_zip_file.zip",
                    "directory/subdirectory/some_resource_inside_zip.txt")) {
                assertEquals("ABCD - 3", getUtfString(stream));
            }
        }
    }



    /* ------------------------------------------- expandListToSize ----------------------------------------------- */


    @Test
    @Expected(expected = NullPointerException.class)
    void expandListToSize() {
        CommonUtils.expandListToSize(null, 10);
    }

    @Test
    void expandListToSize10() {
        /* обычное выполнение  - проверка что ничего не рухнет*/
        CommonUtils.expandListToSize(new ArrayList<>(), 1);
    }

    @Test
    void expandListToSize30() {
        ArrayList<Object> list = new ArrayList<>();
        CommonUtils.expandListToSize(list, 10);
        assertEquals(10, list.size());
    }

    @Test
    void getValueFunction31() {
        /* передаются лажовые данные - но в виду того что используется предикат,
        который всегда возвращает константу мы получаем константу */

        assertEquals(
                "100000",
                CommonUtils.getValueFunction("lkdjfa;kdjf;a;dfadfj;adfj;a", s -> "100000")
        );
    }


    /* ------------------------------------------- getValueFunction ------------------------------------------------------- */

    @Test
    void getValueFunction40() {
        assertEquals(new Integer("12"), CommonUtils.getValueFunction("array[12]", TESTES_ORDER_FUNCTION));
    }

    @Test
    void getValueFunction41() {
        assertEquals("Step", CommonUtils.getValueFunction("array[Step][12]", TESTES_ORDER_FUNCTION_2));
    }

    @Test
    void getValueFunction50() {
        assertEquals(null, CommonUtils.getValueFunction("array[s1]", TESTES_ORDER_FUNCTION));
    }

    @Test
    void getValueFunction51() {
        assertEquals("2", CommonUtils.getValueFunction("array[2][s1]", TESTES_ORDER_FUNCTION_2));
    }

    @Test
    void getValueFunction60() {
        assertEquals(null, CommonUtils.getValueFunction("not_an_array[1111]", TESTES_ORDER_FUNCTION));
    }

    @Test
    void getValueFunction61() {
        assertEquals("step", CommonUtils.getValueFunction("not_an_array[step][1111]", TESTES_ORDER_FUNCTION_2));
    }

    @Test
    void getValueFunction70() {
        assertEquals(null, CommonUtils.getValueFunction("array[11111111111111111111111111111]", TESTES_ORDER_FUNCTION));
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

    @Test
    @Expected(expected = NullPointerException.class)
    void measureTime10() throws Throwable {
        CommonUtils.measureTime(null);
    }

    @Test
    void measureTime20() throws Throwable {
        assertEquals(true, around(CommonUtils.measureTime(() -> Thread.sleep(1000L)),
                1000L, 30));
    }

    @Test
    void measureTime30() throws Throwable {
        long l = new Random().nextInt(1000) + 1000;
        assertEquals(true, around(CommonUtils.measureTime(() -> Thread.sleep(l)), l, 300));
    }

        /*  ----------------------------------------- compileString ---------------------------------------------------  */

    @Test
    @Expected(expected = NullPointerException.class)
    void compileString_error1() {
        CommonUtils.compileString(null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void compileString_error2() {
        CommonUtils.compileString(null, null, "");
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void compileString_error3() {
        CommonUtils.compileString(null, "one");
    }

    @Test
    void compileString10() {
        assertEquals("one_two_three", CommonUtils.compileString("one", "_", "two", "_", "three"));
    }

    @Test
    void compileString20() {
        Object[] objects = {"_", "two", "_", "three"};
        assertEquals("one_two_three", CommonUtils.compileString("one", objects));
    }

    @Test
    void compileString30() {
        assertEquals("one_two_three", CommonUtils.compileString("one", "_", "two", "_", "three"));
    }

    @Test
    void compileString40() {
        assertEquals("two_three", CommonUtils.compileString("", "two", "_", "three"));
    }


/* --------------------------------------------------- checkDefined ------------------------------------------------*/

    @Test
    @Expected(expected = IllegalArgumentException.class)
    void checkDefined10() {
        CommonUtils.checkDefined(null);
    }

    @Test
    void checkDefined20() {
        CommonUtils.checkDefined("");
    }

    @Test
    void checkDefined30() {
        assertEquals("15", CommonUtils.checkDefined("15"));
    }

    @Test
    void checkDefined40() {
        CommonUtils.checkDefined("15", "param is not defined");
    }

/* --------------------------------------------------- skipAllExceptions ---------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void skipAllExceptions10() {
        CommonUtils.skipAllExceptions(null);
    }

    @Test
    void skipAllExceptions20() {
        CommonUtils.skipAllExceptions(() -> {
            throw new Throwable();
        });
    }


/* --------------------------------------------------- skipExceptions ------------------------------------------*/

    @Test
    void skipExceptions10() {
        CommonUtils.skipExceptions(() -> {
            throw new FileNotFoundException();
        });
    }

    @Test
    void skipExceptions14() {
        CommonUtils.skipExceptions(() -> {
            @SuppressWarnings("UnusedDeclaration")
            String s = "" + "121312";
        });
    }

    @Test
    void skipExceptions20() {
        assertEquals(CommonUtils.skipExceptions(() -> "11", "DefaultValue"), "11");
    }

    @Test
    void skipExceptions30() {
        assertEquals(CommonUtils.skipExceptions(() -> {
            throw new FileNotFoundException();
        }, "DefaultValue"), "DefaultValue");
    }

    @Test
    void skipExceptions40() {
        String[] strings = {""};
        CommonUtils.skipExceptions(
                (CommonUtils.Code) () -> {
                    throw new FileNotFoundException();
                },
                () -> {
                    strings[0] = "OK";
                });

        assertEquals("OK", strings[0]);
    }

    @Test
    void skipExceptions50() {
        String[] strings = {""};
        CommonUtils.skipExceptions(
                (CommonUtils.Code) () -> {
                    throw new FileNotFoundException();
                },
                (Runnable) null);

        assertEquals("", strings[0]);
    }

    @Test
    void skipExceptions60() {

        int i = CommonUtils.skipExceptions(
                (CommonUtils.SupressedExceptionSupplier<Integer>) () -> {
                    throw new Exception();
                },
                (Function<Exception, Integer>) e -> 0
        );
        assertEquals(i, 0);
    }

    @Test
    void skipExceptions70() {
        int i = CommonUtils.skipExceptions(
                (CommonUtils.SupressedExceptionSupplier<Integer>) () -> 1,
                (Function<Exception, Integer>) e -> 0
        );
        assertEquals(i, 1);
    }

    @Test
    void skipExceptions80() {
        int i = CommonUtils.skipExceptions(
                (CommonUtils.SupressedExceptionSupplier<Integer>) () -> 1,
                (Function<Exception, Integer>) e -> 0
        );
        assertEquals(i, 1);
    }

/* --------------------------------------------------- ensure ------------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void ensure10() {
        CommonUtils.ensure("1000", null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void ensure20() {
        CommonUtils.ensure(null, () -> null);
    }

    @Test
    void ensure30() {
        assertEquals("1000", CommonUtils.ensure("1000", () -> null));
    }

    @Test
    void ensure40() {
        assertEquals("100", CommonUtils.ensure(null, () -> "100"));
    }


    @Test
    @Expected(expected = NullPointerException.class)
    void copyStreamContent10() throws IOException {
        CommonUtils.copyStreamContent(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void copyStreamContent20() throws IOException {
        CommonUtils.copyStreamContent(new ByteArrayInputStream(new byte[]{}), null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void copyStreamContent30() throws IOException {
        CommonUtils.copyStreamContent(null, new ByteArrayOutputStream());
    }

    @Test
    void copyStreamContent40() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (FileInputStream inputStream = new FileInputStream(CommonUtils.getAbsoluteResourcePath(getClass(),
                "resources/text/resource_file_1.txt"))) {
            CommonUtils.copyStreamContent(inputStream, outputStream);
        }

        try (FileInputStream inputStream = new FileInputStream(CommonUtils.getAbsoluteResourcePath(getClass(),
                "resources/text/resource_file_1.txt"))) {
            assertEquals(true, CommonUtils.compareStreams(inputStream,
                    new ByteArrayInputStream(outputStream.toByteArray())));
        }
    }

    /* ------------------------------------------------------- suppress ------------------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void suppress10() {
        CommonUtils.suppress(null);
    }

    @Test
    void suppress20() {
        CommonUtils.suppress(() -> 20003);
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void suppress30() {
        CommonUtils.suppress(() -> {
            throw new IllegalStateException();
        });
    }

    @Test
    @Expected(expected = NumberFormatException.class)
    void suppress40() {
        CommonUtils.suppress(() -> {
            throw new NumberFormatException();
        });
    }

    @Test
    @Expected(expected = RuntimeException.class)
    void suppress50() {
        CommonUtils.suppress(() -> {
            throw new Exception();
        });
    }

    /* --------- suppress(SupressedExceptionSupplier<T> suppressedCode, Class<T> type) ---------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void suppress100() {
        CommonUtils.suppress(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void suppress110() {
        CommonUtils.suppress(() -> true, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void suppress120() {
        CommonUtils.suppress(null, String.class);
    }

    @Test
    void suppress130() {
        CommonUtils.suppress(() -> 20003, Integer.class);
    }

    private static int retIntException() throws Exception {
        throw new Exception();
    }

    private static int retIntNumberFormatException() {
        throw new NumberFormatException();
    }

    @Test
    @Expected(expected = RuntimeException.class)
    void suppress140() {
        CommonUtils.suppress(CommonUtilsCheck::retIntException, Integer.TYPE);
    }

    @Test
    @Expected(expected = NumberFormatException.class)
    void suppress150() {
        CommonUtils.suppress(CommonUtilsCheck::retIntNumberFormatException, Integer.TYPE);
    }

/* ------------------------------------------------------- suppressAll  --------------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void suppressAll10() {
        CommonUtils.suppressAll(null);
    }

    @Test
    void suppressAll20() {
        CommonUtils.suppressAll(() -> 100);
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void suppressAll30() {
        CommonUtils.suppressAll(() -> {
            throw new IllegalStateException();
        });
    }

    @Test
    @Expected(expected = NumberFormatException.class)
    void suppressAll40() {
        CommonUtils.suppress(() -> {
            throw new NumberFormatException();
        });
    }

    @Test
    @Expected(expected = RuntimeException.class)
    void suppressAll50() {
        CommonUtils.suppress(() -> {
            throw new Exception();
        });
    }

    @Test
    @Expected(expected = RuntimeException.class)
    void suppressAll60() {
        CommonUtils.suppressAll(() -> {
            throw new Throwable();
        });
    }

    /* --------------------------------------------------- suppressWOResult ----------------------------------------*/


    @Test
    @Expected(expected = NullPointerException.class)
    void suppressWOResult10() {
        CommonUtils.suppressWOResult(null);
    }

    @Test
    @Expected(expected = RuntimeException.class)
    void suppressWOResult20() {
        CommonUtils.suppressWOResult(() -> {
            throw new RuntimeException();
        });
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void suppressWOResult30() {
        CommonUtils.suppressWOResult(() -> {
            throw new IllegalStateException();
        });
    }

    @Test
    @Expected(expected = RuntimeException.class)
    void suppressWOResult40() {
        CommonUtils.suppressWOResult(() -> {
            throw new IOException();
        });
    }



    /* --------------------------------------------------- arrayToList ---------------------------------------------*/


    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    @Test
    @Expected(expected = NullPointerException.class)
    void arrayToList10() {
        CommonUtils.arrayToList(null);
    }

    @Test
    void arrayToList20() {
        List<String> list = CommonUtils.arrayToList("en todos lados se cuecen habas!!!");
        list.add("112");
    }

    @Test
    void arrayToList30() {
        assertEquals(Arrays.asList("1", "2", "3"), CommonUtils.arrayToList("1", "2", "3"));
    }

    @Test
    void arrayToList40() {
        assertEquals(Arrays.asList("1", null, "3"), CommonUtils.arrayToList("1", null, "3"));
    }

    /* ------------------------------------------------------- reduceUnique --------------------------------------- */


    @Test
    @Expected(expected = IllegalArgumentException.class)
    void reduceUnique10() {
        CommonUtils.reduceUnique(null, null);
    }

    @Test
    @Expected(expected = IllegalArgumentException.class)
    void reduceUnique20() {
        CommonUtils.reduceUnique("12345678", "2131231");
    }

    @Test
    void reduceUnique30() {
        assertEquals("12345678", CommonUtils.reduceUnique("12345678", null));
    }

    @Test
    @Expected(expected = IllegalArgumentException.class)
    void reduceUnique40() {
        Stream.of("1", "1", "1", "1", "1", "1", "2").reduce(null, CommonUtils::reduceUnique);
    }

    @Test
    void reduceUnique50() {
        assertEquals("1",
                Stream.of("1", "1", "1", "1", "1", "1", "1").reduce(null, CommonUtils::reduceUnique));
    }


    /* ------------------------------------------------------- intersectCollections ------------------------------ */


    @Test
    @Expected(expected = NullPointerException.class)
    void intersectCollections10() {
        CommonUtils.intersectCollections(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void intersectCollections20() {
        CommonUtils.intersectCollections(null, Arrays.asList("1", "2"));
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void intersectCollections30() {
        CommonUtils.intersectCollections(Arrays.asList("1", "2"), null);
    }

    @Test
    void intersectCollections40() {
        List<String> strings = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList("1", "2"),
                Arrays.asList("1", "2")));
        strings.sort(COMPARE_STRINGS);
        List<String> expected = new ArrayList<>(Arrays.asList("1", "2"));
        expected.sort(COMPARE_STRINGS);
        assertEquals(expected, strings);
    }

    @Test
    void intersectCollections50() {
        List<String> strings = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList("1", "2", "3", "4"),
                Arrays.asList("1", "2", "3")));
        strings.sort(COMPARE_STRINGS);
        List<String> expected = new ArrayList<>(Arrays.asList("1", "2", "3"));
        expected.sort(COMPARE_STRINGS);
        assertEquals(expected, strings);
    }

    @Test
    void intersectCollections60() {
        List<String> strings = new ArrayList<>(CommonUtils.intersectCollections());
        strings.sort(COMPARE_STRINGS);
        assertEquals(new ArrayList<>(), strings);
    }

    @Test
    void intersectCollections70() {
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
        assertEquals(expected, strings);
    }

    @Test
    void intersectCollections80() {
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
        assertEquals(expected, ints);
    }

    @Test
    void intersectCollections90() {
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
        assertEquals(Arrays.asList(null, 5), ints);
    }

    @Test
    void intersectCollections100() {
        List<Integer> ints = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList(2, 5, 5, 5, 5)));
        ints.sort(COMPARE_INTEGERS);
        assertEquals(Arrays.asList(2, 5), ints);
    }

    @Test
    void intersectCollections110() {
        List<Integer> ints = new ArrayList<>(CommonUtils.intersectCollections(Arrays.asList(2, null, 5, 5, 5)));
        ints.sort(COMPARE_INTEGERS);
        assertEquals(Arrays.asList(null, 2, 5), ints);
    }

    /* ------------------------------------------------------ eval ---------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void eval10() {
        CommonUtils.eval(null);
    }

    @Test
    void eval20() {
        assertEquals(Arrays.asList("1", "2", "3"),
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


    @Test
    @Expected(expected = NullPointerException.class)
    void suppressAllWOResult10() {
        CommonUtils.suppressAllWOResult(null);
    }


    @Test
    @Expected(expected = RuntimeException.class)
    void suppressAllWOResult20() {
        CommonUtils.suppressAllWOResult(() -> {
            throw new IOException();
        });
    }

    @Test
    @Expected(expected = RuntimeException.class)
    void suppressAllWOResult30() {
        CommonUtils.suppressAllWOResult(() -> {
            throw new Throwable();
        });
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void suppressAllWOResult40() {
        CommonUtils.suppressAllWOResult(() -> {
            throw new IllegalStateException();
        });
    }



   /* ------------------------------------------------------- check ------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void check10() {
        CommonUtils.check(null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void check20() {
        CommonUtils.check(() -> "a".equals("a"), null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void check30() {
        CommonUtils.check(null, IllegalStateException::new);
    }

    private static class Exc extends RuntimeException {

    }

    @Test
    @Expected(expected = Exc.class)
    void check40() {
        CommonUtils.check(() -> false, Exc::new);
    }

    @Test
    void check50() {
        CommonUtils.check(() -> true, Exc::new);
    }

   /* ------------------------------------------------------- check2 ------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void check100() {
        CommonUtils.check(null, null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void check110() {
        CommonUtils.check(() -> "a".equals("a"), null, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void check120() {
        CommonUtils.check(() -> "a", s -> true, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void check130() {
        CommonUtils.check(null, s -> true, Exc::new);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void check140() {
        CommonUtils.check(() -> "a", null, Exc::new);
    }

    @Test
    @Expected(expected = Exc.class)
    void check150() {
        CommonUtils.check(() -> "a", s -> false, Exc::new);
    }

    @Test
    @Expected(expected = Exc.class)
    void check160() {
        CommonUtils.check(() -> "a", s -> false, Exc::new);
    }

    @Test
    void check170() {
        CommonUtils.check(() -> "a", s -> s.equals("a"), Exc::new);
    }

    @Test
    @Expected(expected = Exc.class)
    void check180() {
        CommonUtils.check(() -> "a", s -> s.equals("c"), Exc::new);
    }

   /* ------------------------------------------------------- stackTrace ------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void stackTrace10() {
        CommonUtils.stackTrace(null);
    }

    @Test
    void stackTrace20() {
        String stackTrace = CommonUtils.stackTrace(new Throwable());
        assertTrue(new BufferedReader(new StringReader(stackTrace)).lines().
                filter(s -> !s.contains("java.lang.Throwable")).
                allMatch(s1 -> s1.trim().startsWith("at")));
        assertTrue(new BufferedReader(new StringReader(stackTrace)).lines().
                anyMatch(s1 -> s1.contains("CommonUtilsCheck.stackTrace20")));
    }



   /* ------------------------------------------------------- skipErrors ------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void skipErrors10() {
        CommonUtils.skipErrors(null, null);
    }

    @Test
    void skipErrors20() {
        CommonUtils.skipErrors(() -> {
        }, null);
    }

    @Test
    @Expected(expected = NullPointerException.class)
    void skipErrors30() {
        CommonUtils.skipErrors(null, t -> {
        });
    }

    @Test
    void skipErrors40() {
        AtomicReference<Throwable> reference = new AtomicReference<>();
        CommonUtils.skipErrors(() -> {
            throw new IllegalStateException();
        }, reference::set);
        assertTrue(reference.get() instanceof IllegalStateException);
    }

   /* ------------------------------------------------------- privates ------------------------------------------- */

    @Test
    @Expected(expected = NullPointerException.class)
    void setCombinations10() {
        CommonUtils.setCombinations(null);
    }

    private enum Empty {
    }

    @Test
    void setCombinations20() {
        assertEquals(new HashSet<>(Collections.singletonList(new HashSet<>())), combinations(Empty.class));
    }

    private enum OneElement {
        A
    }

    @Test
    void setCombinations30() {
        assertEquals(new HashSet<>(Arrays.asList(
                new HashSet<>(),
                new HashSet<>(Arrays.asList(OneElement.A))
        )), combinations(OneElement.class));
    }


    private enum TwoOneElements {
        A, B
    }

    @Test
    void setCombinations40() {
        assertEquals(new HashSet<>(Arrays.asList(
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
    void setCombinations50() {
        assertEquals(new HashSet<>(Arrays.asList(
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
    void setCombinations60() {
        assertEquals(new HashSet<>(Arrays.asList(
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


   /* ---------------------------------------------------- array -----------------------------------------------------*/

    @Test
    @Expected(expected = NullPointerException.class)
    void array10() {
        CommonUtils.toArray((Object[]) null);
    }

    @Test
    void array20() {
        assertArrayEquals(new String[]{"1", "2"}, CommonUtils.toArray("1", "2"));
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
      

