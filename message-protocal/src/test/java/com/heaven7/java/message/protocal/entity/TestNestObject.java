package com.heaven7.java.message.protocal.entity;

import com.heaven7.java.message.protocal.anno.FieldMembers;

import java.util.Objects;

@FieldMembers
public class TestNestObject {

    private String code;
    private TestEntity3 entity;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public TestEntity3 getEntity() {
        return entity;
    }

    public void setEntity(TestEntity3 entity) {
        this.entity = entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestNestObject that = (TestNestObject) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, entity);
    }
}
