package com.heaven7.java.message.protocol.secure;


import com.heaven7.java.message.protocol.MessageSecure;

import java.security.GeneralSecurityException;
import java.security.KeyException;

/**
 * the unsafe message secure. just do nothing.
 * @author heaven7
 */
public final class UnsafeMessageSecure implements MessageSecure {

    public static final UnsafeMessageSecure INSTANCE = new UnsafeMessageSecure();

    private UnsafeMessageSecure(){}

    @Override
    public byte[] encode(byte[] data) throws GeneralSecurityException, KeyException {
        return data;
    }
    @Override
    public byte[] decode(byte[] data) throws GeneralSecurityException, KeyException {
        return data;
    }
}
