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
package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * the array type adapter
 * @author heaven7
 */
public final class ArrayTypeAdapter extends TypeAdapter {

    private final Class<?> componentType;
    private final TypeAdapter componentAdapter;

    public ArrayTypeAdapter(Class<?> componentType, TypeAdapter componentAdapter) {
        this.componentType = componentType;
        this.componentAdapter = componentAdapter;
    }

    @Override
    public int write(BufferedSink sink, Object array) throws IOException{
        if(array == null){
            sink.writeInt(-1);
            return 4;
        }
        int total = 0;
        int size = Array.getLength(array);
        sink.writeInt(size);
        total += 4;
        if(size > 0){
            for(int i = 0 ; i < size ; i ++){
                total += componentAdapter.write(sink, Array.get(array, i));
            }
        }
        return total;
    }

    @Override
    public Object read(BufferedSource source) throws IOException{
        int size = source.readInt();
        if (size == -1) {
            return null;
        }
        Object array = Array.newInstance(componentType, size);
        for (int i = 0 ; i < size ; i ++){
            Array.set(array, i, componentAdapter.read(source));
        }
        return array;
    }

    @Override
    public int evaluateSize(Object array){
        if(array == null){
            return 4;
        }
        int total = 0;
        int size = Array.getLength(array);
        total += 4;
        if(size > 0){
            for(int i = 0 ; i < size ; i ++){
                total += componentAdapter.evaluateSize(Array.get(array, i));
            }
        }
        return total;
    }

}
