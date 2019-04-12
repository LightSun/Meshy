package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.FieldMembers;
import com.heaven7.java.message.protocol.anno.Inherit;

/**
 * the old version of person
 * @author heaven7
 */
@FieldMembers
public class Person1 implements IPerson {

    @Inherit
    private int age;
    @Inherit
    private String name;

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
