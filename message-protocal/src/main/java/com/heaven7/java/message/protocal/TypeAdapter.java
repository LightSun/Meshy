package com.heaven7.java.message.protocal;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author heaven7
 */
public abstract class TypeAdapter{

    public abstract int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException,IllegalAccessException, InvocationTargetException;
    public abstract void read(BufferedSource sink, Object obj, MemberProxy proxy) throws IOException,IllegalAccessException, InvocationTargetException;
    public abstract int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException;

}
