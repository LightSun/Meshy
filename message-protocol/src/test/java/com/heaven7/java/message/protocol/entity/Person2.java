package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.CompatKeyClass;
import com.heaven7.java.message.protocol.anno.FieldMembers;

import java.util.Objects;

/**
 * @author heaven7
 */
@CompatKeyClass(Person.class)
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
