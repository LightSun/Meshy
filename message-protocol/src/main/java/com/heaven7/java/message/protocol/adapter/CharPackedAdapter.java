package com.heaven7.java.message.protocol.adapter;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class CharPackedAdapter extends BasePackedAdapter {

    @Override
    protected int writeValue(BufferedSink sink, Object value) throws IOException {
        sink.writeInt((char)value);
        return 4;
    }

    @Override
    protected Object readValue(BufferedSource source) throws IOException {
        return (char) source.readInt() ;
    }

    @Override
    protected int evaluateSize0(Object value){
        return 4;
    }
}
