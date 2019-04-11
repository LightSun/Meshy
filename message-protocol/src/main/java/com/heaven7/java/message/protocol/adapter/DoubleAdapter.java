package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.MemberProxy;
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
    public int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        sink.writeLong(Double.doubleToLongBits(proxy.getDouble(obj)));
        return 8;
    }
    @Override
    public void read(BufferedSource sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        proxy.setDouble(obj, Double.longBitsToDouble(sink.readLong()));
    }
    @Override
    public int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException {
        return 8;
    }
}
