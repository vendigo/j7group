package com.github.vendigo.j7group;

import com.github.vendigo.j7group.key.ambiguity.KeyAmbiguityPolicy;

import java.util.Collection;

import static com.github.vendigo.j7group.ProxyHelper.*;

/**
 * Key words for DSL like constructions
 * @author Dmytro Marchenko
 */
public class J7GroupPrepositions {
    private J7GroupPrepositions() {
    }

    enum Preposition {
        FIELD, BY, FROM, TO, WHEN_TRUE, WHEN_FALSE
    }

    /**
     * Using as second argument in:
     * <ul>
     * <li>{@link J7Group#isUniqueIn(Collection, Object)}</li>
     * <li>{@link J7Group#collectToListFrom(Collection, Object)}</li>
     * <li>{@link J7Group#collectToSetFrom(Collection, Object)}</li>
     * </ul>
     * @param entityClass - class of target entity
     * @param <T> - type of given entity
     */
    public static <T> T field(Class<T> entityClass) {
        addCalledPreposition(Preposition.FIELD);
        return interceptAsFirstArgument(entityClass);
    }

    /**
     * Using as second argument in:
     * <ul>
     * <li>{@link J7Group#group(Collection, Object)}</li>
     * <li>{@link J7Group#group(Collection, Object, KeyAmbiguityPolicy)}</li>
     * <li>{@link J7Group#groupToLists(Collection, Object)}</li>
     * <li>{@link J7Group#groupToSets(Collection, Object)}</li>
     * </ul>
     * @param entityClass - class of target entity
     * @param <T> - type of given entity
     */
    public static <T> T by(Class<T> entityClass) {
        addCalledPreposition(Preposition.BY);
        return interceptAsFirstArgument(entityClass);
    }

    /**
     * Using as second argument in:
     * <ul>
     * <li>{@link J7Group#map(Collection, Object, Object)}</li>
     * <li>{@link J7Group#map(Collection, Object, Object, KeyAmbiguityPolicy)}</li>
     * <li>{@link J7Group#mapToLists(Collection, Object, Object)}</li>
     * <li>{@link J7Group#mapToSets(Collection, Object, Object)}</li>
     * </ul>
     * @param entityClass - class of target entity
     * @param <T> - type of given entity
     */
    public static <T> T from(Class<T> entityClass) {
        addCalledPreposition(Preposition.FROM);
        return interceptAsFirstArgument(entityClass);
    }

    /**
     * Using as third argument in:
     * <ul>
     * <li>{@link J7Group#map(Collection, Object, Object)}</li>
     * <li>{@link J7Group#map(Collection, Object, Object, KeyAmbiguityPolicy)}</li>
     * <li>{@link J7Group#mapToLists(Collection, Object, Object)}</li>
     * <li>{@link J7Group#mapToSets(Collection, Object, Object)}</li>
     * </ul>
     * @param entityClass - class of target entity
     * @param <T> - type of given entity
     */
    public static <T> T to(Class<T> entityClass) {
        addCalledPreposition(Preposition.TO);
        return interceptAsSecondArgument(entityClass);
    }

    public static <T> T whenTrue(Class<T> entityClass) {
        addCalledPreposition(Preposition.WHEN_TRUE);
        return interceptAsFirstArgument(entityClass);
    }

    public static <T> T whenFalse(Class<T> entityClass) {
        addCalledPreposition(Preposition.WHEN_FALSE);
        return interceptAsFirstArgument(entityClass);
    }
}
