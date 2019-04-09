package com.heaven7.java.message.protocal.adapters;

import com.heaven7.java.message.protocal.MemberProxy;
import com.heaven7.java.message.protocal.MessageException;
import com.heaven7.java.message.protocal.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author heaven7
 */
public final class ByteAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        sink.writeByte(proxy.getByte(obj));
        return 1;
    }

    @Override
    public void read(BufferedSource sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException{
        proxy.setByte(obj, sink.readByte());
    }
    @Override
    public int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException {
        return 1;
    }
}
