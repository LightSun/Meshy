package com.heaven7.java.message.protocal;

/**
 * @author heaven7
 */
public interface MessageSignature {

    String signature(byte[] data, String key);
}
