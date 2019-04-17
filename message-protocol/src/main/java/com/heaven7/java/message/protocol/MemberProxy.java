package com.heaven7.java.message.protocol;

import java.lang.reflect.InvocationTargetException;

public interface MemberProxy{

    int TYPE_BYTE = 1;
    int TYPE_SHORT = 2;
    int TYPE_INT = 3;
    int TYPE_LONG = 4;
    int TYPE_BOOLEAN = 5;
    int TYPE_CHAR   = 6;
    int TYPE_FLOAT = 7;
    int TYPE_DOUBLE = 8;

    byte TYPE_STRING = 9;

    byte FLAG_PACKED = 1;

    int getPriority();

    String getPropertyName();

    Class<?> getOwnerClass();

    TypeAdapter getTypeAdapter(MessageProtocolContext mpContext, TypeAdapterContext context, float applyVersion);

    void setObject(Object obj, Object value) throws IllegalAccessException, InvocationTargetException;
    Object getObject(Object obj)  throws IllegalAccessException, InvocationTargetException;

}