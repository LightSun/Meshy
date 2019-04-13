package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.FieldMembers;
import com.heaven7.java.message.protocol.anno.Inherit;

import java.util.Objects;

/**
 * the old version of person
 * @author heaven7
 */
@FieldMembers
public class Person implements IPerson {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person1 = (Person) o;
        return age == person1.age &&
                Objects.equals(name, person1.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(age, name);
    }
}
