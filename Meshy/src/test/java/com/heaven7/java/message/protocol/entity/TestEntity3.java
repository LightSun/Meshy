package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.FieldMember;
import com.heaven7.java.message.protocol.anno.Inherit;
import com.heaven7.java.message.protocol.anno.MethodMember;

import java.util.Objects;

public class TestEntity3 {

    @FieldMember
    private int age;

    //as a member by method
    private String name;

    @MethodMember
    public void setName(String name){
        this.name = name;
    }
    @Inherit
    @MethodMember(MethodMember.TYPE_GET)
    public String getName(){
        return name;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity3 entity3 = (TestEntity3) o;
        return age == entity3.age &&
                Objects.equals(name, entity3.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name);
    }
}
