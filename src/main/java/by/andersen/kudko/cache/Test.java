package by.andersen.kudko.cache;

import by.andersen.kudko.cache.controller.testclass.Student;

import java.util.HashSet;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Set<Student> test = new HashSet<>();
        Student student1 = new Student("Petr", "Petrov", 106215);
        Student student2 = new Student("Vasya", "Vasiliev", 106216);
        Student student3 = new Student("Ivan", "Ivanov", 106217);
        Student student4 = new Student("Segey", "Kuzmin", 106218);
        Student student5 = new Student("Segey", "Kuzmin", 106218);
        test.add(student1);
        test.add(student4);
        test.add(student2);
        test.add(student3);
        test.add(student5);

        System.out.println(test);
    }
}
