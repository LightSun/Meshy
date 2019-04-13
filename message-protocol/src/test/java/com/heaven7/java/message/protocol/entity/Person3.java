package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.FieldMembers;

import java.util.Objects;

/**
 * @author heaven7
 */
@FieldMembers
public class Person3 {

    private String name;

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
        Person3 person3 = (Person3) o;
        return Objects.equals(name, person3.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
