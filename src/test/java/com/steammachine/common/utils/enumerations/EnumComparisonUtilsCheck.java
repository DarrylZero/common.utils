package com.steammachine.common.utils.enumerations;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */
@SuppressWarnings("unchecked")
public class EnumComparisonUtilsCheck {

    private enum One {
        A,
        B,
        C
    }

    private enum OneAndHalf {
        A,
        B,
        C,
        D
    }

    private enum Two {
        A,
        B,
        C
    }

    private enum Three {
        A,
        C,
        B
    }

    private enum Four {
        C,
        A,
        B
    }


    private enum Five {
        AB,
        C,
    }

    private enum Six {
        A("A"),
        B("B"),
        C("C");

        private final String otherName;

        Six(String otherName) {
            this.otherName = otherName;
        }
    }

    private enum Seven {
        C("C"),
        A("A"),
        B("B");
        private final String otherName;

        Seven(String otherName) {
            this.otherName = otherName;
        }
    }

    private enum Eight {
        C("CC"),
        A("AA"),
        B("BB");
        private final String otherName;

        Eight(String otherName) {
            this.otherName = otherName;
        }
    }


    private enum ZeroLength1 {
    }

    private enum ZeroLength2 {
    }


    @Test
    public void testEquality() {
        Assert.assertTrue(EnumComparisonUtils.doCompareEnums(One.class, Two.class));
    }


    @Test
    public void testNotEquality() {
        Assert.assertFalse(EnumComparisonUtils.doCompareEnums(One.class, Five.class));
    }

    @Test(expected = NullPointerException.class)
    public void testErrors1() {
        EnumComparisonUtils.compareEnums(null);
    }

    @Test(expected = NullPointerException.class)
    public void testErrors2() {
        EnumComparisonUtils.compareEnums(null);
    }

    @Test(expected = NullPointerException.class)
    public void testErrors3() {
        EnumComparisonUtils.compareEnums(new Class[]{One.class, Five.class, null});
    }

    @Test
    public void testEquality2() {
        Assert.assertFalse(EnumComparisonUtils.compareEnums(new Class[]{One.class, Five.class}));
    }

    @Test
    public void testEquality3() {
        Assert.assertTrue(EnumComparisonUtils.compareEnums(new Class[]{One.class, Two.class, Three.class, Four.class}));
    }

    @Test
    public void testEquality4() {
        Assert.assertTrue(EnumComparisonUtils.compareEnums(new Class[]{}));
    }

    @Test
    public void testEquality5() {
        Assert.assertTrue(EnumComparisonUtils.compareEnums(new Class[]{One.class}));
    }

    @Test
    public void testEquality6() {
        //noinspection unchecked
        Assert.assertTrue(EnumComparisonUtils.compareEnums(new Class[]{ZeroLength1.class, ZeroLength2.class}));
    }

    /* -------------------------------- deepIfEnumsAreEqual --------------------------------------------------- */

    @Test
    public void testDeepIfEnumsAreEqual() {
        Assert.assertEquals(true, EnumComparisonUtils.deepIfEnumsAreEqual(Six.class, Seven.class, null));
    }

    @Test
    public void testDeepIfEnumsAreEqual2() {
        Assert.assertEquals(true, EnumComparisonUtils.deepIfEnumsAreEqual(Six.class, Seven.class,
                new EnumComparisonUtils.EntityComparator<Six, Seven>() {
                    @Override
                    public boolean areEqual(Six six, Seven seven) {
                        return six.otherName == null ? seven.otherName == null : six.otherName.equals(seven.otherName);
                    }
                }));
    }

    @Test
    public void testDeepIfEnumsAreEqual3() {
        Assert.assertEquals(false, EnumComparisonUtils.deepIfEnumsAreEqual(Six.class, Eight.class,
                new EnumComparisonUtils.EntityComparator<Six, Eight>() {
                    @Override
                    public boolean areEqual(Six six, Eight eight) {
                        return six.otherName == null ? eight.otherName == null : six.otherName.equals(eight.otherName);
                    }
                }));
    }

