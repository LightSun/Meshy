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
package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.*;
import com.heaven7.java.meshy.internal.MUtils;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * the object type adapter .support null.
 * @author heaven7
 */
public final class ObjectTypeAdapter extends TypeAdapter {

    private final Meshy meshy;
    private final float version;

    public ObjectTypeAdapter(Meshy meshy, float applyVersion) {
        this.meshy = meshy;
        this.version = applyVersion;
    }

    private List<MemberProxy> getMemberProxies(Class<?> clazz){
        return meshy.getMessageProtocolContext().getMemberProxies(clazz);
    }

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException {
        int len = 0;
        try {
            if (obj == null) {
                sink.writeByte(0);
                return 1;
            } else {
                sink.writeByte(1);
                len += 1;
            }
            TypeAdapterContext adapterContext = meshy.getTypeAdapterContext();

            final Class<?> rawClass = obj.getClass();
            Class<?> targetClass;   //target class to find member proxy.
            //target version > < = local version
            String name = meshy.getRepresentClassName(rawClass);

            float localVersion = meshy.getVersion();
            if(localVersion != version){
                targetClass = meshy.getCompatClass(name, Math.min(localVersion, version));
                //target class is a new class.
                if(!targetClass.isAssignableFrom(rawClass)){
                    try {
                        Object obj2 = adapterContext.newInstance(targetClass);
                        MUtils.copyProperties(obj, obj2, getMemberProxies(rawClass),
                                getMemberProxies(targetClass));
                        obj = obj2;
                    }catch (Exception e){
                        throw new MeshyException("create compat object failed, class is "
                                + targetClass.getName(), e);
                    }
                }
            } else {
                targetClass = meshy.getCompatClass(name, version);
            }
            //write class name
            sink.writeInt(name.length());
            sink.writeUtf8(name);
            len += 4 + name.length();
            // write data.
            List<MemberProxy> proxies = getMemberProxies(targetClass);
            for (MemberProxy proxy : proxies) {
               // System.out.println("write >>> object:  propName = " + proxy.getPropertyName());
                TypeAdapter adapter = proxy.getTypeAdapter(meshy, version);
                len += adapter.write(sink, proxy.getObject(obj));
            }
        } catch ( ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            throw new MeshyException(e);
        }
        return len;
    }


    @Override
    public Object read(BufferedSource source) throws IOException {
        try {
            if (source.readByte() != 1) {
                return null;
            }

            int classLen = source.readInt();
            final String fullName = source.readUtf8(classLen);
            // remote version > < = local version
            Class<?> clazz;
            clazz = meshy.getCompatClass(fullName, Math.min(version, meshy.getVersion()));
            Object obj = meshy.getTypeAdapterContext().newInstance(clazz);

            List<MemberProxy> proxies = getMemberProxies(clazz);
            for (MemberProxy proxy : proxies) {
                TypeAdapter adapter = proxy.getTypeAdapter(meshy, version);
                Object value = adapter.read(source);
                proxy.setObject(obj, value);
            }
            return obj;
        } catch (ClassNotFoundException
                | IllegalAccessException
                | NullPointerException
                | InvocationTargetException e) {
            throw new MeshyException(e);
        }
    }

    @Override
    public int evaluateSize(Object obj) {
        int len = 0;
        try {
            if (obj == null) {
                return 1;
            } else {
                len += 1;
            }
            TypeAdapterContext adapterContext = meshy.getTypeAdapterContext();

            final Class<?> rawClass = obj.getClass();
            Class<?> targetClass;   //target class to find member proxy.
            //target version > < = local version
            String name = meshy.getRepresentClassName(rawClass);

            float localVersion = meshy.getVersion();
            if(localVersion != version){
                targetClass = meshy.getCompatClass(name, Math.min(localVersion, version));
                //target class is a new class.
                if(!targetClass.isAssignableFrom(rawClass)){
                    try {
                        Object obj2 = adapterContext.newInstance(targetClass);
                        MUtils.copyProperties(obj, obj2, getMemberProxies(rawClass),
                                getMemberProxies(targetClass));
                        obj = obj2;
                    }catch (Exception e){
                        throw new MeshyException("create compat object failed, class is "
                                + targetClass.getName(), e);
                    }
                }
            } else {
                targetClass = meshy.getCompatClass(name, version);
            }
            //write class name
            len += 4 + name.length();
            // write data.
            List<MemberProxy> proxies = getMemberProxies(targetClass);
            for (MemberProxy proxy : proxies) {
                TypeAdapter adapter = proxy.getTypeAdapter(meshy, version);
                len += adapter.evaluateSize(proxy.getObject(obj));
            }
        } catch ( ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            throw new MeshyException(e);
        }
        return len;
    }

}
