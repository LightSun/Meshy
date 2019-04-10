package com.heaven7.java.message.protocal.secure;

import com.heaven7.java.message.protocal.MessageSecure;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyException;

/**
 * @author heaven7
 */
public class CipherMessageSecure implements MessageSecure {

    private final Cipher encodeCipher;
    private final Cipher decodeCipher;

    public CipherMessageSecure(Cipher encodeCipher, Cipher decodeCipher){
        this.encodeCipher = encodeCipher;
        this.decodeCipher = decodeCipher;
    }

    public CipherMessageSecure(Key encodeKey, Key decodeKey, String algorithm) throws Exception{
        this.encodeCipher = Cipher.getInstance(algorithm);
        this.decodeCipher = Cipher.getInstance(algorithm);

        encodeCipher.init(Cipher.ENCRYPT_MODE, encodeKey);
        decodeCipher.init(Cipher.DECRYPT_MODE, decodeKey);
    }

    @Override
    public byte[] encode(byte[] data) throws GeneralSecurityException, KeyException {
        return encodeCipher.doFinal(data);
    }
    @Override
    public byte[] decode(byte[] data) throws GeneralSecurityException, KeyException {
        return decodeCipher.doFinal(data);
    }
}
