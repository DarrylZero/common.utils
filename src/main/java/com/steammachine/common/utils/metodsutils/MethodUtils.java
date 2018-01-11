package com.steammachine.common.utils.metodsutils;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 */
@Api(State.MAINTAINED)
public class MethodUtils {

    private static final String CLASS_IS_NULL = "class is null";

    @SuppressWarnings("TryWithIdenticalCatches")
    public static class BaseMethodCaller implements MethodCaller {
        protected final Method method;

        public BaseMethodCaller(Method method) {
            this.method = Objects.requireNonNull(method, "method is null");
        }

        @Override
        public <T> T invoke(Object object, Object... param) {
            boolean accessible = method.isAccessible();
            if (!accessible) {
                method.setAccessible(true);
            }
            try {
                //noinspection unchecked
                return (T) method.invoke(object, param);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() == null) {
                    throw new IllegalStateException(e);
                } else if (e.getTargetException() instanceof RuntimeException) {
                    throw ((RuntimeException) e.getTargetException());
                } else if (e.getTargetException() instanceof Exception) {
                    throw new RuntimeException(e.getTargetException());
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                method.setAccessible(accessible);
            }

        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static class BaseConstructorCaller<T> implements ConstructorCaller<T> {
        protected final Constructor constructor;

        public BaseConstructorCaller(Constructor<T> constructor) {
            if (constructor == null) {
                throw new NullPointerException("constructor is null");
            }
            this.constructor = constructor;
        }

        @Override
        public <T> T newInstance(Object... param) {
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }

            try {
                constructor.setAccessible(true);
                //noinspection unchecked
                return (T) constructor.newInstance(param);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() == null) {
                    throw new IllegalStateException(e);
                } else if (e.getTargetException() instanceof RuntimeException) {
                    throw ((RuntimeException) e.getTargetException());
                } else if (e.getTargetException() instanceof Exception) {
                    throw new RuntimeException(e.getTargetException());
                } else {
                    throw new RuntimeException(e);
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } finally {
                constructor.setAccessible(accessible);
            }
        }
    }


    public enum Level {
        PRIVATE,
        PROTECTED,
        DEFAULT,
        PUBLIC,
    }

    private interface MemberInfo {
        int flags();

        int level();
    }

    private static class BaseMemberInfo implements MemberInfo {
        private final int flags;
        private final int level;

        private BaseMemberInfo(int flags, int level) {
            this.flags = flags;
            this.level = level;
        }

        @Override
        public int flags() {
            return flags;
        }

        @Override
        public int level() {
            return level;
        }
    }

    private static final Map<Level, MemberInfo> METHOD_LEVEL_INFO;

    static {
        Map<Level, MemberInfo> methodLevelInfo = new EnumMap<>(Level.class);
        methodLevelInfo.put(Level.PRIVATE, new BaseMemberInfo(0x0002, 40));
        methodLevelInfo.put(Level.PROTECTED, new BaseMemberInfo(0x0004, 30));
        methodLevelInfo.put(Level.DEFAULT, new BaseMemberInfo(0x0000, 20));
        methodLevelInfo.put(Level.PUBLIC, new BaseMemberInfo(0x0001, 10));

        METHOD_LEVEL_INFO = Collections.unmodifiableMap(methodLevelInfo);
    }


    public static Level getLevel(int modifiers) {
        for (Level l : Level.values()) {
            if ((METHOD_LEVEL_INFO.get(l).flags() & modifiers) != 0) {
                return l;
            }
        }
        return Level.DEFAULT;
    }

    /**
     * Находит метод класса как статический так и нет по наименованию и передаваемым параметрам
     * Поиск производится по всем методам класса и всех классов предков
     * Доступность метода указывается в level
     *
     * @param clazz          - класс, в котором производится поиск
     * @param methodSelector - предикат - селектор - который отбирает требуемый метод. Если селектор возвращает true такой метод выбирается
     * @return методы соответствующие критериям посиска если метод не найден (never {@code null})
     */
    public static List<Method> findMethods(
            Class<?> clazz,
            BiFunction<Class<?>, Method, Boolean> methodSelector) {
        Objects.requireNonNull(clazz, CLASS_IS_NULL);
        Objects.requireNonNull(methodSelector, "methodFilter is null");

        List<Method> methods = new ArrayList<>();
        Class<?> c = clazz;
        while (c != null) {
            for (Method method : c.getDeclaredMethods()) {
                if (!methodSelector.apply(clazz, method)) {
                    continue;
                }
                methods.add(method);
            }
            c = c.getSuperclass();
        }
        return methods;
    }


    /**
     * Находит метод класса как статический так и нет по наименованию и передаваемым параметрам
     * Поиск производится по всем методам класса и всех классов предков
     * Доступность метода указывается в level
     *
     * @param clazz          - класс, в котором производится поиск
     * @param methodSelector - предикат - селектор - который отбирает требуемый метод. Если селектор возвращает true такой метод выбирается
     * @return методы соответствующие критериям посиска если метод не найден (never {@code null})
     */
    public static List<Method> findMethods(Class<?> clazz, Predicate<Method> methodSelector) {
        Objects.requireNonNull(methodSelector);
        return findMethods(clazz, (aClass, method) -> methodSelector.test(method));
    }


