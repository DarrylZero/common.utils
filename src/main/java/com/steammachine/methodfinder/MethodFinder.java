package com.steammachine.methodfinder;

import com.steammachine.common.apilevel.Api;
import com.steammachine.common.apilevel.State;
import com.steammachine.common.definitions.annotations.SignatureSensitive;
import com.steammachine.common.lazyeval.LazyEval;
import com.steammachine.common.utils.ResourceUtils;
import com.steammachine.common.utils.commonutils.CommonUtils;
import com.steammachine.methodfinder.ClassMethodTable.SourceCodePosition;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 *
 * {@link com.steammachine.methodfinder.MethodFinder}
 **/
@Api(State.INCUBATING)
public class MethodFinder {

    public interface Signatures {
        Map<String, ClassMethodTable.SourceCodePosition> get(Class clazz);
    }

    public static class DefaultSignatures implements Signatures {
        private final Map<Class, LazyEval<Map<String, SourceCodePosition>>> evalMap = new HashMap<>();

        public Map<String, ClassMethodTable.SourceCodePosition> get(Class clazz) {
            evalMap.putIfAbsent(clazz, LazyEval.eval(() -> methodsMap(clazz)));
            return evalMap.get(clazz).value();
        }
    }


    /**
     * Производит поиск метода по стеку вызова.
     * Вызываемый к каком то месте кода метод получает информацию о стековых вызовах получает если это возможно все
     * объекты классов и методов классов
     * <p>
     * <p>
     * Возвращается первый найденный метод соответствующий передаваемому критерию (methodCriteria).
     *
     * @param classCriteria  критрерий отбора классов (всегда не null)
     * @param methodCriteria критрерий отбора методов (всегда не null)
     * @return найденный метод или null
     */
    public static Method call(
            Predicate<Class> classCriteria,
            Predicate<Method> methodCriteria) {
        Objects.requireNonNull(methodCriteria);
        Objects.requireNonNull(classCriteria);

        LazyEval<Signatures> methods = LazyEval.eval(DefaultSignatures::new);

        Stream<StackTraceElement> stackTraceStream = Stream.of(Thread.currentThread().getStackTrace());
        return stackTraceStream.
                map(Context::new). /* Обернули контекстом */
                map(c -> c.updateClass(getClass(c.element().getClassName()))). /* обновили класс контекста */
                filter(c -> classCriteria.test(c.clazz())). /* отфильтровали класс */
                map(c -> c.updateMethod(getMethod(c.clazz(), c.element().getMethodName(), methods, c.element().getLineNumber()))).
                filter(Context::everithingIsSet).
                map(Context::checkEverithingIsSet). /* Тут проверим, что установлено все что нужно. */
                filter(c -> methodCriteria.test(c.method())).
                map(Context::method).
                findFirst().orElse(null);
    }

    private static Class getClass(String className) {
        Objects.requireNonNull(className);
        return CommonUtils.suppress(() -> Thread.currentThread().getContextClassLoader().loadClass(className));
    }


    @SignatureSensitive
    private static Method getMethod(Class clazz, String methodName,
                                    LazyEval<Signatures> methodsMap, int position) {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(methodName);
        Objects.requireNonNull(methodsMap);

        if (position < 0) {
            return null;
        }

        List<Method> methods = Stream.of(clazz.getDeclaredMethods()).
                filter(m -> methodName.equals(m.getName())).collect(toList());
        if (methods.isEmpty()) {
            /* метод по имени не найден */
            return null;
        }

        Map.Entry<String, ClassMethodTable.SourceCodePosition> entry = methodsMap.value().get(clazz).entrySet().stream().
                // filter((en) -> methodName.equals(en.getKey())).
                        filter(en -> en.getValue().hasPosition()).
                        filter(en -> en.getValue().inlinerange(position)).
                        findFirst().orElse(null);

        if (entry == null) {
            /* метод на позиции не найден */
            return null;
        }

        return methods.stream().filter(m -> ClassMethodTable.methodSignature(m).equals(entry.getKey())).
                findFirst().orElse(null);
    }


    public static Map<String, ClassMethodTable.SourceCodePosition> methodsMap(Class clazz) {
        Objects.requireNonNull(clazz);
        InputStream classStream = clazz.getResourceAsStream(ResourceUtils.classFileName(clazz));
        if (classStream == null) {
            /* for classes build dynamically - it is not possible to load class data */
            return Collections.emptyMap();
        }

        try {
            try {
                return ClassMethodTable.readClassFromInputStream(classStream);
            } finally {
                classStream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}
