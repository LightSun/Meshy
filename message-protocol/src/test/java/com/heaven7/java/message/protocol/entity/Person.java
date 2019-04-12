package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.FieldMembers;

import java.util.Objects;

/**
 * @author heaven7
 */
@FieldMembers
public class Person extends Person1 implements IPerson {

    private String auchor;

    public String getAuchor() {
        return auchor;
    }
    public void setAuchor(String auchor) {
        this.auchor = auchor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(auchor, person.auchor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auchor);
    }
}
