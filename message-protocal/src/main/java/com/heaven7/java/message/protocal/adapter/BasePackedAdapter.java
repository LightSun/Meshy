package com.heaven7.java.message.protocal.adapter;

import com.heaven7.java.message.protocal.MemberProxy;
import com.heaven7.java.message.protocal.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * the base packed adapter for packed primitive type.
 * @author heaven7
 */
public abstract class BasePackedAdapter extends TypeAdapter {

    @Override
    public final int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        Object value = proxy.getObject(obj);
        if(value == null){
            sink.writeByte(0);
            return 1;
        }else {
            sink.writeByte(1);
            return writeValue(sink, value) + 1;
        }
    }

    @Override
    public final void read(BufferedSource sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException{
        boolean isNull = sink.readByte() == 0;
        if(isNull){
            proxy.setObject(obj, null);
        }else {
            proxy.setObject(obj, readValue(sink));
        }
    }

    @Override
    public final int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException {
        Object value = proxy.getObject(obj);
        return value != null ? evaluateSize(value) + 1 : 1;
    }

    /**
     * write the value to the sink
     * @param sink the out sink
     * @param value the value. never null
     * @return the write size as byte count
     * @throws IOException if an I/O error occurs.
     */
    protected abstract int writeValue(BufferedSink sink, Object value) throws IOException;

    /**
     * read the value from source
     * @param source the source
     * @return the value . never null
     * @throws IOException if an I/O error occurs.
     */
    protected abstract Object readValue(BufferedSource source) throws IOException;

    /**
     * evaluate the size of target value in message-protocal
     * @param value the value . never null
     * @return the size as byte count
     */
    protected abstract int evaluateSize(Object value);
}
