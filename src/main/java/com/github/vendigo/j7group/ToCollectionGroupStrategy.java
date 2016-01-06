package com.github.vendigo.j7group;

import java.util.Collection;
import java.util.Map;

import static com.github.vendigo.j7group.GroupHelper.createCollectionInstance;

class ToCollectionGroupStrategy<K, V, C extends Collection<V>> implements GroupStrategy<K, V, C> {

    private final Class<? extends Collection> collectionClass;
    private final int initialCapacity;

    public ToCollectionGroupStrategy(Class<? extends Collection> collectionClass, int initialCapacity) {
        this.collectionClass = collectionClass;
        this.initialCapacity = initialCapacity;
    }

    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Override
    public void handleFirstOccurrence(K key, V newValue, Map<K, C> map) {
        C valuesForKey = createCollectionInstance(collectionClass, initialCapacity);
        valuesForKey.add(newValue);
        map.put(key, valuesForKey);
    }

    @Override
    public void handleNonFirstOccurrence(K key, V newValue, C oldValue, Map<K, C> map) {
        oldValue.add(newValue);
    }
}
