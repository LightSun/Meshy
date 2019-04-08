package com.heaven7.java.message.protocal;

public interface MemberProxy{

    byte TYPE_BYTE = 1;
    byte TYPE_SHORT = 2;
    byte TYPE_INT = 3;
    byte TYPE_LONG = 4;
    byte TYPE_BOOLEAN = 5;
    byte TYPE_FLOAT = 6;
    byte TYPE_DOUBLE = 7;
    byte TYPE_STRING = 8;
    byte TYPE_OBJECT = 9;

    int getPriority();
    int getType();

    void setInt(Object obj, int value) throws IllegalAccessException;
    void setShort(Object obj, short value) throws IllegalAccessException;

    void setByte(Object obj, byte value) throws IllegalAccessException;

    void setLong(Object obj, long value) throws IllegalAccessException;

    void setBoolean(Object obj, boolean value) throws IllegalAccessException;

    void setFloat(Object obj, float value) throws IllegalAccessException;

    void setDouble(Object obj, double value) throws IllegalAccessException;

    void setString(Object obj, String value) throws IllegalAccessException;

    void setObject(Object obj, Object value) throws IllegalAccessException;

    byte getByte(Object obj) throws IllegalAccessException;
    int getInt(Object obj)  throws IllegalAccessException;
    short getShort(Object obj)  throws IllegalAccessException;
    long getLong(Object obj)  throws IllegalAccessException;
    boolean getBoolean(Object obj)  throws IllegalAccessException;
    float getFloat(Object obj)  throws IllegalAccessException;
    double getDouble(Object obj)  throws IllegalAccessException;
    String getString(Object obj) throws IllegalAccessException;
    Object getObject(Object obj)  throws IllegalAccessException;
}