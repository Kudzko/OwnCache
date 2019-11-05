package by.andersen.kudko.cache.controller;

import by.andersen.kudko.cache.controller.testclass.Student;
import by.andersen.kudko.cache.impl.TwoLevelCacheClass;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Map;
import java.util.Set;


@Log4j2
public class MainTest {


    public static void main(String[] args) {
        int maxRamCacheCapacity = 2;
        int maxMemoryCacheCapacity = 2;
        int numberOfRequests = 2;

        TwoLevelCacheClass<Object, Student> cache = new TwoLevelCacheClass(maxRamCacheCapacity, numberOfRequests, maxMemoryCacheCapacity);


        Student student1 = new Student("Petr", "Petrov", 106215);
        Student student2 = new Student("Vasya", "Vasiliev", 106215);
        Student student3 = new Student("Ivan", "Ivanov", 106216);
        Student student4 = new Student("Segey", "Kuzmin", 106216);
        Student student5 = new Student("Test", "TEst", 106000);
        log.info("Objects: "+"\n"
                + student1.toString() + "\n"
                + student2.toString() + "\n"
                + student3.toString() + "\n"
                + student4.toString() + "\n"
                + student5.toString() + "\n"
        );

        try {
            cache.cache("a", student1);
            cache.cache("b", student2);
            cache.cache("c", student3);
            cache.cache("d", student4);
            cache.cache("e", student5);

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.trace("cache content" + cache.getCacheContent());
        log.trace("Loop for");
        for (Map.Entry<Object, Object> entry : cache.getCacheContent().entrySet()) {
            log.info(cache.getObject(entry.getKey()));
        }

        log.trace("get objects");
        log.trace(cache.getRamCache());
        log.trace(cache.getMemoryCache());
        log.trace("Most often used keys" + cache.getMostFrequentlyUsedKeys());

        log.trace("get object to use");
        log.trace(cache.getObjectToUse("d"));

        log.trace(cache.getRamCache());
        log.trace(cache.getMemoryCache());
        log.trace("Most often used keys" + cache.getMostFrequentlyUsedKeys());

        log.trace("get object to use");
        log.trace(cache.getObjectToUse("d"));

        log.trace(cache.getRamCache());
        log.trace(cache.getMemoryCache());
        log.trace("Most often used keys" + cache.getMostFrequentlyUsedKeys());

        log.trace("get object to use");
        log.trace(cache.getObjectToUse("e"));
        log.trace("Most often used keys" + cache.getMostFrequentlyUsedKeys());
        log.trace(cache.getRamCache());
        log.trace(cache.getMemoryCache());



        for (Map.Entry<Object, Object> entry : cache.getCacheContent().entrySet()) {
            log.info(entry.getKey() + "  " + entry.getValue());
        }

            }
}
