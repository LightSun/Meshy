package com.heaven7.java.message.protocal.adapters;

import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class DoublePackedAdapter extends BasePackedAdapter {

    @Override
    protected int writeValue(BufferedSink sink, Object value) throws IOException {
        sink.writeLong(Double.doubleToLongBits((Double) value));
        return 8;
    }

    @Override
    protected Object readValue(BufferedSource source) throws IOException {
        return Double.longBitsToDouble(source.readLong());
    }

    @Override
    protected int evaluateSize(Object value) {
        return 8;
    }
}
