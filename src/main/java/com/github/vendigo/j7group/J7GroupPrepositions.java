package com.github.vendigo.j7group;

import com.github.vendigo.j7group.key.ambiguity.KeyAmbiguityPolicy;

import java.util.Collection;

import static com.github.vendigo.j7group.ProxyHelper.interceptAsFirstArgument;
import static com.github.vendigo.j7group.ProxyHelper.interceptAsSecondArgument;

/**
 * Key words for DSL like constructions
 * @author Dmytro Marchenko
 */
public class J7GroupPrepositions {
    private J7GroupPrepositions() {
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
        return interceptAsSecondArgument(entityClass);
    }
}
