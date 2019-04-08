package com.heaven7.java.message.protocal;

public class Message<T> {

    public static final int LOGIN  = 1;
    public static final int LOGOUT = 2;
    public static final int TICK   = 3;
    public static final int COMMON = 4;

    private int type;
    private String msg;
    private T entity;

    public static <T> Message<T> create(int type, String str, T object){
        Message<T> msg = new Message<>();
        msg.setType(type);
        msg.setMsg(str);
        msg.setEntity(object);
        return msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
