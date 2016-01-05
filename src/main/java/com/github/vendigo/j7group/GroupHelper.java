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

    public static <K, V, C, T> Map<K, C> genericGroup(Collection<T> collection, GroupStrategy<K, V, C> groupStrategy,
                                                      ValueExtractor<T, V> valueExtractor) {
        Map<K, C> resultMap = new HashMap<>();

        for (T entity : collection) {
            K key = extractFirstArgument(entity);
            C oldValue = resultMap.get(key);
            V newValue = valueExtractor.extract(entity);
            if (oldValue == null) {
                groupStrategy.handleFirstOccurrence(key, newValue, resultMap);
            } else {
                groupStrategy.handleNonFirstOccurrence(key, newValue, oldValue, resultMap);
            }
        }

        return resultMap;
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    static <T, K, V, C extends Collection<V>> Map<K, C> mapToCollection(Collection<T> collection,
                                                                        Class<? extends Collection> collectionClass) {
        Map<K, C> resultMap = new HashMap<>();

        for (T entity : collection) {
            K key = extractFirstArgument(entity);
            C valuesForKey = resultMap.get(key);
            if (valuesForKey == null) {
                valuesForKey = createCollectionInstance(collectionClass, DEFAULT_CAPACITY);
            }
            valuesForKey.add(ProxyHelper.<V>extractSecondArgument(entity));
            resultMap.put(key, valuesForKey);
        }

        return resultMap;
    }

    @SuppressWarnings("unchecked")
    static <C extends Collection> C createCollectionInstance(Class<? extends Collection> collectionClass, int initialCapacity) {
        try {
            return (C)collectionClass.getConstructor(int.class).newInstance(initialCapacity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
