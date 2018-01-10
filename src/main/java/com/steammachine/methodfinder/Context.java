package com.steammachine.methodfinder;

import java.lang.reflect.Method;

/**
 * Created 31.12.2017 18:10
 *
 * @author Vladimir Bogodukhov
 **/
class Context {
    private final StackTraceElement element;
    private final Class clazz;
    private final Method method;

    public Context(StackTraceElement element, Class clazz, Method method) {
        this.element = element;
        this.clazz = clazz;
        this.method = method;
    }

    public Context(StackTraceElement element) {
        this(element, null, null);
    }

    public Context updateClass(Class clazz) {
        return new Context(this.element, clazz, this.method);
    }

    public Context updateMethod(Method method) {
        return new Context(this.element, this.clazz, method);
    }


    public StackTraceElement element() {
        return element;
    }

    public Class clazz() {
        return clazz;
    }

    public Method method() {
        return method;
    }

    public boolean everithingIsSet() {
        return method != null && element != null && clazz != null;
    }

    public Context checkEverithingIsSet() {
        if (!everithingIsSet()) {
            throw new IllegalStateException("!everithingIsSet()");
        }
        return this;
    }


}

