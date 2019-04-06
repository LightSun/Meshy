package com.heaven7.java.message.protocal;

public interface FieldProxy {

    byte TYPE_BYTE = 1;
    byte TYPE_SHORT_INT = 2;
    byte TYPE_INT = 3;
    byte TYPE_LONG = 4;
    byte TYPE_BOOLEANM = 5;
    byte TYPE_FLOAT = 6;
    byte TYPE_DOUBLE = 7;
    byte TYPE_OBJECT = 8;

    int getType();

    void setValue(Object obj, int value);

    void setValue(Object obj, byte value);

    void setValue(Object obj, long value);

    void setValue(Object obj, boolean value);

    void setValue(Object obj, float value);

    void setValue(Object obj, double value);

    void setValue(Object obj, String value);
}