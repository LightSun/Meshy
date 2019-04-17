package com.heaven7.java.message.protocol;

import java.util.List;

/**
 * @author heaven7
 */
public class SimpleMessageProtocalContext implements MessageProtocolContext {

    @Override
    public List<MemberProxy> getMemberProxies(Class<?> clazz) {
        return MessageIO.getMemberProxies(clazz);
    }

    @Override
    public TypeAdapter getTypeAdapter(MemberProxy proxy) {
        return MessageIO.getTypeAdapter(proxy);
    }

}
