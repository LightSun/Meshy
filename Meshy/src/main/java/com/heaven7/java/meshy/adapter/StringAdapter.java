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

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.meshy.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * @author heaven7
 */
public final class StringAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException{
        String msg = (String) obj;
        if(msg == null){
            sink.writeInt(-1);
            return 4;
        }else if(msg.length() == 0){
            sink.writeInt(0);
            return 4;
        }else {
            sink.writeInt(msg.length());
            sink.writeUtf8(msg);
        }
        return 4 + msg.length();
    }

    @Override
    public Object read(BufferedSource sink) throws IOException{
        int len = sink.readInt();
        if(len == -1){
            return null;
        }else if(len == 0){
            return "";
        }else if(len > 0){
            return sink.readUtf8(len);
        }else {
            throw new UnsupportedOperationException("wrong len of string.");
        }
    }

    @Override
    public int evaluateSize(Object obj){
        String msg = (String) obj;
        if(Predicates.isEmpty(msg)){
            return 4;
        }else {
            return 4 + msg.length();
        }
    }
}
