package com.heaven7.java.message.protocal.adapters;

import com.heaven7.java.message.protocal.MemberProxy;
import com.heaven7.java.message.protocal.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author heaven7
 */
public class LongAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        sink.writeLong(proxy.getLong(obj));
        return 8;
    }

    @Override
    public void read(BufferedSource sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        proxy.setLong(obj, sink.readLong());
    }

    @Override
    public int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException {
        return 8;
    }
}
