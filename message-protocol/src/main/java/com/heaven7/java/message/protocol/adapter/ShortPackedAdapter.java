package com.heaven7.java.message.protocol.adapter;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class ShortPackedAdapter extends BasePackedAdapter {

    @Override
    protected int writeValue(BufferedSink sink, Object value) throws IOException {
        sink.writeShort((Short) value);
        return 2;
    }

    @Override
    protected Object readValue(BufferedSource source) throws IOException {
        return source.readShort();
    }

    @Override
    protected int evaluateSize0(Object value) {
        return 2;
    }
}
