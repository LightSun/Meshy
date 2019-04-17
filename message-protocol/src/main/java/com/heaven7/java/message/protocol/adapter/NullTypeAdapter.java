package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class NullTypeAdapter extends TypeAdapter{

    public static final NullTypeAdapter INSTANCE = new NullTypeAdapter();

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException {
        sink.writeByte(-1);
        return 1;
    }
    @Override
    public Object read(BufferedSource source) throws IOException {
        source.skip(1);
        return null;
    }
    @Override
    public int evaluateSize(Object obj) {
        return 1;
    }
}
