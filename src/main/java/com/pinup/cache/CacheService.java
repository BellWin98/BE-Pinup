package com.pinup.cache;

import java.util.function.Function;

public interface CacheService<K, V> {
    V getFromCache(K key, Function<K, V> dataProvider);
    void putInCache(K key, V value);
    void invalidateCache(K key);
    void syncCache(Function<K, V> dataProvider);
    String getCacheKey(K key);
}