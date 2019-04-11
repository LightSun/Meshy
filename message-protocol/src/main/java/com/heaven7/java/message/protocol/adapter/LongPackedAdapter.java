package com.heaven7.java.message.protocol.adapter;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class LongPackedAdapter extends BasePackedAdapter {
    @Override
    protected int writeValue(BufferedSink sink, Object value) throws IOException {
        sink.writeLong((Long) value);
        return 8;
    }

    @Override
    protected Object readValue(BufferedSource source) throws IOException {
        return source.readLong();
    }

    @Override
    protected int evaluateSize(Object value){
        return 8;
    }
}
