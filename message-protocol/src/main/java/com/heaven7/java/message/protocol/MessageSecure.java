package com.heaven7.java.message.protocol;

import java.security.GeneralSecurityException;
import java.security.KeyException;

/**
 * the message secure used for encode and decode
 */
public interface MessageSecure {

    byte[] encode(byte[] data) throws GeneralSecurityException, KeyException;

    byte[] decode(byte[] data) throws GeneralSecurityException, KeyException;

}
