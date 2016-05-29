package com.example.venkatneehar.mainpage;

/**
 * Created by venkatneehar on 4/22/2015.
 */
public class Person {
    Integer filename;
    String name;

    Person(String name, Integer filename){
        this.filename=filename;
        this.name=name;
    }

    public Integer getFilename() {
        return filename;
    }

    public String getName() {
        return name;
    }
}
