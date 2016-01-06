package com.github.vendigo.j7group;

import java.util.Map;

interface GroupStrategy<K, V, C> {
    void handleFirstOccurrence(K key, V newValue, Map<K, C> map);

    void handleNonFirstOccurrence(K key, V newValue, C oldValue, Map<K, C> map);
}
