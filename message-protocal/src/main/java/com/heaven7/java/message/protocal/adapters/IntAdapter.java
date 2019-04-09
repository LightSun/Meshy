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
public class IntAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        sink.writeInt(proxy.getInt(obj));
        return 4;
    }
    @Override
    public void read(BufferedSource sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        proxy.setInt(obj, sink.readInt());
    }
    @Override
    public int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException {
        return 4;
    }
}