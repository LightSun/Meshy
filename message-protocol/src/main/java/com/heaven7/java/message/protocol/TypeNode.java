package com.heaven7.java.message.protocol;

/**
 * the type node often indicate the parameter types from {@linkplain com.heaven7.java.message.protocol.reflect.TypeToken}
 * @author heaven7
 */
public interface TypeNode {

    /**
     * get the type adapter from
     * @param mpContext the message protocol context
     * @param context the type adapter context
     * @param applyVersion the expect version
     * @return the type adapter
     */
    TypeAdapter getTypeAdapter(MessageProtocolContext mpContext, TypeAdapterContext context, float applyVersion);

    /**
     * the hash code of this node
     * @return the hash code
     */
    int hashCode();

    /**
     * equals or not.
     * @param o the other node
     * @return true if equals.
     */
    boolean equals(Object o);
}
