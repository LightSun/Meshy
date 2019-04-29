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
