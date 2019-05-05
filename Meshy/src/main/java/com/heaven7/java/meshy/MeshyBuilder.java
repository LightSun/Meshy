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
package com.heaven7.java.meshy;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.meshy.internal.$MPTypes;
import com.heaven7.java.meshy.internal.SimpleMessageProtocolContext;
import com.heaven7.java.meshy.policy.DefaultRSASegmentationPolicy;
import com.heaven7.java.meshy.reflect.TypeToken;
import com.heaven7.java.meshy.signature.HMAC_SHA1Signature;
import com.heaven7.java.meshy.util.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the meshy builder used to create {@linkplain Meshy}.
 * @since 1.2.0
 * @author heaven7
 */
public final class MeshyBuilder {

    final Map<TypeNode, Pair<Float,TypeAdapter>> adapterMap = new HashMap<>();
    TypeAdapterContext adapterContext;

    /**
     * the version of current used
     */
    float version;
    /**
     * the sign key
     */
    String signKey;
    /**
     * the message signature
     */
    MessageSignature signature;

    /**
     * the segmentation policy for {@linkplain MessageSecure}
     */
    SegmentationPolicy segmentationPolicy;

    MessageProtocolContext messageProtocolContext;
    /**
     * the message secures. key is type, value is MessageSecure
     */
    final SparseArrayDelegate<MessageSecure> secureMap = SparseFactory.newSparseArray(5);
    // share-classname, real-class, since-version
    /**
     * the message entity compat map. key is classname which used for 'CS' communication.
     * Pair<Class<?>, Float>: means key is the real class used , float means the version this class since used.
     */
    final Map<String, List<Pair<Class<?>, Float>>> compatMap = new HashMap<>();


    public MeshyBuilder() {
        messageProtocolContext = SimpleMessageProtocolContext.getDefault();
        adapterContext = new BaseTypeAdapterContext();
        signature = new HMAC_SHA1Signature();
        segmentationPolicy = new DefaultRSASegmentationPolicy(53);
    }

    /**
     * register the type adapter for target type and version
     * @param type the type to apply can be used from {@linkplain TypeToken#getType()}.
     * @param version the version to apply
     * @param adapter the adapter.
     * @return this
     */
    public MeshyBuilder registerTypeAdapter(Type type, float version, TypeAdapter adapter){
        TypeNode node = $MPTypes.getTypeNode(type);
        adapterMap.put(node, new Pair<>(version, adapter));
        return this;
    }

    /**
     * register the message secure with target type.
     * @param type the type
     * @param secure the message secure
     * @return this
     */
    public MeshyBuilder registerMessageSecure(int type , MessageSecure secure){
        secureMap.put(type, secure);
        return this;
    }

    /**
     * register the compat class pairs for target class name. the pair contains class and start version.
     * @param classname the target class name
     * @param pairs the pairs.
     * @return this
     */
    public MeshyBuilder registerCompatClasses(String classname, List<Pair<Class<?>, Float>> pairs){
        compatMap.put(classname, pairs);
        return this;
    }
    /**
     * set the type adapter context
     * @param context the context
     * @return this.
     * @since 1.0.1
     */
    public MeshyBuilder setMessageProtocolContext(MessageProtocolContext context){
        Throwables.checkNull(context);
        this.messageProtocolContext = context;
        return this;
    }
    /**
     * set the type adapter context
     * @param context the context
     * @return this.
     */
    public MeshyBuilder setTypeAdapterContext(TypeAdapterContext context){
        this.adapterContext = context;
        return this;
    }

    /**
     * set the local version of meshy
     * @param version the version
     * @return this
     */
    public MeshyBuilder setVersion(float version){
        this.version = version;
        return this;
    }

    /**
     * set the signature key
     * @param signKey the sign key
     * @return this
     */
    public MeshyBuilder setSignatureKey(String signKey) {
        this.signKey = signKey;
        return this;
    }

    /**
     * set the Message Signature
     * @param signature the message Signature
     * @return this
     */
    public MeshyBuilder setSignature(MessageSignature signature) {
        this.signature = signature;
        return this;
    }

    /**
     * set the segmentation policy
     * @param segmentationPolicy the segmentation policy
     * @return this
     */
    public MeshyBuilder setSegmentationPolicy(SegmentationPolicy segmentationPolicy) {
        this.segmentationPolicy = segmentationPolicy;
        return this;
    }
    public Meshy build(){
        //check
        if(version == 0f){
            throw new IllegalStateException("you must config the version of local.");
        }
        if(Predicates.isEmpty(signKey)){
            throw new IllegalStateException("you must config the signatureKey which will be used by Message signature.");
        }
        if(secureMap.size() == 0){
            throw new IllegalStateException("you must config the MessageSecure which will be used by Message encode and decode.");
        }
        return new Meshy(this);
    }
}
