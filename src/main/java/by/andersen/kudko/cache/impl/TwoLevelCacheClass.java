package by.andersen.kudko.cache.impl;

import by.andersen.kudko.cache.IleveredCache;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Log4j2
public class TwoLevelCacheClass<KeyType, ValueType extends Serializable> implements IleveredCache<KeyType, ValueType> {
    private RamCacheClass<KeyType, ValueType> ramCache;
    private MemoryCacheClass<KeyType, ValueType> memoryCache;
    private int maxRamCacheCapacity;
    private int maxMemoryCacheCapacity;
    private int numberOfRequests;
    private int numberRequestsForRecahce;

    public TwoLevelCacheClass() {
    }

    public TwoLevelCacheClass(int maxRamCacheCapacity, int numberRequestsForRecahce, int maxMemoryCacheCapacity) {
        this.maxRamCacheCapacity = maxRamCacheCapacity;
        this.numberRequestsForRecahce = numberRequestsForRecahce;
        this.maxMemoryCacheCapacity = maxMemoryCacheCapacity;
        ramCache = new RamCacheClass<KeyType, ValueType>();
        memoryCache = new MemoryCacheClass<KeyType, ValueType>();
        numberOfRequests = 0;
    }


    @Override
    public void recache() {

        int boundFrecquency = 0;

        // вычисление среднего арифметического для отбрасывания редко вызываемых объектов
        boundFrecquency = average(ramCache);

        for (KeyType key : ramCache.getMostFrequentlyUsedKeys()) {
            if (ramCache.getFrequencyObjectCalling(key) <= boundFrecquency) {
                ValueType value = ramCache.removeObject(key);
                memoryCache.cache(key, value);
                log.trace("moved to memory cache", value);
            }
        }

        for (KeyType key : memoryCache.getMostFrequentlyUsedKeys()) {

            if (memoryCache.getFrequencyObjectCalling(key) > boundFrecquency
                    && ramCache.size() <= maxRamCacheCapacity) {
                ValueType value = memoryCache.removeObject(key);
                ramCache.cache(key, value);
                log.trace("moved to ram cache", value);
            }
        }

    }

    private int average(RamCacheClass ramCache) {

        int average = 0;
        Set mostFrequentlyUsedKeys = this.getMostFrequentlyUsedKeys();
        for (Object key : mostFrequentlyUsedKeys) {
            average += ramCache.getFrequencyObjectCalling(key);
        }

        average /= mostFrequentlyUsedKeys.size();
        log.trace("avarage = " + average);
        return average;
    }

    @Override
    public void cache(KeyType key, ValueType value) throws IOException {
        if (ramCache.size() < maxRamCacheCapacity) {
            ramCache.cache(key, value);
            log.trace("Object added to ram cache", value);
        } else if (memoryCache.size() < maxMemoryCacheCapacity) {
            memoryCache.cache(key, value);
            log.trace("Object added to memory cache", value);
        } else {
            deleteMinUsedObject();
            log.trace("Object with min frequency using deleted from memory cache", value);
            memoryCache.cache(key, value);
            log.trace("Object added to memory cache", value);
        }

    }


    private void deleteMinUsedObject() {
        int minFrequency = 0;
        KeyType minFrequencyKey;

        KeyType key = (KeyType) memoryCache.getMostFrequentlyUsedKeys().stream().min((key1, key2)
                -> (memoryCache.getFrequencyObjectCalling(key1) - memoryCache.getFrequencyObjectCalling(key2))
        );

        memoryCache.deleteObject(key);

    }

    @Override
    public ValueType getObject(KeyType key) throws IndexOutOfBoundsException {
        ValueType object = ramCache.getObject(key);
        if (object == null) {
            object = memoryCache.getObject(key);
        }
        return object;
    }

    @Override
    public ValueType getObjectToUse(KeyType key) throws IndexOutOfBoundsException {
        ValueType object = ramCache.getObjectToUse(key);
        if (object == null) {
            object = memoryCache.getObjectToUse(key);
        }
        if (object != null) {
            numberOfRequests++;
            checkRecache(numberOfRequests);
        }
        return object;
    }

    private boolean checkRecache(int numberOfRequests) {
        if (numberOfRequests > numberRequestsForRecahce) {
            this.recache();
            numberOfRequests = 0;
            return true;
        }
        return false;
    }

    @Override
    public void deleteObject(KeyType key) {
        if (ramCache.containsKey(key)) {
            ramCache.deleteObject(key);
        } else if (memoryCache.containsKey(key)) {
            memoryCache.deleteObject(key);
        }
    }

    @Override
    public void clearCache() {
        memoryCache.clearCache();
        ramCache.clearCache();
    }

    @Override
    public ValueType removeObject(KeyType key) {
        ValueType removingObject = null;
        if (ramCache.containsKey(key)) {
            removingObject = ramCache.removeObject(key);
        } else if (memoryCache.containsKey(key)) {
            removingObject = memoryCache.removeObject(key);
        }
        return removingObject;
    }

    @Override
    public boolean containsKey(KeyType key) {
        boolean isContain = false;
        isContain = ramCache.containsKey(key);
        if (!isContain) {
            isContain = memoryCache.containsKey(key);
        }
        return isContain;
    }

    @Override
    public int size() {
        return ramCache.size() + memoryCache.size();
    }

    @Override
    public Set<KeyType> getMostFrequentlyUsedKeys() {
        Set<KeyType> ramKeys = ramCache.getMostFrequentlyUsedKeys();
        Set<KeyType> memoryKeys = memoryCache.getMostFrequentlyUsedKeys();
        Set<KeyType> mostFrequentlyUsedKeys = new HashSet<>();
        mostFrequentlyUsedKeys.addAll(ramKeys);
        mostFrequentlyUsedKeys.addAll(memoryKeys);
        return mostFrequentlyUsedKeys;
    }

    @Override
    public int getFrequencyObjectCalling(KeyType key) {
        int frequency;
        frequency = ramCache.getFrequencyObjectCalling(key);
        if (frequency == -1) {
            frequency = memoryCache.getFrequencyObjectCalling(key);
        }
        return frequency;
    }

    public Map<KeyType, Object> getCacheContent() {
        Map<KeyType, Object> result = new HashMap<>();
        result.putAll(ramCache.getCacheContent());
        result.putAll(memoryCache.getCacheContent());
        return result;
    }

    public Map<KeyType, ValueType> getRamCache() {
        return ramCache.getCacheContent();
    }

    ;

    public Map<KeyType, String> getMemoryCache() {
        return memoryCache.getCacheContent();
    }

    ;

}
