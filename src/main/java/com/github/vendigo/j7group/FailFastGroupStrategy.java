package com.github.vendigo.j7group;

import java.util.Map;

public class FailFastGroupStrategy<K, V> implements GroupStrategy<K, V, V> {
    @Override
    public void handleFirstOccurrence(K key, V newValue, Map<K, V> map) {
        map.put(key, newValue);
    }

    @Override
    public void handleNonFirstOccurrence(K key, V newValue, V oldValue, Map<K, V> map) {
        throw new KeyAmbiguityException("Key: "+key+" has more than one value");
    }
}
