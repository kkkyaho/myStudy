package com.mystudy.lucenetest.dto;

import lombok.Getter;

@Getter

public class Person {
    private String id;
    private String name;
    private long age;

    public Person(String id, String name, long age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
