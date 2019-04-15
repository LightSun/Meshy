package com.heaven7.java.message.protocol.adapter;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class BooleanPackedAdapter extends BasePackedAdapter {

    @Override
    protected int writeValue(BufferedSink sink, Object value) throws IOException {
        sink.writeByte((Boolean)value ? 1 : 0);
        return 1;
    }

    @Override
    protected Object readValue(BufferedSource source) throws IOException {
        return source.readByte() == 1;
    }

    @Override
    protected int evaluateSize0(Object value) {
        return 1;
    }
}
