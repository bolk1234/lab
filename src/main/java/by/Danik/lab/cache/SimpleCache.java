package by.Danik.lab.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Универсальный кеш (Для каждого типа запросов, надо создать бин в APPConfig)
 * @param <K> - ключ
 * @param <V> - значение
 */
public class SimpleCache<K, V> {

    private final Map<K, V> cache = new ConcurrentHashMap<>();

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public void clear() {
        cache.clear();
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}
