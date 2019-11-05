package by.andersen.kudko.cache;


public interface IleveredCache<KeyType, ValueType> extends ICache<KeyType, ValueType>, IFrequencyCallObject<KeyType> {
    void recache();
}
