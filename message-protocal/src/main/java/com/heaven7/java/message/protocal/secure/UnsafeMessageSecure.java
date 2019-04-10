package com.heaven7.java.message.protocal.secure;


import com.heaven7.java.message.protocal.MessageSecure;

import java.security.GeneralSecurityException;
import java.security.KeyException;

/**
 * the unsafe message secure. just do nothing.
 * @author heaven7
 */
public class UnsafeMessageSecure implements MessageSecure {
    @Override
    public byte[] encode(byte[] data) throws GeneralSecurityException, KeyException {
        return data;
    }
    @Override
    public byte[] decode(byte[] data) throws GeneralSecurityException, KeyException {
        return data;
    }
}
