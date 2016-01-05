package com.github.vendigo.j7group;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;

public class J7Group {
    private final static ThreadLocal<Method> mainCalledMethod = new ThreadLocal<>();
    private final static ThreadLocal<Method> secondaryCalledMethod = new ThreadLocal<>();

    public static <T> T field(Class<T> entityClass) {
        return createProxy(entityClass, mainCalledMethod);
    }

    public static <T> T by(Class<T> entityClass) {
        return createProxy(entityClass, mainCalledMethod);
    }

    public static <T> T from(Class<T> entityClass) {
        return createProxy(entityClass, mainCalledMethod);
    }

    public static <T> T to(Class<T> entityClass) {
        return createProxy(entityClass, secondaryCalledMethod);
    }

    public static <T, V> List<V> collectFrom(Collection<T> from, V field) {
        List<V> collected = new ArrayList<>(from.size());

        for (T entity : from) {
            collected.add(J7Group.<V>extractValue(entity, mainCalledMethod));
        }

        return collected;
    }

    public static <K, V> Map<K, V> group(Collection<V> collection, K by) {
        Map<K, V> resultMap = new HashMap<>();

        for (V entity : collection) {
            resultMap.put(J7Group.<K>extractValue(entity, mainCalledMethod), entity);
        }

        return resultMap;
    }

    public static <K, V> Map<K, List<V>> groupToLists(Collection<V> collection, K by) {
        return groupToCollection(collection, ArrayList.class);
    }

    public static <K, V> Map<K, Set<V>> groupToSets(Collection<V> collection, K by) {
        return groupToCollection(collection, HashSet.class);
    }

    public static <K, V, T> Map<K, V> map(Collection<T> collection, K from, V to) {
        Map<K, V> resultMap = new HashMap<>();

        for (T entity : collection) {
            resultMap.put(J7Group.<K>extractValue(entity, mainCalledMethod), J7Group.<V>extractValue(entity, secondaryCalledMethod));
        }

        return resultMap;
    }

    //Utility methods -----------------------------------

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static <K, V, T extends Collection<V>> Map<K, T> groupToCollection(Collection<V> collection,
                                                                               Class<? extends Collection> collectionClass) {
        Map<K, Collection<V>> resultMap = new HashMap<>();

        for (V entity : collection) {
            K key = extractValue(entity, mainCalledMethod);
            Collection<V> valuesForKey = resultMap.get(key);
            if (valuesForKey == null) {
                valuesForKey = createCollectionInstance(collectionClass);
            }
            valuesForKey.add(entity);
            resultMap.put(key, valuesForKey);
        }

        return (Map<K, T>) resultMap;
    }

    @SuppressWarnings("unchecked")
    private static <V> Collection<V> createCollectionInstance(Class<? extends Collection> collectionClass) {
        try {
            return (Collection<V>) collectionClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
