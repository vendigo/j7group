package com.github.vendigo.j7group;

class EntityAsValueExtractor<V> implements ValueExtractor<V, V> {
    @Override
    public V extract(V entity) {
        return entity;
    }
}
