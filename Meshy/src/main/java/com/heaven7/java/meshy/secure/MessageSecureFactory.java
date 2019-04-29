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
import com.heaven7.java.meshy.util.RSAUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author heaven7
 */
public final class MessageSecureFactory {

    public static MessageSecure createMessageSecure(String className, String... params)
            throws ClassNotFoundException, IllegalAccessException, InvocationTargetException,
            InstantiationException, NoSuchMethodException{
        //
        final Class<?> clazz = Class.forName(className);
        if(clazz == SingleRSAMessageSecure.class){
            if(params.length != 2){
                throw new IllegalStateException("param count error.");
            }
            Constructor<?> cons = clazz.getConstructor(byte[].class, boolean.class);
            Boolean bool = Boolean.valueOf(params[1]);
            return (MessageSecure) cons.newInstance((bool ? RSAUtils.getPrivateKey(params[0]).getEncoded() :
                    RSAUtils.getPublicKey(params[0]).getEncoded()), bool);
        }else if(clazz == RSAMessageSecure.class){
            if(params.length == 2){
                Constructor<?> cons = clazz.getConstructor(PublicKey.class, PrivateKey.class);
                return (MessageSecure) cons.newInstance(RSAUtils.getPublicKey(params[0]), RSAUtils.getPrivateKey(params[1]));
            }else if(params.length == 3){
                Constructor<?> cons = clazz.getConstructor(PublicKey.class, PrivateKey.class, byte.class);
                return (MessageSecure) cons.newInstance(RSAUtils.getPublicKey(params[0]),
                        RSAUtils.getPrivateKey(params[1]), Byte.valueOf(params[2]));
            }else {
                throw new IllegalStateException("param count error.");
            }
        }else if(clazz == UnsafeMessageSecure.class){
            return UnsafeMessageSecure.INSTANCE;
        }
        else {
            throw new IllegalStateException("unsupport Message Secure class = " + className);
        }
    }
}
