package com.heaven7.java.message.protocal.util;

import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RSAKeyUtil {
 
    public static KeyPair generateKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(512);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }
     
    public static void saveFile(String path,String key)throws Exception{
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(key.getBytes());
        fos.flush();
        fos.close();
    }

    public static PublicKey getPubKey(String pubKey) {
        PublicKey publicKey = null;
        try {
            // 自己的公钥(测试)
            //String pubKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVRiDkEKXy/KBTe+UmkA+feq1zGWIgBxkgbz7aBJGb5+eMKKoiDRoEHzlGndwFKm4mQWNftuMOfNcogzYpGKSEfC7sqfBPDHsGPZixMWzL3J10zkMTWo6MDIXKKqMG1Pgeq1wENfJjcYSU/enYSZkg3rFTOaBSFId+rrPjPo7Y4wIDAQAB";
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
                    Base64.getDecoder().decode(pubKey));
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey( String priKey) {
        PrivateKey privateKey = null;
        //String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJVGIOQQpfL8oFN75SaQD596rXMZYiAHGSBvPtoEkZvn54woqiINGgQfOUad3AUqbiZBY1+24w581yiDNikYpIR8Luyp8E8MewY9mLExbMvcnXTOQxNajowMhcoqowbU+B6rXAQ18mNxhJT96dhJmSDesVM5oFIUh36us+M+jtjjAgMBAAECgYABtnxKIabF0wBD9Pf8KUsEmXPEDlaB55LyPFSMS+Ef2NlfUlgha+UQhwsxND6CEKqS5c0uG/se/2+4l0jXz+CTYBEh+USYB3gxcMKEo5XDFOGaM2Ncbc7FAKJIkYYN2DHmr4voSM5YkVibw5Lerw0kKdYyr0Xd0kmqTok3JLiLgQJBAOGZ1ao9oqWUzCKnpuTmXre8pZLmpWPhm6S1FU0vHjI0pZh/jusc8UXSRPnx1gLsgXq0ux30j968x/DmkESwxX8CQQCpY1+2p1aX2EzYO3UoTbBUTg7lCsopVNVf41xriek7XF1YyXOwEOSokp2SDQcRoKJ2PyPc2FJ/f54pigdsW0adAkAM8JTnydc9ZhZ7WmBhOrFuGnzoux/7ZaJWxSguoCg8OvbQk2hwJd3U4mWgbHWY/1XB4wHkivWBkhRpxd+6gOUjAkBH9qscS52zZzbGiwQsOk1Wk88qKdpXku4QDeUe3vmSuZwC85tNyu+KWrfM6/H74DYFbK/MzK7H8iz80uJye5jVAkAEqEB/LwlpXljFAxTID/SLZBb+bCIoV/kvg+2145F+CSSUjEWRhG/+OH0cQfqomfg36WrvHl0g/Xw06fg31HgK";
        PKCS8EncodedKeySpec priPKCS8;
        try {
            priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return privateKey;
    }
     
    public static void main(String[] args) throws Exception {
        KeyPair keyPair = generateKey();
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] privateBT = privateKey.getEncoded();
        // base64
        saveFile("c://privateKey.txt", Base64.getEncoder().encodeToString(privateBT));
        //============================================//
        PublicKey publicKey = keyPair.getPublic();
        byte[] publicBT = publicKey.getEncoded();
        // base64
        saveFile("c://publicKey.txt", Base64.getEncoder().encodeToString(publicBT));
    }
 
}