    /* -------------------------------- deepCheckIfEnumsAreEqual --------------------------------------------------- */

    @Test
    public void testDeepCheckIfEnumsAreEqual() {
        EnumComparisonUtils.deepCheckIfEnumsAreEqual(Six.class, Seven.class, null);
    }

    @Test
    public void testDeepCheckIfEnumsAreEqual2() {
        EnumComparisonUtils.deepIfEnumsAreEqual(Six.class, Seven.class,
                new EnumComparisonUtils.EntityComparator<Six, Seven>() {
                    @Override
                    public boolean areEqual(Six six, Seven seven) {
                        return six.otherName == null ? seven.otherName == null : six.otherName.equals(seven.otherName);
                    }
                });
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void testDeepCheckIfEnumsAreEqual3() {
/*
        Lambda пока что не поддерживается той штукой которая собрает  имена классов по классовому пути
        EnumComparisonUtils.deepCheckIfEnumsAreEqual(Six.class, Eight.class,
                (six, eight) -> six.otherName == null ? eight.otherName == null : six.otherName.equals(eight.otherName));
*/
        EnumComparisonUtils.deepCheckIfEnumsAreEqual(Six.class, Eight.class,
                new EnumComparisonUtils.EntityComparator<Six, Eight>() {
                    @Override
                    public boolean areEqual(Six six, Eight eight) {
                        return six.otherName == null ? eight.otherName == null : six.otherName.equals(eight.otherName);
                    }
                });
    }

    /* ----------------------------------- compareEnumElements ----------------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void compareEnumElements_error1() {
        Enum[] enums = null;
        EnumComparisonUtils.compareEnumElements(enums);
    }

    @Test
    public void compareEnumElements1() {
        Assert.assertEquals(EnumComparisonUtils.compareEnumElements(null, null), true);
    }

    @Test
    public void compareEnumElements2() {
        Assert.assertEquals(EnumComparisonUtils.compareEnumElements(), true);
    }

    @Test
    public void compareEnumElements3() {
        Assert.assertEquals(EnumComparisonUtils.compareEnumElements(Three.A, Four.A, Six.A, Seven.A), true);
    }

    @Test
    public void compareEnumElements4() {
        Assert.assertEquals(EnumComparisonUtils.compareEnumElements(Three.B, Four.A, Six.A, Seven.A), false);
    }

    /* ------------------------------ assertEnumElements ------------------------------------------------------------*/

    @Test
    public void assertEnumElements1() {
        EnumComparisonUtils.assertEnumElements(null, null);
    }

    @Test
    public void assertEnumElements2() {
        EnumComparisonUtils.assertEnumElements();
    }

    @Test
    public void assertEnumElements3() {
        EnumComparisonUtils.assertEnumElements(Three.A, Four.A, Six.A, Seven.A);
    }

    @Test(expected = IllegalStateException.class)
    public void assertEnumElements4() {
        EnumComparisonUtils.assertEnumElements(Three.B, Four.A, Six.A, Seven.A);
    }

    /* ----------------------------------------------enumHasName --------------------------------------------------*/


    @Test(expected = NullPointerException.class)
    public void enumHasName_error() {
        EnumComparisonUtils.enumHasName(null, null);
    }


    @Test(expected = NullPointerException.class)
    public void enumHasName_error2() {
        EnumComparisonUtils.enumHasName(null, One.class);
    }


    @Test(expected = NullPointerException.class)
    public void enumHasName_error3() {
        EnumComparisonUtils.enumHasName("", null);
    }

    @Test
    public void enumHasName1() {
        Assert.assertTrue(EnumComparisonUtils.enumHasName("A", One.class));
        Assert.assertTrue(EnumComparisonUtils.enumHasName("B", One.class));
        Assert.assertTrue(EnumComparisonUtils.enumHasName("C", One.class));
        Assert.assertFalse(EnumComparisonUtils.enumHasName("DDF", One.class));
    }

    /* ------------------------------------------ includedByEnum ---------------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void includedByEnum_error() {
        EnumComparisonUtils.includedByEnum(null, null);
    }


    @Test(expected = NullPointerException.class)
    public void includedByEnum_error2() {
        EnumComparisonUtils.includedByEnum(null, One.class);
    }


    @Test(expected = NullPointerException.class)
    public void includedByEnum_error3() {
        EnumComparisonUtils.includedByEnum(One.A, null);
    }

    @Test
    public void includedByEnum1() {
        Assert.assertTrue(EnumComparisonUtils.includedByEnum(OneAndHalf.A, One.class));
        Assert.assertTrue(EnumComparisonUtils.includedByEnum(OneAndHalf.A, One.class));
        Assert.assertTrue(EnumComparisonUtils.includedByEnum(OneAndHalf.A, One.class));
        Assert.assertFalse(EnumComparisonUtils.includedByEnum(OneAndHalf.D, One.class));
    }

    /* ------------------------------------------ getEnumItem --------------------------------------------------- */

    @Test(expected = NullPointerException.class)
    public void findEnumElement_error() {
        EnumComparisonUtils.getEnumItem(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void findEnumElement_error2() {
        EnumComparisonUtils.getEnumItem(OneAndHalf.D, null);
    }

    @Test(expected = NullPointerException.class)
    public void findEnumElement_error3() {
        EnumComparisonUtils.getEnumItem(null, OneAndHalf.class);
    }

    @Test(expected = IllegalStateException.class)
    public void findEnumElement_error4() {
        EnumComparisonUtils.getEnumItem(OneAndHalf.D, One.class);
    }

    @Test
    public void findEnumElement_1() {
        Assert.assertEquals(OneAndHalf.A, EnumComparisonUtils.getEnumItem(One.A, OneAndHalf.class));
        Assert.assertEquals(OneAndHalf.C, EnumComparisonUtils.getEnumItem(One.C, OneAndHalf.class));
    }


    /**
     * пример использования
     */
    public void useFindEnumElementExample() {
        switch (EnumComparisonUtils.getEnumItem(One.A, OneAndHalf.class)) {
            case A:
            case B:
            case C:
            case D: {
                break;
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }

    /* ---------------------------- compareOrdinals ------------------------------ */

    @Test(expected = NullPointerException.class)
    public void compareOrdinals10() {
        EnumComparisonUtils.compareOrdinals(null);
    }


    enum R1 {
        a,
        b,
        c
    }

    enum R2 {
        a,
        b,
        c
    }

    enum R3 {
        b,
        a,
        c
    }

    @Test
    public void compareOrdinals20() {
        Assert.assertEquals(true, EnumComparisonUtils.compareOrdinals(R1.class, R1.class));
    }

    @Test
    public void compareOrdinals30() {
        Assert.assertEquals(true, EnumComparisonUtils.compareOrdinals(R1.class, R2.class));
    }

    @Test
    public void compareOrdinals40() {
        Assert.assertEquals(false, EnumComparisonUtils.compareOrdinals(R1.class, R3.class));
    }

    /* ---------------------------- checkIfEnumOrdinalsEqual ------------------------------ */

    @Test(expected = NullPointerException.class)
    public void checkIfEnumOrdinalsEqual10() {
        EnumComparisonUtils.compareOrdinals(null);
    }

    @Test
    public void checkIfEnumOrdinalsEqual20() {
        EnumComparisonUtils.checkIfEnumOrdinalsEqual(R1.class, R1.class);
    }

    @Test
    public void checkIfEnumOrdinalsEqual30() {
        EnumComparisonUtils.checkIfEnumOrdinalsEqual(R1.class, R2.class);
    }

    @Test(expected = IllegalStateException.class)
    public void checkIfEnumOrdinalsEqual40() {
        EnumComparisonUtils.checkIfEnumOrdinalsEqual(R1.class, R3.class);
    }


}
