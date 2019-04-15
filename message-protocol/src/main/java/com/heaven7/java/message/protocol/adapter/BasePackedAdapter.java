package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * the base packed adapter for packed primitive type.
 * @author heaven7
 */
public abstract class BasePackedAdapter extends TypeAdapter {

    @Override
    public final int write(BufferedSink sink, Object value) throws IOException{
        if(value == null){
            sink.writeByte(0);
            return 1;
        }else {
            sink.writeByte(1);
            return writeValue(sink, value) + 1;
        }
    }

    @Override
    public final Object read(BufferedSource sink) throws IOException{
        boolean isNull = sink.readByte() == 0;
        if(isNull){
            return null;
        }else {
            return readValue(sink);
        }
    }

    @Override
    public final int evaluateSize(Object value){
        return value != null ? evaluateSize0(value) + 1 : 1;
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
     * evaluate the size of target value in message-protocol
     * @param value the value . never null
     * @return the size as byte count
     */
    protected abstract int evaluateSize0(Object value);
}
