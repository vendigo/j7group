package com.github.vendigo.j7group;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

class ProxyHelper {
    private ProxyHelper() {}

    private final static ThreadLocal<Method> firstCalledMethod = new ThreadLocal<>();
    private final static ThreadLocal<Method> secondCalledMethod = new ThreadLocal<>();

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
