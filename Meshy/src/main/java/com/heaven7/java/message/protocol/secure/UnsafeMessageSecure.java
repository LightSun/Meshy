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
package com.heaven7.java.message.protocol.secure;


import com.heaven7.java.message.protocol.MessageSecure;

import java.security.GeneralSecurityException;
import java.security.KeyException;

/**
 * the unsafe message secure. just do nothing.
 * @author heaven7
 */
public final class UnsafeMessageSecure implements MessageSecure {

    public static final UnsafeMessageSecure INSTANCE = new UnsafeMessageSecure();

    private UnsafeMessageSecure(){}

    @Override
    public byte[] encode(byte[] data) throws GeneralSecurityException, KeyException {
        return data;
    }
    @Override
    public byte[] decode(byte[] data) throws GeneralSecurityException, KeyException {
        return data;
    }
}
