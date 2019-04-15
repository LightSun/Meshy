package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author heaven7
 */
public class DoubleAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException{
        sink.writeLong(Double.doubleToLongBits((Double) obj));
        return 8;
    }
    @Override
    public Object read(BufferedSource sink) throws IOException{
        return Double.longBitsToDouble(sink.readLong());
    }
    @Override
    public int evaluateSize(Object obj) {
        return 8;
    }
}
