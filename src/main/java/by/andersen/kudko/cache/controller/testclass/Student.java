package by.andersen.kudko.cache.controller.testclass;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Serializable {
    private String name;
    private String surname;
    private int group;

    public Student() {
    }

    public Student(String name, String surname, int group) {
        this.name = name;
        this.surname = surname;
        this.group = group;
    }



}
