package com.heaven7.java.message.protocol.adapter;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class FloatPackedAdapter extends BasePackedAdapter {

    @Override
    protected int writeValue(BufferedSink sink, Object value) throws IOException {
        sink.writeInt(Float.floatToIntBits((Float) value));
        return 4;
    }
    @Override
    protected Object readValue(BufferedSource source) throws IOException {
        return Float.intBitsToFloat(source.readInt());
    }

    @Override
    protected int evaluateSize(Object value){
        return 4;
    }
}
