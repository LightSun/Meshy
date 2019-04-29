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

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.TypeAdapterContext;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author heaven7
 */
public final class CollectionTypeAdapter extends TypeAdapter {

    private final TypeAdapterContext context;
    private final TypeAdapter componentAdapter;

    public CollectionTypeAdapter(TypeAdapterContext context, TypeAdapter componentAdapter) {
        this.context = context;
        this.componentAdapter = componentAdapter;
    }

    @Override
    public int write(BufferedSink sink, Object coll) throws IOException{
        if(coll == null){
            sink.writeInt(-1);
            return 4;
        }
        int total = 0;
        int size = ((Collection)coll).size();
        sink.writeInt(size);
        total += 4;

        String name = coll.getClass().getName();
        sink.writeInt(name.length());
        sink.writeUtf8(name);
        total += 4 + name.length();

        if(size > 0){
            if(coll instanceof List){
                List list = (List) coll;
                for(int i = 0 ; i < size ; i ++){
                    total += componentAdapter.write(sink, list.get(i));
                }
            }else {
                for (Object child: ((Collection) coll)){
                    total += componentAdapter.write(sink, child);
                }
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
        String name = source.readUtf8(source.readInt());
        Collection coll = context.createCollection(name);

        for (int i = 0 ; i < size ; i ++){
            coll.add(componentAdapter.read(source));
        }
        return coll;
    }

    @Override
    public int evaluateSize(Object coll){
        if(coll == null){
            return 4;
        }
        int total = 0;
        int size = ((Collection)coll).size();
        total += 4;

        String name = coll.getClass().getName();
        total += 4 + name.length();

        if(size > 0){
            if(coll instanceof List){
                List list = (List) coll;
                for(int i = 0 ; i < size ; i ++){
                    total += componentAdapter.evaluateSize(list.get(i));
                }
            }else {
                for (Object child: ((Collection) coll)){
                    total += componentAdapter.evaluateSize(child);
                }
            }
        }
        return total;
    }

}
