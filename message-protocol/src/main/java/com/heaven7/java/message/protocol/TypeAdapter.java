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
     * @return the write length as bytes count.
     * @throws IOException if an I/O error occurs
     */
    public abstract int write(BufferedSink sink, Object obj) throws IOException;
    /**
     * read the value from source and set to the object.
     * @param source the input source
     * @throws IOException if an I/O error occurs
     */
    public abstract Object read(BufferedSource source) throws IOException;

    /**
     * evaluate the size of target member from object
     * @param obj the object
     * @return the size as bytes count
     */
    public abstract int evaluateSize(Object obj);

}
