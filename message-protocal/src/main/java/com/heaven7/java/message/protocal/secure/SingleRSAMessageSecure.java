package com.heaven7.java.message.protocal.secure;

import com.heaven7.java.message.protocal.MessageSecure;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author heaven7
 */
public class SingleRSAMessageSecure implements MessageSecure {

    private final Cipher encodeCipher;
    private final Cipher decodeCipher;

    /**
     * create 'RSA' message secure by single key. that means encode and decode use the sample key
     * @param key the key
     * @param asPrivate true if the key is used as private rsa-key. false otherwise.
     */
    public SingleRSAMessageSecure(byte[] key, boolean asPrivate){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            if(asPrivate){
                PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(key));
                Cipher enCipher = Cipher.getInstance(keyFactory.getAlgorithm());
                enCipher.init(Cipher.ENCRYPT_MODE, privateKey);
                encodeCipher = enCipher;

                Cipher deCipher = Cipher.getInstance(keyFactory.getAlgorithm());
                deCipher.init(Cipher.DECRYPT_MODE, privateKey);
                decodeCipher = deCipher;
            }else {
                PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(key));
                Cipher enCipher = Cipher.getInstance(keyFactory.getAlgorithm());
                enCipher.init(Cipher.ENCRYPT_MODE, pubKey);
                encodeCipher = enCipher;

                Cipher deCipher = Cipher.getInstance(keyFactory.getAlgorithm());
                deCipher.init(Cipher.DECRYPT_MODE, pubKey);
                decodeCipher = deCipher;
            }
        }catch (Exception e){
            if( e instanceof RuntimeException){
                throw (RuntimeException)e;
            } else {
                throw new RuntimeException(e);
            }
        }
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
