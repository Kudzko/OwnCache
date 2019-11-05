package by.andersen.kudko.cache.impl;

import by.andersen.kudko.cache.ICache;
import by.andersen.kudko.cache.IFrequencyCallObject;

import java.io.IOException;
import java.util.*;

public class RamCacheClass<KeyType, ValueType> extends AbstractCache<KeyType> implements ICache<KeyType, ValueType>, IFrequencyCallObject<KeyType> {
    private Map<KeyType, ValueType> cache;


    public RamCacheClass() {
        this.cache = new HashMap<>();
    }

    @Override
    public void cache(KeyType key, ValueType value) {
        frequencyMap.put(key, 1);
        cache.put(key, value);
    }

    /**
     * Returns object by key and increase counter of callings
     *
     * @param key
     * @return Object by key
     * @throws IndexOutOfBoundsException
     */
    @Override
    public ValueType getObjectToUse(KeyType key) throws IndexOutOfBoundsException {

        frequencyMap.entrySet().stream().filter(elem -> elem.getKey().equals(key)).forEach(e -> {
            int temp = e.getValue();
            temp++;
            e.setValue(temp);
        });

        return cache.get(key);
    }

    /**
     * rerurn object by key without changing counter  of callings
     *
     * @param key
     * @return
     * @throws IndexOutOfBoundsException
     */
    @Override
    public ValueType getObject(KeyType key) throws IndexOutOfBoundsException {
        return cache.get(key);
    }

    @Override
    public void deleteObject(KeyType key) {
        cache.remove(key);
        frequencyMap.remove(key);
    }

    @Override
    public void clearCache() {
        cache.clear();
        frequencyMap.clear();
    }

    @Override
    public ValueType removeObject(KeyType key) {
        frequencyMap.remove(key);
        return cache.remove(key);
    }

    @Override
    public boolean containsKey(KeyType key) {
        return cache.containsKey(key);
    }

    @Override
    public int size() {
        return cache.size();
    }

    /**
     *
     * @return copy of inner map cache Map<KeyType, ValueType>
     */
    public Map<KeyType, ValueType> getCacheContent() {
        Map<KeyType, ValueType> content = new HashMap<>(cache);
        return content;
    }

    /**
     *
     * @return inner map cache Map<KeyType, ValueType>
     */
    public Map<KeyType, ValueType> getCache() {
        return cache;
    }


}
