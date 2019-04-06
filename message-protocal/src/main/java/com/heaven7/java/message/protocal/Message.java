package com.heaven7.java.message.protocal;

public class Message<T> {

    public static final int LOGIN  = 1;
    public static final int LOGOUT = 2;
    public static final int TICK   = 3;
    public static final int COMMON = 4;

    private int type;
    private String fullName;
    private T entity;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
