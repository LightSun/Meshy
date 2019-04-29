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
package com.heaven7.java.meshy.signature;

import com.heaven7.java.meshy.MessageSignature;

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
