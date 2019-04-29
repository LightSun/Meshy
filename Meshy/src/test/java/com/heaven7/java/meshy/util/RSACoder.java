package com.heaven7.java.meshy.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class RSACoder {

    public static final String KEY_ALGORITHM = "RSA";
    //must be 512 * n
    private static final int KEY_SIZE = 512;

    public static KeyPair initKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }
}