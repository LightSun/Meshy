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
package com.heaven7.java.message.protocol;

import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.message.protocol.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the message config used for communicate protocol.
 * @author heaven7
 */
public class MessageConfig {

    /**
     * the version of current used
     */
    public float version;
    /**
     * the sign key
     */
    public String signKey;
    /**
     * the message signature
     */
    public MessageSignature signature;

    /**
     * the segmentation policy for {@linkplain MessageSecure}
     */
    public SegmentationPolicy segmentationPolicy;
    /**
     * the message secures. key is type, value is MessageSecure
     */
    public SparseArrayDelegate<MessageSecure> secures;
            // share-classname, real-class, since-version
    /**
     * the message entity compat map. key is classname which used for 'CS' communication.
     * Pair<Class<?>, Float>: means key is the real class used , float means the version this class since used.
     */
    public Map<String, List<Pair<Class<?>, Float>>> compatMap;

    public TypeAdapterContext context;


    public static MessageConfig newConfig(){
        MessageConfig config = new MessageConfig();
        config.compatMap = new HashMap<>();
        config.secures = SparseFactory.newSparseArray(5);
        config.context = new BaseTypeAdapterContext();
        return config;
    }

}
