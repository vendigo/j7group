package com.github.vendigo.j7group;

import com.github.vendigo.j7group.key.ambiguity.KeyAmbiguityPolicy;

import java.util.*;

import static com.github.vendigo.j7group.ProxyHelper.extractFirstArgument;

class GroupHelper {
    static final int DEFAULT_CAPACITY = 10;

    private GroupHelper() {
    }

    static <T, V> boolean checkUniqueness(Collection<T> in) {
        Set<V> uniqueElements = new HashSet<>();

        for (T entity : in) {
            V fieldValue = extractFirstArgument(entity);
            if (!uniqueElements.add(fieldValue)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    static <T, V> Collection<V> genericCollect(Collection<T> from, Class<? extends Collection> collectionClass,
                                               int initialCapacity) {
        Collection<V> collected = createCollectionInstance(collectionClass, initialCapacity);

        for (T entity : from) {
            collected.add(ProxyHelper.<V>extractFirstArgument(entity));
        }

        return collected;
    }

    static <T> List<T> collectWithPredicate(Collection<T> from) {
        List<T> collected = new ArrayList<>();

        boolean desiredValue = extractDesiredValue();

        for (T entry : from) {
            if (desiredValue == (Boolean)extractFirstArgument(entry)) {
                collected.add(entry);
            }
        }
        return collected;
    }

    private static boolean extractDesiredValue() {
        J7GroupPrepositions.Preposition calledPreposition = ProxyHelper.getCalledPreposition();
        switch (calledPreposition) {
            case WHEN_TRUE:
                return true;
            case WHEN_FALSE:
                return false;
            default:
                throw new IllegalPrepositionException("Not allowed preposition was used");

        }
    }

    static <K, V, C, T> Map<K, C> genericGroup(Collection<T> collection, GroupStrategy<K, V, C> groupStrategy,
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

    static <K, T> GroupStrategy<K, T, T> resolveGroupStrategy(KeyAmbiguityPolicy keyAmbiguityPolicy) {
        GroupStrategy<K, T, T> groupStrategy;
        switch (keyAmbiguityPolicy) {
            case KEEP_FIRST:
                groupStrategy = new KeepFirstGroupStrategy<>();
                break;
            case FAIL_FAST:
                groupStrategy = new FailFastGroupStrategy<>();
                break;
            default:
                groupStrategy = new KeepLastGroupStrategy<>();
        }
        return groupStrategy;
    }

    @SuppressWarnings("unchecked")
    static <C extends Collection> C createCollectionInstance(Class<? extends Collection> collectionClass, int initialCapacity) {
        try {
            return (C) collectionClass.getConstructor(int.class).newInstance(initialCapacity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
