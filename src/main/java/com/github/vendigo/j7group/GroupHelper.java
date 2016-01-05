package com.github.vendigo.j7group;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.github.vendigo.j7group.ProxyHelper.extractFirstArgument;

class GroupHelper {
    private static final int DEFAULT_CAPACITY = 10;

    private GroupHelper() {
    }

    @SuppressWarnings("ConstantConditions")
    static <T, V> Collection<V> collectToCollection(Collection<T> from, Class<? extends Collection> collectionClass,
                                                    int initialCapacity) {
        Collection<V> collected = createCollectionInstance(collectionClass, initialCapacity);

        for (T entity : from) {
            collected.add(ProxyHelper.<V>extractFirstArgument(entity));
        }

        return collected;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    static <K, V, T extends Collection<V>> Map<K, T> groupToCollection(Collection<V> collection,
                                                                       Class<? extends Collection> collectionClass) {
        Map<K, Collection<V>> resultMap = new HashMap<>();

        for (V entity : collection) {
            K key = extractFirstArgument(entity);
            Collection<V> valuesForKey = resultMap.get(key);
            if (valuesForKey == null) {
                valuesForKey = createCollectionInstance(collectionClass, DEFAULT_CAPACITY);
            }
            valuesForKey.add(entity);
            resultMap.put(key, valuesForKey);
        }

        return (Map<K, T>) resultMap;
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    static <T, K, V, C extends Collection<V>> Map<K, C> mapToCollection(Collection<T> collection,
                                                                     Class<? extends Collection> collectionClass) {
        Map<K, C> resultMap = new HashMap<>();

        for (T entity : collection) {
            K key = extractFirstArgument(entity);
            C valuesForKey = resultMap.get(key);
            if (valuesForKey == null) {
                valuesForKey = (C)createCollectionInstance(collectionClass, DEFAULT_CAPACITY);
            }
            valuesForKey.add(ProxyHelper.<V>extractSecondArgument(entity));
            resultMap.put(key, valuesForKey);
        }

        return resultMap;
    }

    @SuppressWarnings("unchecked")
    private static <V> Collection<V> createCollectionInstance(Class<? extends Collection> collectionClass, int initialCapacity) {
        try {
            return (Collection<V>) collectionClass.getConstructor(int.class).newInstance(initialCapacity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
