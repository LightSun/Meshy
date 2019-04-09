package com.heaven7.java.message.protocal.secure;

import com.heaven7.java.message.protocal.MessageSecure;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAMessageSecure implements MessageSecure {

    private final Cipher privateCipher;
    private final Cipher publicCipher;

    public RSAMessageSecure(PrivateKey rsaPrivateKey, PublicKey rsaPublicKey) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            privateCipher = Cipher.getInstance("RSA");
            privateCipher.init(Cipher.DECRYPT_MODE, privateKey);

            publicCipher = Cipher.getInstance("RSA");
            publicCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        }catch (NoSuchPaddingException | NoSuchAlgorithmException | KeyException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] encode(byte[] data) throws GeneralSecurityException, KeyException {
        return publicCipher.doFinal(data);
    }

    @Override
    public byte[] decode(byte[] data) throws GeneralSecurityException, KeyException  {
        return privateCipher.doFinal(data);
    }

}
