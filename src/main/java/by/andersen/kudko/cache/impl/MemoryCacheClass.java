package by.andersen.kudko.cache.impl;

import by.andersen.kudko.cache.ICache;
import by.andersen.kudko.cache.IFrequencyCallObject;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.util.*;

@Log4j2
public class MemoryCacheClass<KeyType, ValueType extends Serializable> extends AbstractCache<KeyType> implements ICache<KeyType, ValueType>, IFrequencyCallObject<KeyType> {

    Map<KeyType, String> cache;

    public MemoryCacheClass() {
        this.cache = new HashMap<>();
        this.frequencyMap = new TreeMap<>();

        File tempFolder = new File("temp\\");
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
    }

    @Override
    public void cache(KeyType key, ValueType value) {
        String pathObject;
        pathObject = "temp\\" + UUID.randomUUID().toString() + ".temp";

        frequencyMap.put(key, 1);
        cache.put(key, pathObject);

        ObjectOutputStream objectOutputStream = null;
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(pathObject);
            // don't clear. for what FileOutputStream give to ObjectOutputStream
            objectOutputStream = new ObjectOutputStream(fileOut);

            objectOutputStream.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectOutputStream != null) {

                    objectOutputStream.flush();
                    objectOutputStream.close();
                }

                if (objectOutputStream != null) {
                    fileOut.flush();
                    fileOut.close();
                }
            } catch (IOException e) {
                log.warn("Stream closed incorrectly", e);

            }
        }


    }


    @Override
    public ValueType getObjectToUse(KeyType key) throws IndexOutOfBoundsException {

        if (cache.containsKey(key)) {

            String pathToObject = cache.get(key);

            FileInputStream fileInput = null;
            ObjectInputStream objectStream = null;
            ValueType deserializedObject;
            try {
                fileInput = new FileInputStream(pathToObject);
                objectStream = new ObjectInputStream(fileInput);


                deserializedObject = (ValueType) objectStream.readObject();

                int frecquency = frequencyMap.remove(key);
                frequencyMap.put(key, ++frecquency);


                return deserializedObject;
            } catch (FileNotFoundException e) {
                log.warn(e);
            } catch (IOException e) {
                log.warn(e);
            } catch (ClassNotFoundException e) {
                log.warn(e);
            } finally {
                try {
                    if (fileInput != null) {
                        fileInput.close();
                    }
                    if (objectStream != null) {
                        objectStream.close();
                    }
                } catch (IOException e) {
                    log.warn("streams don't exist", e);
                }
            }
        }

        return null;
    }

    @Override
    public ValueType getObject(KeyType key) throws IndexOutOfBoundsException {
        if (cache.containsKey(key)) {

            String pathToObject = cache.get(key);

            FileInputStream fileInput = null;
            ObjectInputStream objectStream = null;
            ValueType deserializedObject;
            try {
                fileInput = new FileInputStream(pathToObject);
                objectStream = new ObjectInputStream(fileInput);


                deserializedObject = (ValueType) objectStream.readObject();

                 return deserializedObject;
            } catch (FileNotFoundException e) {
                log.warn(e);
            } catch (IOException e) {
                log.warn(e);
            } catch (ClassNotFoundException e) {
                log.warn(e);
            } finally {
                try {
                    if (fileInput != null) {
                        fileInput.close();
                    }
                    if (objectStream != null) {
                        objectStream.close();
                    }
                } catch (IOException e) {
                    log.warn("streams don't exist", e);
                }
            }
        }

        return null;
    }

    @Override
    public void deleteObject(KeyType key) {
        String deletingObj;
        deletingObj = cache.remove(key);
        if (deletingObj != null) {
            try {
                File deletingFile = new File(deletingObj);
                frequencyMap.remove(key);
                deletingFile.delete();
            } catch (SecurityException e) {
                log.warn("Can not delete file", e);
            }
        }
    }

    @Override
    public void clearCache() {
        try {
            File deletingFile;
            for (Map.Entry entry : cache.entrySet()) {
                deletingFile = new File(entry.getValue().toString());
                deletingFile.delete();
            }
        } catch (SecurityException e) {
            log.warn("File haven't removed from dir", e);
        }
        cache.clear();
        frequencyMap.clear();
    }

    @Override
    public ValueType removeObject(KeyType key) {
        ValueType result = this.getObject(key);
        this.deleteObject(key);
        return result;
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
     * @return copy of inner map cache Map<KeyType, String>
     */
    public  Map<KeyType, String> getCacheContent(){
        Map<KeyType, String>  content = new HashMap<>(cache);
        return content;
    }

    /**
     *
     * @return inner map cache  Map<KeyType, String>
     */
    public Map<KeyType, String> getMemoryCache(){
        return cache;
    }


}
