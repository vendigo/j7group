package com.github.vendigo.j7group;

import static com.github.vendigo.j7group.ProxyHelper.extractSecondArgument;

class SecondArgumentValueExtractor<T, V> implements ValueExtractor<T, V> {
    @Override
    public V extract(T entity) {
        return extractSecondArgument(entity);
    }
}
