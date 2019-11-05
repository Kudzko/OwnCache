package by.andersen.kudko.cache.impl;

import by.andersen.kudko.cache.IFrequencyCallObject;
import by.andersen.kudko.cache.comparator.ComparatorClass;
import lombok.extern.log4j.Log4j2;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
@Log4j2
public abstract class AbstractCache<KeyType> implements IFrequencyCallObject<KeyType> {
    protected SortedMap<KeyType, Integer> frequencyMap;

    public AbstractCache() {
        this.frequencyMap = new TreeMap<>();
    }

    @Override
    public Set<KeyType> getMostFrequentlyUsedKeys() {
        ComparatorClass comparator = new ComparatorClass(frequencyMap);
        TreeMap<KeyType,Integer> sorted = new TreeMap(comparator);
        sorted.putAll(frequencyMap);

        return sorted.keySet();
    }

    @Override
    public int getFrequencyObjectCalling(KeyType key) {
        return frequencyMap.getOrDefault(key, -1);
    }
}
