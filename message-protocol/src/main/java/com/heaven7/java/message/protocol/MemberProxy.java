package com.heaven7.java.message.protocol;

import java.lang.reflect.InvocationTargetException;

public interface MemberProxy{

    byte TYPE_BYTE = 1;
    byte TYPE_SHORT = 2;
    byte TYPE_INT = 3;
    byte TYPE_LONG = 4;
    byte TYPE_BOOLEAN = 5;
    byte TYPE_CHAR   = 6;
    byte TYPE_FLOAT = 7;
    byte TYPE_DOUBLE = 8;

    byte TYPE_STRING = 9;
    byte TYPE_OBJECT = 10;

    byte TYPE_LIST  = 11;
    byte TYPE_ARRAY = 12;
    byte TYPE_SET   = 12;
    byte TYPE_MAP   = 13;

    int getPriority();
    int getType();

    /**
     * indicate is packed type for primitive or not.
     * @return true if is packed.
     */
    boolean isPackedType();

    String getPropertyName();

    void setInt(Object obj, int value) throws IllegalAccessException, InvocationTargetException;
    void setShort(Object obj, short value) throws IllegalAccessException, InvocationTargetException;
    void setByte(Object obj, byte value) throws IllegalAccessException, InvocationTargetException;
    void setLong(Object obj, long value) throws IllegalAccessException, InvocationTargetException;

    void setBoolean(Object obj, boolean value) throws IllegalAccessException, InvocationTargetException;
    void setFloat(Object obj, float value) throws IllegalAccessException, InvocationTargetException;
    void setDouble(Object obj, double value) throws IllegalAccessException, InvocationTargetException;
    void setChar(Object obj, char value) throws IllegalAccessException, InvocationTargetException;

    void setString(Object obj, String value) throws IllegalAccessException, InvocationTargetException;
    void setObject(Object obj, Object value) throws IllegalAccessException, InvocationTargetException;

    byte getByte(Object obj) throws IllegalAccessException, InvocationTargetException;
    int getInt(Object obj)  throws IllegalAccessException , InvocationTargetException;
    short getShort(Object obj)  throws IllegalAccessException , InvocationTargetException;
    long getLong(Object obj)  throws IllegalAccessException , InvocationTargetException;

    boolean getBoolean(Object obj)  throws IllegalAccessException, InvocationTargetException;
    float getFloat(Object obj)  throws IllegalAccessException , InvocationTargetException;
    double getDouble(Object obj)  throws IllegalAccessException , InvocationTargetException;
    char getChar(Object obj) throws IllegalAccessException, InvocationTargetException;

    String getString(Object obj) throws IllegalAccessException , InvocationTargetException;
    Object getObject(Object obj)  throws IllegalAccessException, InvocationTargetException;

}