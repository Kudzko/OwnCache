package by.andersen.kudko.cache;

import java.io.IOException;

public interface ICache<KeyType, ValueType> {
    void cache(KeyType key, ValueType value) throws IOException;

    ValueType getObjectToUse(KeyType key) throws IndexOutOfBoundsException;
    /**
     * rerurn object by key without changing counter  of callings
     *
     * @param key
     * @return
     * @throws IndexOutOfBoundsException
     */
    ValueType getObject(KeyType key) throws IndexOutOfBoundsException;

    /**
     * Returns object by key and increase counter of callings
     *
     * @param key
     * @return Object by key
     * @throws IndexOutOfBoundsException
     */
    void deleteObject(KeyType key);
    void clearCache();
    ValueType removeObject(KeyType key);
    boolean containsKey(KeyType key);
    int size();
}
