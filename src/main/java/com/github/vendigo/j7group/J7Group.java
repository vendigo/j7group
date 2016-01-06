package com.github.vendigo.j7group;

import java.util.*;

import static com.github.vendigo.j7group.GroupHelper.genericCollect;
import static com.github.vendigo.j7group.GroupHelper.genericGroup;
import static com.github.vendigo.j7group.GroupHelper.resolveGroupStrategy;
import static com.github.vendigo.j7group.ProxyHelper.interceptAsFirstArgument;
import static com.github.vendigo.j7group.ProxyHelper.interceptAsSecondArgument;

public final class J7Group {

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
        return (List<V>) genericCollect(from, ArrayList.class, from.size());
    }

    public static <T, V> Set<V> collectToSetFrom(Collection<T> from, V field) {
        return (Set<V>) genericCollect(from, HashSet.class, from.size());
    }

    public static <K, T> Map<K, T> group(Collection<T> collection, K by) {
        return group(collection, by, KeyAmbiguityPolicy.KEEP_LAST);
    }

    public static <K, T> Map<K, T> group(Collection<T> collection, K by, KeyAmbiguityPolicy keyAmbiguityPolicy) {
        GroupStrategy<K, T, T> groupStrategy = resolveGroupStrategy(keyAmbiguityPolicy);
        return genericGroup(collection, groupStrategy, new EntityAsValueExtractor<T>());
    }

    public static <K, T> Map<K, List<T>> groupToLists(Collection<T> collection, K by) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, T, List<T>>(ArrayList.class,
                        collection.size()),
                new EntityAsValueExtractor<T>());
    }

    public static <K, T> Map<K, Set<T>> groupToSets(Collection<T> collection, K by) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, T, Set<T>>(HashSet.class,
                        collection.size()),
                new EntityAsValueExtractor<T>());
    }

    public static <K, V, T> Map<K, V> map(Collection<T> collection, K from, V to) {
        return map(collection, from, to, KeyAmbiguityPolicy.KEEP_LAST);
    }

    public static <K, V, T> Map<K, V> map(Collection<T> collection, K from, V to, KeyAmbiguityPolicy keyAmbiguityPolicy) {
        GroupStrategy<K, V, V> groupStrategy = resolveGroupStrategy(keyAmbiguityPolicy);
        return genericGroup(collection, groupStrategy, new SecondArgumentValueExtractor<T, V>());
    }

    public static <K, V, T> Map<K, List<V>> mapToLists(Collection<T> collection, K from, V to) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, V, List<V>>(ArrayList.class,
                GroupHelper.DEFAULT_CAPACITY), new SecondArgumentValueExtractor<T, V>());
    }

    public static <T, K, V> Map<K, Set<V>> mapToSets(Collection<T> collection, K from, V to) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, V, Set<V>>(HashSet.class,
                GroupHelper.DEFAULT_CAPACITY), new SecondArgumentValueExtractor<T, V>());
    }
}
