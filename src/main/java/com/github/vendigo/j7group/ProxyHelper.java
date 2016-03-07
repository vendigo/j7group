package com.github.vendigo.j7group;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class ProxyHelper {
    private final static ThreadLocal<Method> firstCalledMethod = new ThreadLocal<>();
    private final static ThreadLocal<Method> secondCalledMethod = new ThreadLocal<>();
    private final static ThreadLocal<List<J7GroupPrepositions.Preposition>> calledPrepositions = new ThreadLocal<>();

    static {
        calledPrepositions.set(new ArrayList<J7GroupPrepositions.Preposition>(2));
    }

    private ProxyHelper() {
    }

    static void addCalledPreposition(J7GroupPrepositions.Preposition preposition) {
        calledPrepositions.get().add(preposition);
    }

    static J7GroupPrepositions.Preposition getCalledPreposition(int index) {
        return calledPrepositions.get().get(index);
    }

    static void clearCalledPrepositions() {
        calledPrepositions.set(new ArrayList<J7GroupPrepositions.Preposition>(2));
    }

    static <T> T interceptAsFirstArgument(Class<T> entityClass) {
        return createProxy(entityClass, firstCalledMethod);
    }

    static <T> T interceptAsSecondArgument(Class<T> entityClass) {
        return createProxy(entityClass, secondCalledMethod);
    }

    static <T> T extractFirstArgument(Object entity) {
        return extractValue(entity, firstCalledMethod);
    }

    static <T> T extractSecondArgument(Object entity) {
        return extractValue(entity, secondCalledMethod);
    }

    @SuppressWarnings("unchecked")
    private static <T> T createProxy(Class<T> entityClass, final ThreadLocal<Method> methodContainer) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(entityClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                methodContainer.set(method);
                return null;
            }
        });
        return ((T) enhancer.create());
    }

    @SuppressWarnings("unchecked")
    private static <T> T extractValue(Object entity, ThreadLocal<Method> methodContainer) {
        try {
            return (T) methodContainer.get().invoke(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
