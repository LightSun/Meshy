package com.heaven7.java.message.protocol.signature;

import com.heaven7.java.message.protocol.MessageSignature;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * base on 'HMAC-sha1' algorithm to sign and generate 28 bits data.
 * @author heaven7
 */
public class HMAC_SHA1Signature implements MessageSignature {

    private static final String ALGORITHM = "HmacSHA1";

    @Override
    public String signature(byte[] data, String key) {
        try {
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(signinKey);
            return Base64.getEncoder().encodeToString(mac.doFinal(data));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
