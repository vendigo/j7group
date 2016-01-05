package com.github.vendigo.j7group;

import java.util.*;

import static com.github.vendigo.j7group.GroupHelper.collectToCollection;
import static com.github.vendigo.j7group.GroupHelper.genericGroup;
import static com.github.vendigo.j7group.GroupHelper.groupToCollection;
import static com.github.vendigo.j7group.ProxyHelper.interceptAsFirstArgument;
import static com.github.vendigo.j7group.ProxyHelper.interceptAsSecondArgument;

public class J7Group {

    private J7Group() {
    }

    public static <T> T field(Class<T> entityClass) {
        return interceptAsFirstArgument(entityClass);
    }

    public static <T> T by(Class<T> entityClass) {
        return interceptAsFirstArgument(entityClass);
    }

    public static <T> T from(Class<T> entityClass) {
        return interceptAsFirstArgument(entityClass);
    }

    public static <T> T to(Class<T> entityClass) {
        return interceptAsSecondArgument(entityClass);
    }

    public static <T, V> List<V> collectToListFrom(Collection<T> from, V field) {
        return (List<V>) collectToCollection(from, ArrayList.class, from.size());
    }

    public static <T, V> Set<V> collectToSetFrom(Collection<T> from, V field) {
        return (Set<V>) collectToCollection(from, HashSet.class, from.size());
    }

    public static <K, T> Map<K, T> group(Collection<T> collection, K by) {
        return genericGroup(collection, new RetainLastGroupStrategy<K, T>(), new EntityAsValueExtractor<T>());
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
            resultMap.put(ProxyHelper.<K>extractFirstArgument(entity), ProxyHelper.<V>extractSecondArgument(entity));
        }

        return resultMap;
    }

    public static <T, K, V> Map<K, List<V>> mapToLists(Collection<T> collection, K from, V to) {
        return GroupHelper.<T, K, V, List<V>>mapToCollection(collection, ArrayList.class);
    }


    public static <T, K, V> Map<K, Set<V>> mapToSets(Collection<T> collection, K from, V to) {
        return GroupHelper.<T, K, V, Set<V>>mapToCollection(collection, HashSet.class);
    }
}
