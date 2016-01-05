package com.github.vendigo.j7group;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestCollections {
    private TestCollections() {}

    @SafeVarargs
    public static <T> Set<T> setOf(T... elems) {
        Set<T> resultSet = new HashSet<>(elems.length);
        resultSet.addAll(Arrays.asList(elems));
        return resultSet;
    }
}
