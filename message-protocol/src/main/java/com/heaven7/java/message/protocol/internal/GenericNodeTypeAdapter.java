package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.message.protocol.MessageProtocolContext;
import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.TypeAdapterContext;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public class GenericNodeTypeAdapter extends TypeAdapter {

    private final TypeAdapter mAdapter;

    public GenericNodeTypeAdapter(MessageProtocolContext mpContext, TypeAdapterContext adapterContext, $MPTypes.GenericNode node, float applyVersion) {
        mAdapter = node.getTypeAdapter(mpContext, adapterContext, applyVersion);
    }

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException {
        return mAdapter.write(sink, obj);
    }

    @Override
    public Object read(BufferedSource source) throws IOException {
        return mAdapter.read(source);
    }

    @Override
    public int evaluateSize(Object obj) {
        return mAdapter.evaluateSize(obj);
    }
}
