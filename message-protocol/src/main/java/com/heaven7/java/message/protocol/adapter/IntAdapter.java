package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author heaven7
 */
public class IntAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException{
        sink.writeInt((Integer)obj);
        return 4;
    }
    @Override
    public Object read(BufferedSource sink) throws IOException {
        return sink.readInt();
    }
    @Override
    public int evaluateSize(Object obj){
        return 4;
    }
}
