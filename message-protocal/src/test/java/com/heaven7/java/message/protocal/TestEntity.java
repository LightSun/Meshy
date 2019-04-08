package com.heaven7.java.message.protocal;

import java.lang.reflect.Field;

/**
 * @author heaven7
 */
public class TestEntity {

    private byte arg1;

    private short arg2;
    private int arg3;
    private long arg4;
    private boolean arg5;


    public static void main(String[] args) {
        Field[] fields = TestEntity.class.getDeclaredFields();
        System.out.println(fields);
    }
}
