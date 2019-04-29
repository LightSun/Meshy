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

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.message.protocol.adapter.NullTypeAdapter;
import com.heaven7.java.message.protocol.internal.$MPTypes;
import com.heaven7.java.message.protocol.reflect.TypeToken;

/**
 * this is a helper class to create {@linkplain TypeAdapter}.
 * @author heaven7
 */
public final class TypeAdapters {

    public static TypeAdapter ofTypeToken(TypeToken<?> tt, Meshy meshy){
        return ofTypeToken(tt, meshy, meshy.getVersion());
    }

    public static TypeAdapter ofTypeToken(TypeToken<?> tt, Meshy meshy, float applyVersion){
        return $MPTypes.getTypeNode(tt.getType()).getTypeAdapter(meshy, applyVersion);
    }
    /**
     * get the type adapter from target object. the object should be any object of self define.
     * mus not be any collection or map.
     * @param obj the object
     * @param meshy the meshy
     * @return the type adapter
     */
    public static TypeAdapter getTypeAdapter(@Nullable Object obj, Meshy meshy){
        return getTypeAdapter(obj, meshy, meshy.getVersion());
    }
    /**
     * get the type adapter from target object. the object should be any object of self define.
     * mus not be any collection or map.
     * @param obj the object
     * @param meshy the meshy
     * @param applyVersion the version to apply
     * @return the type adapter
     */
    public static TypeAdapter getTypeAdapter(@Nullable Object obj, Meshy meshy, float applyVersion){
        if(obj == null){
            return NullTypeAdapter.INSTANCE;
        }
        Class<?> clazz = obj.getClass();
        return $MPTypes.getTypeNode(clazz, clazz).getTypeAdapter(meshy, applyVersion);

    }
}
