package com.heaven7.java.meshy.entity;

import com.heaven7.java.meshy.anno.FieldMembers;

import java.util.Objects;

/**
 * @author heaven7
 */
@FieldMembers
public class Person2 extends Person implements IPerson {

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
        if (!super.equals(o)) return false;
        Person2 person = (Person2) o;
        return Objects.equals(auchor, person.auchor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), auchor);
    }
}
