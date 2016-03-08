package com.github.vendigo.j7group;

import com.github.vendigo.j7group.key.ambiguity.KeyAmbiguityPolicy;

import java.util.*;

import static com.github.vendigo.j7group.ProxyHelper.*;

class GroupHelper {
    static final int DEFAULT_CAPACITY = 10;
    public static final int FIRST_PREPOSITION_INDEX = 0;

    private GroupHelper() {
    }

    private static void checkPrepositions(J7GroupPrepositions.Preposition... prepositions) {
        try {
            for (int i = 0; i < prepositions.length; i++) {
                checkPreposition(prepositions[i], i);
            }
        } finally {
            clearCalledPrepositions();
        }
    }

    private static void checkPreposition(J7GroupPrepositions.Preposition preposition, int index) {
        J7GroupPrepositions.Preposition calledPreposition = getCalledPreposition(index);
        if (!preposition.equals(calledPreposition)) {
            throw new IllegalPrepositionException("Not allowed preposition used. Expected preposition: " + preposition +
                    " Actual preposition: " + calledPreposition);
        }
    }

    static <T, V> boolean checkUniqueness(Collection<T> in) {
        checkPrepositions(J7GroupPrepositions.Preposition.FIELD);

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
        checkPrepositions(J7GroupPrepositions.Preposition.FIELD);
        Collection<V> collected = createCollectionInstance(collectionClass, initialCapacity);

        for (T entity : from) {
            collected.add(ProxyHelper.<V>extractFirstArgument(entity));
        }

        return collected;
    }

    static <T> List<T> collectWithPredicate(Collection<T> from) {
        boolean desiredValue = extractDesiredValue();

        List<T> collected = new ArrayList<>();
        for (T entry : from) {
            if (desiredValue == (Boolean) extractFirstArgument(entry)) {
                collected.add(entry);
            }
        }

        return collected;
    }

    static <T> Collection<T> removeByPredicate(Collection<T> from) {
        boolean desiredValue = extractDesiredValue();

        Iterator<T> iterator = from.iterator();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (desiredValue == (Boolean)extractFirstArgument(element)) {
                iterator.remove();
            }
        }
        return from;
    }

    private static boolean extractDesiredValue() {
        J7GroupPrepositions.Preposition calledPreposition = ProxyHelper.getCalledPreposition(FIRST_PREPOSITION_INDEX);
        try {
            switch (calledPreposition) {
                case WHEN_TRUE:
                    return true;
                case WHEN_FALSE:
                    return false;
                default:
                    throw new IllegalPrepositionException("Not allowed preposition was used. " +
                            "Expected prepositions: WHEN_TRUE, WHEN_FALSE. Actual preposition: " + calledPreposition);

            }
        } finally {
            clearCalledPrepositions();
        }
    }

    static <K, V, C, T> Map<K, C> genericGroup(Collection<T> collection, GroupStrategy<K, V, C> groupStrategy,
                                               ValueExtractor<T, V> valueExtractor,
                                               J7GroupPrepositions.Preposition... expectedPrepositions) {
        checkPrepositions(expectedPrepositions);
        clearCalledPrepositions();
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
