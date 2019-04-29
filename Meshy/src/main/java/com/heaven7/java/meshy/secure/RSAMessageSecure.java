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
 * @author heaven7
 */
public final class RSAMessageSecure implements MessageSecure {

    public static final byte MODE_PUBLIC_EN_PRIVATE_DE = 1;
    public static final byte MODE_PUBLIC_DE_PRIVATE_EN = 2;

    private final Cipher encodeCipher;
    private final Cipher decodeCipher;

    public RSAMessageSecure(PublicKey pubKey, PrivateKey priKey){
        this(pubKey, priKey, MODE_PUBLIC_EN_PRIVATE_DE);
    }
    public RSAMessageSecure(PublicKey pubKey, PrivateKey priKey, byte mode){
        this(pubKey.getEncoded(), priKey.getEncoded(), mode);
    }
    public RSAMessageSecure(byte[] publicKey, byte[] privateKey, byte mode){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            if(mode == MODE_PUBLIC_EN_PRIVATE_DE){
                //public encode and private decode
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
                PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                encodeCipher = cipher;

                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
                PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
                Cipher cipher2 = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher2.init(Cipher.DECRYPT_MODE, priKey);
                decodeCipher = cipher2;
            }else if(mode == MODE_PUBLIC_DE_PRIVATE_EN){
                //private encode and public decode
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
                PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
                Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, priKey);
                encodeCipher = cipher;

                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
                PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
                Cipher cipher2 = Cipher.getInstance(keyFactory.getAlgorithm());
                cipher2.init(Cipher.DECRYPT_MODE, pubKey);
                decodeCipher = cipher2;
            }else {
                throw new IllegalArgumentException("wrong mode");
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
