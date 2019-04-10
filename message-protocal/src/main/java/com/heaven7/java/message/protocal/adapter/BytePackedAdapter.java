package com.heaven7.java.message.protocal.adapter;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public final class BytePackedAdapter extends BasePackedAdapter {

    @Override
    protected int writeValue(BufferedSink sink, Object value)  throws IOException {
        sink.writeByte((Byte)value);
        return 1;
    }

    @Override
    protected Object readValue(BufferedSource source)  throws IOException {
        return source.readByte();
    }

    @Override
    protected int evaluateSize(Object value){
        return 1;
    }
}
