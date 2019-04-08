package com.heaven7.java.message.protocal;

import java.lang.reflect.Field;

/**
 * @author heaven7
 */
public class FieldProxy implements MemberProxy {

    private final Field field;
    private final int type;
    private int priority;

    public FieldProxy(Field field, MessageMember mm) {
        this.field = field;
        this.type = parseType(field);
        this.priority = mm.value();
    }

    private static int parseType(Field field) {
        if(field.getType() == byte.class){
            return TYPE_BYTE;
        }
        else if(field.getType() == int.class){
            return TYPE_INT;
        }
        else if(field.getType() == long.class){
            return TYPE_LONG;
        }
        else if(field.getType() == float.class){
            return TYPE_FLOAT;
        }
        else if(field.getType() == double.class){
            return TYPE_DOUBLE;
        }
        else if(field.getType() == boolean.class){
            return TYPE_BOOLEAN;
        }
        else if(field.getType() == String.class){
            return TYPE_STRING;
        }
        return TYPE_OBJECT;
    }

    @Override
    public int getPriority() {
        return priority;
    }
    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setInt(Object obj, int value)throws IllegalAccessException {
        field.set(obj, value);
    }
    @Override
    public void setByte(Object obj, byte value) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public void setLong(Object obj, long value) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public void setBoolean(Object obj, boolean value)throws IllegalAccessException  {
        field.set(obj, value);
    }

    @Override
    public void setFloat(Object obj, float value) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public void setDouble(Object obj, double value) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public void setString(Object obj, String value)throws IllegalAccessException  {
        field.set(obj, value);
    }

    @Override
    public void setObject(Object obj, Object value) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public byte getByte(Object obj) throws IllegalAccessException {
        return field.getByte(obj);
    }

    @Override
    public int getInt(Object obj) throws IllegalAccessException  {
        return field.getInt(obj);
    }

    @Override
    public long getLong(Object obj)throws IllegalAccessException   {
        return field.getLong(obj);
    }

    @Override
    public boolean getBoolean(Object obj)throws IllegalAccessException   {
        return field.getBoolean(obj);
    }

    @Override
    public float getFloat(Object obj)throws IllegalAccessException  {
        return field.getFloat(obj);
    }

    @Override
    public double getDouble(Object obj) throws IllegalAccessException {
        return field.getDouble(obj);
    }

    @Override
    public String getString(Object obj) throws IllegalAccessException {
        return (String) field.get(obj);
    }

    @Override
    public Object getObject(Object obj) throws IllegalAccessException  {
        return field.get(obj);
    }
}
