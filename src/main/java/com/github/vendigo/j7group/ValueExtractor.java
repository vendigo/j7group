package com.github.vendigo.j7group;

interface ValueExtractor<T, V> {
    V extract(T entity);
}
