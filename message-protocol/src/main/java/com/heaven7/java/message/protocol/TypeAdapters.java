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
public final class TypeAdapters {

    public static TypeAdapter ofTypeToken(TypeToken<?> tt){
        return ofTypeToken(tt, MessageConfigManager.getVersion());
    }

    public static TypeAdapter ofTypeToken(TypeToken<?> tt, float applyVersion){
        return $MPTypes.getTypeNode(tt.getType()).getTypeAdapter( SimpleMessageProtocolContext.getDefault(),
                MessageConfigManager.getTypeAdapterContext(), applyVersion);
    }

    /**
     * get the type adapter from target object. the object should be any object of self define.
     * mus not be any collection or map.
     * @param obj the object
     * @param applyVersion the version to apply
     * @return the type adapter
     */
    public static TypeAdapter getTypeAdapter(@Nullable Object obj, float applyVersion){
        if(obj == null){
            return NullTypeAdapter.INSTANCE;
        }
        Class<?> clazz = obj.getClass();
        return $MPTypes.getTypeNode(clazz, clazz).getTypeAdapter(
                SimpleMessageProtocolContext.getDefault(),
                MessageConfigManager.getTypeAdapterContext(),
                applyVersion);

    }
}
