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

import com.heaven7.java.base.anno.NonNull;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.meshy.internal.SimpleMessageProtocolContext;
import com.heaven7.java.meshy.util.Pair;

import java.util.*;

/**
 * @since @since 1.2.0
 * @author heaven7
 */
public final class Meshy {

    private static final Comparator<Pair<Class<?>, Float>> sCompatComparator =
            new Comparator<Pair<Class<?>, Float>>() {
                @Override
                public int compare(Pair<Class<?>, Float> o1, Pair<Class<?>, Float> o2) {
                    return Float.compare(o1.value, o2.value);
                }
            };
    private MessageProtocolContext mProtocolContext;

    private final Map<TypeNode, Pair<Float,TypeAdapter>> mAdapterMap;
    private TypeAdapterContext mAdapterContext;
    private float mVersion;
    private String mSignKey;
    private MessageSignature mSignature;
    private SegmentationPolicy mSegmentationPolicy;
    private final SparseArrayDelegate<MessageSecure> mSecureMap;
    // share-classname, real-class, since-mVersion
    private final Map<String, List<Pair<Class<?>, Float>>> mCompatMap;

    //=============================
    private final SparseArrayDelegate<MessageSecureWrapper> mSecureWrappers;
    private final WeakHashMap<Class<?>, String> mRepresentMap;

    private final MeshyReader mReader;
    private final MeshyWriter mWriter;

    /*public*/ Meshy(MeshyBuilder builder) {
         this.mAdapterMap = builder.adapterMap;
         this.mAdapterContext = new WrappedTypeAdapterContext(builder.adapterContext);
         this.mProtocolContext = builder.messageProtocolContext;
         this.mVersion = builder.version;
         this.mSignKey = builder.signKey;
         this.mSignature = builder.signature;
         this.mSegmentationPolicy = builder.segmentationPolicy;
         this.mSecureMap = builder.secureMap;
         this.mCompatMap = builder.compatMap;

        mSecureWrappers = SparseFactory.newSparseArray(5);
        mRepresentMap = new WeakHashMap<>();
        init();

        mReader = new MeshyReader(this);
        mWriter = new MeshyWriter(this);
    }

    private void init() {
        for (Map.Entry<String, List<Pair<Class<?>, Float>>> en : mCompatMap.entrySet()){
            Collections.sort(en.getValue(), sCompatComparator); //AESC
            final String className = en.getKey();
            for (Pair<Class<?>, Float> pair : en.getValue()){
                mRepresentMap.put(pair.key, className);
            }
        }
    }

    public @NonNull MessageProtocolContext getMessageProtocolContext() {
        return mProtocolContext;
    }
    public TypeAdapterContext getTypeAdapterContext(){
        return mAdapterContext;
    }
    public float getVersion() {
        return mVersion;
    }
    public String getMessageSignatureKey() {
        return mSignKey;
    }
    public MessageSignature getMessageSignature() {
        return mSignature;
    }
    public SegmentationPolicy getSegmentationPolicy() {
        return mSegmentationPolicy;
    }
    public int randomEncodeType(){
        try{
            int index = new Random().nextInt(mSecureMap.size());
            return mSecureMap.keyAt(index);
        }catch (IllegalArgumentException e){
            throw new MeshyException("you must config the message secure.");
        }
    }
    /**
     * sign the message
     * @param data the data to signature
     * @return the signed value
     */
    public String signatureMessage(byte[] data){
        return mSignature.signature(data, mSignKey);
    }

    /**
     * get the compat class. this is useful for compat message entity classes.
     * @param className the expect classname
     * @param version the version code
     * @return the actually class as expect
     * @throws ClassNotFoundException if {@linkplain Class#forName(String)} occurs
     */
    public Class<?> getCompatClass(String className, float version) throws ClassNotFoundException {
        List<Pair<Class<?>, Float>> pairs = mCompatMap.get(className);
        if(Predicates.isEmpty(pairs)){
            return Class.forName(className);
        }
        for (Pair<Class<?>, Float> pair : pairs){
            if(pair.value >= version){
                return pair.key;
            }
        }
        throw new MeshyException("wrong version code("+ version +") or class compat had not configuration.");
    }


    /**
     * get message secure for target type
     * @param type the secure type for encode and decode
     * @return the message secure
     */
    public MessageSecureWrapper getMessageSecure(int type){
        MessageSecureWrapper wrapper = mSecureWrappers.get(type);
        if(wrapper != null){
            return wrapper;
        }
        MessageSecure secure = mSecureMap.get(type);
        if(secure != null){
            wrapper = new MessageSecureWrapper(secure);
            mSecureWrappers.put(type, wrapper);
            return wrapper;
        }
        throw new MeshyException("you must config the message secure for type = " + type);
    }

    /**
     * get the represent compat class name for target class .
     * @param rawClass the raw class
     * @return the represent class name.
     */
    public String getRepresentClassName(Class<?> rawClass) {
        String classname = mRepresentMap.get(rawClass);
        if(classname == null){
            return rawClass.getName();
        }
        return classname;
    }
    /**
     * get the type adapter
     * @param type the type node
     * @param expectVersion the expect mVersion to used
     * @return the type adapter if exist. or else null.
     */
    public TypeAdapter getTypeAdapter(TypeNode type, float expectVersion){
        Pair<Float, TypeAdapter> pair = mAdapterMap.get(type);
        if(pair != null && expectVersion >= pair.key){
            return pair.value;
        }
        return null;
    }

    /**
     * get the meshy reader
     * @return the reader to read message protocol
     */
    public MeshyReader getReader() {
        return mReader;
    }

    /**
     * get the meshy writer
     * @return the writer to write message protocol
     */
    public MeshyWriter getWriter() {
        return mWriter;
    }

    private static class WrappedTypeAdapterContext implements TypeAdapterContext {

        private final TypeAdapterContext base;

        WrappedTypeAdapterContext(TypeAdapterContext base) {
            this.base = base;
        }

        @Override
        public Object newInstance(Class<?> clazz) {
            return base.newInstance(clazz);
        }

        @Override
        public Map createMap(String name) {
            Map map = base.createMap(name);
            if(map != null){
                return map;
            }
            return new HashMap();
        }
        @Override
        public Map getMap(Object obj) {
            return base.getMap(obj);
        }
        @Override
        public Collection createCollection(String name) {
            Collection collection = base.createCollection(name);
            if(collection != null){
                return collection;
            }
            return new ArrayList();
        }

        @Override
        public boolean isMap(Class<?> rawType) {
            return base.isMap(rawType);
        }
    }

}