    /**
     * Находит метод класса как статический так и нет по наименованию и передаваемым параметрам
     * Поиск производится по всем методам класса и всех классов предков
     * Доступность метода указывается в level
     *
     * @param level          - уровень доступности метода
     * @param clazz          - класс в котором производится поиск
     * @param methodName     - наименование метода
     * @param returnType     - тип возвращаемого значения (null если метод не возвращает ничего)
     * @param parameterTypes - типы параметров
     * @return найденный метод или null если метод не найден ()
     */
    public static Method findMethod(
            Level level,
            Class<?> clazz,
            String methodName,
            Class<?> returnType,
            Class<?>... parameterTypes) {

        boolean[] found = {false};
        List<Method> methods = findMethods(clazz, method -> {
            if (found[0]) {
                /* Если уже нашли - то больше не ищем */
                return false;
            }
            if (!methodName.equals(method.getName())) {
                return false;
            }
            if (!isAppropriateLevel(method, level)) {
                return false;
            }
            if (returnType != null && method.getReturnType() != returnType) {
                return false;
            }
            if (!arraysEqual(method.getParameterTypes(), parameterTypes)) {
                return false;
            }

            found[0] = true;
            return true;
        });
        return methods.size() == 1 ? methods.get(0) : null;
    }

    /**
     * Находит конструкторы класса соотвтествующих некоторому условию
     *
     * @param clazz               - класс в котором производится поиск
     * @param constructorSelector интерфейс выбора конструкторов
     * @return найденный метод или null если метод не найден ()
     */
    public static <T> List<Constructor<T>> findConstructors(
            Class<T> clazz,
            BiFunction<Class<T>, Constructor<T>, Boolean> constructorSelector) {

        Objects.requireNonNull(clazz, CLASS_IS_NULL);
        Objects.requireNonNull(constructorSelector, "constructorFilter is null");

        List<Constructor<T>> constructors = new ArrayList<>();
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (!constructorSelector.apply(clazz, constructor)) {
                continue;
            }
            //noinspection unchecked
            constructors.add(constructor);
        }
        return constructors;
    }


    /**
     * Находит конструкторы класса соотвтествующих некоторому условию
     *
     * @param clazz               - класс в котором производится поиск
     * @param constructorSelector интерфейс выбора конструкторов
     * @return найденный метод или null если метод не найден ()
     */
    public static <T> List<Constructor<T>> findConstructors(
            Class<T> clazz,
            Predicate<Constructor<T>> constructorSelector) {
        Objects.requireNonNull(constructorSelector, "constructorFilter is null");
        return findConstructors(clazz, (c, constructor) -> constructorSelector.test(constructor));
    }


    /**
     * Находит конструктор класса
     *
     * @param level          - уровень доступности метода
     * @param clazz          - класс в котором производится поиск
     * @param parameterTypes - типы параметров
     * @return найденный метод или null если метод не найден ()
     */
    public static <T> Constructor<T> findConstructor(
            Level level,
            Class<T> clazz,
            Class<?>... parameterTypes) {

        Objects.requireNonNull(clazz, CLASS_IS_NULL);
        Objects.requireNonNull(level, "level is null");

        List<Constructor<T>> constructors = findConstructors(clazz,
                (tClass, constructor) -> isAppropriateLevel(constructor, level) &&
                        arraysEqual(constructor.getParameterTypes(), parameterTypes));

        return constructors.size() == 1 ? constructors.get(0) : null;
    }


    /**
     * Поиск полей в классе по критерию.
     *
     * @param clazz    - класс
     * @param criteria - критерий поиска
     * @return список полей удовлетворяющих критерию. (всегда не null).
     */
    public static List<Field> findFields(Class<?> clazz, Predicate<Field> criteria) {
        Objects.requireNonNull(clazz, CLASS_IS_NULL);
        Objects.requireNonNull(criteria, "criteria is null");

        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (criteria.test(field)) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }


    /* ---------------------------------------------------- privates ------------------------------------------------ */

    /**
     * Сравнение массивов элементов одного типа
     *
     * @param a1  - массив 1
     * @param a2  - массив 2
     * @param <T> тип элементов массивов
     * @return - true если оба массива либо null либо равно количество элементов каждый элемент позиции i одного массива
     * равен  элементу i той позиции другого массива
     */
    private static <T> boolean arraysEqual(T[] a1, T[] a2) {
        if (a1 == a2) {
            return true;
        }
        if ((a1 == null) != (a2 == null)) {
            return false;
        }
        if (a1.length != a2.length) {
            return false;
        }

        for (int i = 0; i < a1.length; i++) {
            if (!Objects.equals(a1[i], a2[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAppropriateLevel(Method method, Level level) {
        return isAppropriateLevel(method.getModifiers(), level);
    }

    private static boolean isAppropriateLevel(Constructor constructor, Level level) {
        return isAppropriateLevel(constructor.getModifiers(), level);
    }

    private static boolean isAppropriateLevel(int modifiers, Level level) {
        return METHOD_LEVEL_INFO.get(getLevel(modifiers)).level() <= METHOD_LEVEL_INFO.get(level).level();
    }

}
