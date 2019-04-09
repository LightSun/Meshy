package com.heaven7.java.message.protocal;

import com.heaven7.java.message.protocal.anno.MethodMember;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * the method proxy
 * @author heaven7
 */
/*public*/ class MethodProxy extends BaseMemberProxy implements MemberProxy {

    private final Method get;
    private final Method set;
    private final int priority;
    private final int type;

    //method param count: must be one ,and must support message protocal.
    public MethodProxy(Method get, Method set) {
        this.get = get;
        this.set = set;
        this.priority = get.getAnnotation(MethodMember.class).priority();
        this.type = parseType(get.getReturnType());
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
    public void setInt(Object obj, int value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setShort(Object obj, short value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setByte(Object obj, byte value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setLong(Object obj, long value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setBoolean(Object obj, boolean value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setFloat(Object obj, float value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setDouble(Object obj, double value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setString(Object obj, String value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setObject(Object obj, Object value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public void setChar(Object obj, char value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }

    @Override
    public byte getByte(Object obj) throws IllegalAccessException, InvocationTargetException{
        return (byte) get.invoke(obj);
    }

    @Override
    public int getInt(Object obj) throws IllegalAccessException , InvocationTargetException{
        return (int) get.invoke(obj);
    }

    @Override
    public short getShort(Object obj) throws IllegalAccessException, InvocationTargetException {
        return (short) get.invoke(obj);
    }

    @Override
    public long getLong(Object obj) throws IllegalAccessException , InvocationTargetException{
        return (long) get.invoke(obj);
    }

    @Override
    public boolean getBoolean(Object obj) throws IllegalAccessException, InvocationTargetException {
        return (boolean) get.invoke(obj);
    }

    @Override
    public float getFloat(Object obj) throws IllegalAccessException, InvocationTargetException {
        return (float) get.invoke(obj);
    }

    @Override
    public double getDouble(Object obj) throws IllegalAccessException, InvocationTargetException {
        return (double) get.invoke(obj);
    }

    @Override
    public String getString(Object obj) throws IllegalAccessException, InvocationTargetException {
        return (String) get.invoke(obj);
    }
    @Override
    public Object getObject(Object obj) throws IllegalAccessException, InvocationTargetException {
        return get.invoke(obj);
    }

    @Override
    public char getChar(Object obj) throws IllegalAccessException, InvocationTargetException {
        return (char) get.invoke(obj);
    }
}
