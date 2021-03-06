/*
 * Copyright 2019
 * heaven7(donshine723@gmail.com)

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.java.meshy.secure;

import com.heaven7.java.meshy.MessageSecure;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * the RSA message secure used as single key. for example: often client use public key. server use private key.
 * so this class used to do it.
 * @author heaven7
 */
public final class SingleRSAMessageSecure implements MessageSecure {

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
