package com.github.vendigo.j7group;

public interface ValueExtractor<T, V> {
    V extract(T entity);
}
