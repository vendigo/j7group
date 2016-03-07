package com.github.vendigo.j7group;

import com.github.vendigo.j7group.key.ambiguity.KeyAmbiguityPolicy;

import java.util.*;

import static com.github.vendigo.j7group.GroupHelper.*;

/**
 * Bunch of useful methods for type safe manipulations with collections.
 * See doc for each method and J7GroupTest for more usage examples.
 * @author Dmytro Marchenko
 */
public final class J7Group {

    private J7Group() {
    }

    /**
     * Checks whether value of some field is unique within given collection.
     * Usage example: {@code isUniqueIn(persons, field(Person.class).getName())}
     * @param in - collection to check uniqueness in
     * @param field - placeholder for "field" construction
     * @param <T> - type of collection
     * @param <V> - type of target field
     * @return true if all field values are different
     */
    public static <T, V> boolean isUniqueIn(Collection<T> in, V field) {
        return checkUniqueness(in);
    }

    public static <T> List<T> collect(Collection<T> from, boolean predicate) {
        return collectWithPredicate(from);
    }

    /**
     * Collects values of some field to list.
     * Usage example: {@code collectToListFrom(persons, field(Person.class).getName())}
     * @param from - collection to collect field values from
     * @param field - placeholder for "field" construction
     * @param <T> - type of collection
     * @param <V> - type of collected field
     * @return List with collected values
     */
    public static <T, V> List<V> collectToListFrom(Collection<T> from, V field) {
        return (List<V>) genericCollect(from, ArrayList.class, from.size());
    }

    /**
     * Collects values of some field to set.
     * Usage example: {@code collectToSetFrom(persons, field(Person.class).getName())}
     * @param from - collection to collect field values from
     * @param field - placeholder for "field" construction
     * @param <T> - type of collection
     * @param <V> - type of collected field
     * @return Set with collected values
     */
    public static <T, V> Set<V> collectToSetFrom(Collection<T> from, V field) {
        return (Set<V>) genericCollect(from, HashSet.class, from.size());
    }

    /**
     * Groups entities from given collection by given field.
     * If few entities would have equal key, keeps only last occurrence.
     * Usage example: {@code group(persons, by(Person.class).getAge())}
     * @param collection - given collection
     * @param by - placeholder for "by" construction
     * @param <K> - type of field for grouping
     * @param <T> - type of collection
     * @return Map "field" -> "entity"
     */
    public static <K, T> Map<K, T> group(Collection<T> collection, K by) {
        return group(collection, by, KeyAmbiguityPolicy.KEEP_LAST);
    }

    /**
     * Groups entities from given collection by given field.
     * Usage example: {@code group(persons, by(Person.class).getAge(), KeyAmbiguityPolicy.KEEP_FIRST)}
     * @param collection - given collection
     * @param by - placeholder for "by" construction
     * @param keyAmbiguityPolicy - rule how handle equal keys
     * @param <K> - type of field for grouping
     * @param <T> - type of collection
     * @return Map "field" -> "entity"
     */
    public static <K, T> Map<K, T> group(Collection<T> collection, K by, KeyAmbiguityPolicy keyAmbiguityPolicy) {
        GroupStrategy<K, T, T> groupStrategy = resolveGroupStrategy(keyAmbiguityPolicy);
        return genericGroup(collection, groupStrategy, new EntityAsValueExtractor<T>());
    }

    /**
     * Groups entities from given collection by given field.
     * Use this when there are entities with equal keys.
     * Usage example: {@code groupToLists(persons, by(Person.class).getAge())}
     * @param collection - given collection
     * @param by - placeholder for "by" construction
     * @param <K> - type of field for grouping
     * @param <T> - type of collection
     * @return Map "field" -> List of "entities"
     */
    public static <K, T> Map<K, List<T>> groupToLists(Collection<T> collection, K by) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, T, List<T>>(ArrayList.class,
                        collection.size()),
                new EntityAsValueExtractor<T>());
    }

    /**
     * Groups entities from given collection by given field.
     * Use this when there are entities with equal keys.
     * Usage example: {@code groupToSets(persons, by(Person.class).getAge())}
     * @param collection - given collection
     * @param by - placeholder for "by" construction
     * @param <K> - type of field for grouping
     * @param <T> - type of collection
     * @return Map "field" -> Set of "entities"
     */
    public static <K, T> Map<K, Set<T>> groupToSets(Collection<T> collection, K by) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, T, Set<T>>(HashSet.class,
                        collection.size()),
                new EntityAsValueExtractor<T>());
    }

    /**
     * Creates mapping from some field of the entity to another.
     * Usage example: {@code map(persons, from(Person.class).getName(), to(Person.class).getAge())}
     * If few entities would have equal keys, keeps only last occurrence.
     * @param collection - given collection
     * @param from - placeholder for "from" construction
     * @param to - placeholder for "to" construction
     * @param <K> - type of "from" field
     * @param <V> - type of "to" field
     * @param <T> - type of collection
     * @return Map "from" -> "to"
     */
    public static <K, V, T> Map<K, V> map(Collection<T> collection, K from, V to) {
        return map(collection, from, to, KeyAmbiguityPolicy.KEEP_LAST);
    }

    /**
     * Creates mapping from some field of the entity to another.
     * Usage example: {@code map(persons, from(Person.class).getName(), to(Person.class).getAge(), KeyAmbiguityPolicy.FAIL_FAST)}
     * @param collection - given collection
     * @param from - placeholder for "from" construction
     * @param to - placeholder for "to" construction
     * @param keyAmbiguityPolicy - rule how handle equal keys
     * @param <K> - type of "from" field
     * @param <V> - type of "to" field
     * @param <T> - type of collection
     * @return Map "from" -> "to"
     */
    public static <K, V, T> Map<K, V> map(Collection<T> collection, K from, V to, KeyAmbiguityPolicy keyAmbiguityPolicy) {
        GroupStrategy<K, V, V> groupStrategy = resolveGroupStrategy(keyAmbiguityPolicy);
        return genericGroup(collection, groupStrategy, new SecondArgumentValueExtractor<T, V>());
    }

    /**
     * Creates mapping from some field of the entity to another.
     * Usage example: {@code mapToLists(persons, from(Person.class).getName(), to(Person.class).getAge())}
     * Use this when there are few entities with equal key.
     * @param collection - given collection
     * @param from - placeholder for "from" construction
     * @param to - placeholder for "to" construction
     * @param <K> - type of "from" field
     * @param <V> - type of "to" field
     * @param <T> - type of collection
     * @return Map "from" -> List of "to"
     */
    public static <K, V, T> Map<K, List<V>> mapToLists(Collection<T> collection, K from, V to) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, V, List<V>>(ArrayList.class,
                GroupHelper.DEFAULT_CAPACITY), new SecondArgumentValueExtractor<T, V>());
    }

    /**
     * Creates mapping from some field of the entity to another.
     * Usage example: {@code mapToSets(persons, from(Person.class).getName(), to(Person.class).getAge())}
     * Use this when there are few entities with equal key.
     * @param collection - given collection
     * @param from - placeholder for "from" construction
     * @param to - placeholder for "to" construction
     * @param <K> - type of "from" field
     * @param <V> - type of "to" field
     * @param <T> - type of collection
     * @return Map "from" -> Set of "to"
     */
    public static <T, K, V> Map<K, Set<V>> mapToSets(Collection<T> collection, K from, V to) {
        return genericGroup(collection, new ToCollectionGroupStrategy<K, V, Set<V>>(HashSet.class,
                GroupHelper.DEFAULT_CAPACITY), new SecondArgumentValueExtractor<T, V>());
    }
}
