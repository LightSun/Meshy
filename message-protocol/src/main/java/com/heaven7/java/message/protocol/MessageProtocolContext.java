package com.heaven7.java.message.protocol;

import java.util.List;

/**
 * @author heaven7
 */
public interface MessageProtocolContext {

    List<MemberProxy> getMemberProxies(Class<?> clazz);
    /**
     * get the type adapter for base types. like primitive types with its' wrapper and
     * string type.
     * @param Clazz the base types
     * @return the type adapter of 'Basic' type.
     */
    TypeAdapter getBaseTypeAdapter(Class<?> Clazz);
}
