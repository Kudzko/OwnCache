package by.andersen.kudko.cache;

import java.util.Set;

public interface IFrequencyCallObject<KeyType> {
    Set<KeyType> getMostFrequentlyUsedKeys();
    int getFrequencyObjectCalling(KeyType key);
}
