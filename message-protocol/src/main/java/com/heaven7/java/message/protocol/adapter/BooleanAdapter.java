package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author heaven7
 */
public class BooleanAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException{
        sink.writeByte((Boolean)obj ? 1 : 0);
        return 1;
    }
    public Object read(BufferedSource sink) throws IOException{
        return sink.readByte() == 1;
    }

    @Override
    public int evaluateSize(Object obj){
        return 1;
    }
}
