package com.steammachine.common.utils.enumerations;

import com.steammachine.common.lazyeval.LazyEval;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Вспомогательный класс для сравнения двух перечислений
 * При сравнении проверяется совпаденине количества элементов и их названий
 * <p>
 * <p>
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 */
public class EnumComparisonUtils {

    private static final String ENUMERATIONS_ARE_NOT_EQUAL = "enumerations are not equal";

    /**
     * Вспомогательный интерфейс для сравнения двух элементов разлчных типов
     *
     * @param <T1>
     * @param <T2>
     */
    public interface EntityComparator<T1, T2> {
        /**
         * @param t1 -
         * @param t2 -
         * @return true если элементы считаются равными
         */
        boolean areEqual(T1 t1, T2 t2);
    }

    /**
     * проверяет несколько перечислений на совпадение имен и количества элементов
     *
     * @param enumeration - перечисления
     * @return истинно только в том случае если все элементы одного перечисления совпадают с элементами другого
     * перечисления (количество элементов и их наименования)
     */
    public static boolean compareEnums(Class<? extends Enum>[] enumeration) {
        if (enumeration == null) {
            throw new NullPointerException("enumeration is null");
        }

        for (Class<? extends Enum> aClass : enumeration) {
            if (aClass == null) {
                throw new NullPointerException("one of passed classes in null " + Arrays.asList(enumeration));
            }
        }

        /* тут сравнивается все перечисления со всеми */
        for (Class<? extends Enum> enumClass1 : enumeration) {
            for (Class<? extends Enum> enumClass2 : enumeration) {
                if (!compareEnumClasses(enumClass1, enumClass2, null)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Метод производит проверку соответствия нескольких перечислений на предмет полного соотвествия имен элементов и
     * их количества. При проверке порядок следования элементов не учитывается.
     * @param enumerations - массив классов перечислений (не null).
     * @exception  IllegalStateException в случае неравенства перечислений.
     */
    public static void checkIfEnumsAreEqual(Class<? extends Enum>[] enumerations) {
        if (!compareEnums(enumerations)) {
            throw new IllegalStateException(ENUMERATIONS_ARE_NOT_EQUAL + " " + Arrays.toString(enumerations));
        }
    }

    /**
     * Метод производит проверку соответствия нескольких перечислений на предмет полного соотвествия имен элементов и
     * их количества. При проверке порядок следования элементов не учитывается.
     * @param enumerations - массив классов перечислений.
     * @exception  IllegalStateException в случае неравенства перечислений.
     */
    @SafeVarargs
    public static void checkIfEnumsEqual(Class<? extends Enum>... enumerations) {
        if (!compareEnums(enumerations)) {
            throw new IllegalStateException(ENUMERATIONS_ARE_NOT_EQUAL + " " + Arrays.toString(enumerations));
        }
    }

    /**
     * Проверить, что имена перечислений одинаковые
     *
     * @param enums - перечисления
     */
    public static void assertEnumElements(Enum<?>... enums) {
        if (!compareEnumElements(enums)) {
            throw new IllegalStateException("" + Arrays.toString(enums));
        }
    }

    /**
     * Производит "глубокое" сравнение элементов двух перечислений различного типа.
     * Элементы предварительно проверяются на равенство наименований всех элементов и на соответствие их количества.
     * Затем, если условие выполнено, производится сравнение элементов с использованием переданного comparator
     *
     * @param e1 класс перечисления 1 (не null)
     * @param e2 класс перечисления 2 (не null)
     * @param comparator компаратор сравнения элементов перечислений (не null)
     * @return {@code true} если перечисления считаются равными {@code false} если перечисления различаются
     */
    public static <T1 extends Enum, T2 extends Enum> boolean deepIfEnumsAreEqual(
            Class<T1> e1,
            Class<T2> e2,
            EntityComparator<T1, T2> comparator) {
        return compareEnumClasses(e1, e2, comparator);
    }

    /**
     * Производит "глубокое" сравнение элементов двух перечислений различного типа.
     * Элементы предварительно проверяются на равенство наименований всех элементов и на соответствие их количества.
     * Затем, если условие выполнено, производится сравнение элементов с использованием переданного comparator
     *
     * Метод выбрасывает исключение {@link java.lang.IllegalStateException} в случае неравенства перечислений
     *
     *
     * @param e1 класс перечисления 1 (не null)
     * @param e2 класс перечисления 2 (не null)
     * @param comparator компаратор сравнения элементов перечислений (не null)
     */
    public static <T1 extends Enum, T2 extends Enum> void deepCheckIfEnumsAreEqual(
            Class<T1> e1,
            Class<T2> e2,
            EntityComparator<T1, T2> comparator) {
        if (!deepIfEnumsAreEqual(e1, e2, comparator)) {
            throw new IllegalStateException();
        }
    }


    /**
     * Сравнить элементы перечислений на соответствие имен
     *
     * @param enums - перечисления
     * @return -
     */
    public static boolean compareEnumElements(Enum<?>... enums) {
        if (enums == null) {
            throw new NullPointerException("enums is null");
        }
        if (enums.length == 0 || enums.length == 1) {
            return true;
        }

        Enum<?> elementZero = enums[0];
        for (Enum<?> anEnum : enums) {
            if (elementZero == null && anEnum == null) {
                //noinspection UnnecessaryContinue
                continue;
            }
            if ((elementZero == null) != (anEnum == null)) {
                //noinspection UnnecessaryContinue
                continue;
            }

            if (!Objects.equals(elementZero.name(), anEnum.name())) {
                return false;
            }
        }
        return true;
    }

    /**
     * проверить есть ли в передаваемом классе перечисления enumClass элемент с именем enumName
     *
     * @param enumName  - имя элемента (должно быть не null)
     * @param enumClass - класс перечисления(должно быть не null)
     * @return true если элемент есть
     */
    public static boolean enumHasName(
            String enumName,
            Class<? extends Enum> enumClass) {
        if (enumName == null) {
            throw new NullPointerException("enumName is null");
        }
        if (enumClass == null) {
            throw new NullPointerException("enumClass is null");
        }

        for (Enum anEnum : enumClass.getEnumConstants()) {
            if (enumName.equals(anEnum.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * проверить что в передаемом классе перечисления enumClass есть элемент с наименованием
     * передаваемого элемента (enumElement.name())
     *
     * @param enumElement     - эдемент любого перечисления(должно быть не null)
     * @param enumClass-класс перечисления(должно быть не null)
     * @return true если элемент есть
     */
    public static boolean includedByEnum(
            Enum<?> enumElement,
            Class<? extends Enum> enumClass) {
        if (enumElement == null) {
            throw new NullPointerException("enumElement is null");
        }
        return enumHasName(enumElement.name(), enumClass);
    }


    /**
     * Ищет в перечислении enumClass элемент с именем enumElement.
     * Если такой не находится выбрасывается исключение {@link java.lang.IllegalStateException}
     *
     * @param enumElement -
     * @param enumClass   -
     * @param <T>         -
     * @return Элемент enumClass
     */
    public static <T extends Enum> T getEnumItem(
            Enum<?> enumElement,
            Class<T> enumClass) {
        if (enumElement == null) {
            throw new NullPointerException("enumElement is null");
        }
        if (enumClass == null) {
            throw new NullPointerException("enumClass is null");
        }

        for (T item : enumClass.getEnumConstants()) {
            if (item.name().equals(enumElement.name())) {
                return item;
            }
        }
        throw new IllegalStateException("element  " + enumElement + " not found in " + enumClass);
    }




    /* ----------------------------------------- privates -------------------------------------------------------  */

    /**
     * проверить на равенство два перечисления
     *
     * @return true  в том случае если количество элементов равно и наименования всех элементов присутствуют в обоих
     * перечислениях.
     */
    static boolean doCompareEnums(
            Class<? extends Enum> e1,
            Class<? extends Enum> e2) {

        if (e1 == null) {
            throw new NullPointerException("e1 is null");
        }
        if (e2 == null) {
            throw new NullPointerException("e2 is null");
        }
        return compareEnumClasses(e1, e2, null);
    }

    /**
     * проверить что перечисление contained содержится в containing
     *
     * @param contained  элементы перечисления вхождение которых ищется.
     * @param containing элементы перечисления в которых ищется вхождение.
     * @return true если все элементы перечисления contained содержатся в перечислении containing
     */
    private static boolean containsAll(Enum[] contained, Enum[] containing) {
        return Stream.of(containing).map(Enum::name).collect(Collectors.toList()).
                containsAll(Stream.of(contained).map(Enum::name).collect(Collectors.toList()));
    }

    /**
     * @deprecated - no longer used
     */
    @Deprecated
    private static <T1 extends Enum, T2 extends Enum> boolean oldCompareEnumClasses(
            Class<T1> e1,
            Class<T2> e2,
            EntityComparator<T1, T2> comparator) {
        if (e1 == e2) {
            /* один и тот же класс  */
            return true;
        }

        T1[] enumConstants1 = e1.getEnumConstants();
        T2[] enumConstants2 = e2.getEnumConstants();
        if (enumConstants1.length != enumConstants2.length) {
            /* количество элементов перечисления не равно - считаем что перечисления не равны */
            return false;
        }

        if (enumConstants1.length == 0) {
            return true;
        }

        if (!containsAll(enumConstants1, enumConstants2)) {
            return false;
        }

        if (!containsAll(enumConstants2, enumConstants1)) {
            return false;
        }

        if (comparator == null) {
            return true;
        }

        /* на этот момент элементы содержатся в обоих перечислениях */

        for (T1 t1 : enumConstants1) {
            for (T2 t2 : enumConstants2) {
                if (t2.name().equals(t1.name())) {
                    if (!comparator.areEqual(t1, t2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static <T1 extends Enum, T2 extends Enum> boolean compareEnumClasses(
            Class<T1> e1,
            Class<T2> e2,
            EntityComparator<T1, T2> comparator) {
        if (e1 == e2) {
            /* один и тот же класс  */
            return true;
        }

        T1[] enumConstants1 = e1.getEnumConstants();
        T2[] enumConstants2 = e2.getEnumConstants();
        if (enumConstants1.length != enumConstants2.length) {
            /* количество элементов перечисления не равно - считаем что перечисления не равны */
            return false;
        }

        if (enumConstants1.length == 0) {
            return true;
        }

        if (!containsAll(enumConstants1, enumConstants2)) {
            return false;
        }

        if (!containsAll(enumConstants2, enumConstants1)) {
            return false;
        }

        if (comparator == null) {
            return true;
        }

        /* на этот момент элементы содержатся в обоих перечислениях */

        for (T1 t1 : enumConstants1) {
            for (T2 t2 : enumConstants2) {
                if (t2.name().equals(t1.name())) {
                    if (!comparator.areEqual(t1, t2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     * Производит сравнение ординалов {@link Enum#ordinal()} перечислений
     *
     * @param enumerations перечисления (not null)
     */
    @SafeVarargs
    public static boolean compareOrdinals(Class<? extends Enum> ... enumerations) {
        List<Map<String, Integer>> list = Stream.of(enumerations).
                map(c -> Stream.of(c.getEnumConstants()).collect(Collectors.toMap(Enum::name, Enum::ordinal))).
                collect(Collectors.toList());

        if (list.size() == 0 || list.size() == 1) return true;

        Map<String, Integer> first = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            Map<String, Integer> current = list.get(i);
            for (Map.Entry<String, Integer> entry : first.entrySet()) {
                if (!Objects.equals(entry.getValue(), current.get(entry.getKey()))) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Производит проверку ординалов перечислений {@link Enum#ordinal()}
     *
     * @param enumerations перечисления (not null)
     */
    @SafeVarargs
    public static void checkIfEnumOrdinalsEqual(Class<? extends Enum>... enumerations) {
        LazyEval<String> enumerationNames =
                LazyEval.eval(() -> Stream.of(enumerations).map(Class::getName).collect(Collectors.joining(", ")));

        if (!compareEnums(enumerations)) {
            throw new IllegalStateException(ENUMERATIONS_ARE_NOT_EQUAL + " " + enumerationNames.value());
        }

        if (!compareOrdinals(enumerations)) {
            throw new IllegalStateException("enumerations " + enumerationNames.value() +
                    " contain different ordinals");
        }
    }



}
