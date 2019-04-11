package com.heaven7.java.message.protocol;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * the type adapter which used to write and read message members.
 * @author heaven7
 */
public abstract class TypeAdapter{

    /**
     * write the member by the sink.
     * @param sink the out sink
     * @param obj the object
     * @param proxy the member proxy which can get and set value
     * @return the write length as bytes count.
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if occurs from reflect
     * @throws InvocationTargetException if occurs from reflect
     */
    public abstract int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException,IllegalAccessException, InvocationTargetException;
    /**
     * read the value from source and set to the object.
     * @param source the input source
     * @param obj the object
     * @param proxy the member proxy which can get and set value
     * @throws IOException if an I/O error occurs
     * @throws IllegalAccessException if occurs from reflect
     * @throws InvocationTargetException if occurs from reflect
     */
    public abstract void read(BufferedSource source, Object obj, MemberProxy proxy) throws IOException,IllegalAccessException, InvocationTargetException;

    /**
     * evaluate the size of target member from object
     * @param obj the object
     * @param proxy the member proxy
     * @return the size as bytes count
     * @throws IllegalAccessException if occurs from reflect
     * @throws InvocationTargetException if occurs from reflect
     */
    public abstract int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException;

}
