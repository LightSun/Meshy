package com.heaven7.java.message.protocol;

import java.util.List;

/**
 * @author heaven7
 */
public interface MessageProtocolContext {

    List<MemberProxy> getMemberProxies(Class<?> clazz);
    TypeAdapter getTypeAdapter(MemberProxy proxy);
}
