package com.heaven7.java.message.protocol;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.message.protocol.anno.FieldMember;

import java.lang.reflect.Field;

/**
 * the field proxy
 * @author heaven7
 */
/*public*/ class FieldProxy extends BaseMemberProxy implements MemberProxy {

    private final Field field;
    private final int type;
    private int priority;

    public FieldProxy(Field field, @Nullable FieldMember mm) {
        this.field = field;
        this.type = parseType(field.getGenericType());
        this.priority = mm != null ? mm.value() : 0;
    }

    @Override
    public String getPropertyName() {
        return field.getName();
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
    public void setShort(Object obj, short value)throws IllegalAccessException  {
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
    public void setChar(Object obj, char value) throws IllegalAccessException{
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
    public short getShort(Object obj)throws IllegalAccessException   {
        return field.getShort(obj);
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
    @Override
    public char getChar(Object obj) throws IllegalAccessException {
        return field.getChar(obj);
    }
}
