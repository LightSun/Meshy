package com.heaven7.java.message.protocol;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.message.protocol.adapter.NullTypeAdapter;
import com.heaven7.java.message.protocol.internal.$MPTypes;
import com.heaven7.java.message.protocol.internal.SimpleMessageProtocolContext;
import com.heaven7.java.message.protocol.reflect.TypeToken;

/**
 * this is a helper class to create {@linkplain TypeAdapter}.
 * @author heaven7
 */
public class TypeAdapters {

    public static TypeAdapter ofTypeToken(TypeToken<?> tt){
        return ofTypeToken(tt, MessageConfigManager.getVersion());
    }

    public static TypeAdapter ofTypeToken(TypeToken<?> tt, float applyVersion){
        $MPTypes.GenericNode node = new $MPTypes.GenericNode();
        $MPTypes.parseNode(null, tt.getType(), node);
        return node.getTypeAdapter(SimpleMessageProtocolContext.getDefault(),
                MessageConfigManager.getTypeAdapterContext(), applyVersion);
    }

    public static TypeAdapter getTypeAdapter(@Nullable Object obj, float applyVersion){
        if(obj == null){
            return NullTypeAdapter.INSTANCE;
        }
        Class<?> clazz = obj.getClass();
        $MPTypes.GenericNode node = new $MPTypes.GenericNode();
        $MPTypes.parseNode(clazz, clazz, node);
        return node.getTypeAdapter(SimpleMessageProtocolContext.getDefault(),
                MessageConfigManager.getTypeAdapterContext(), applyVersion);
    }
}
