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
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

/**
 * the type adapter which read and write boolean type.
 * @author heaven7
 */
public final class BooleanAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException{
        sink.writeByte((Boolean)obj ? 1 : 0);
        return 1;
    }
    public Object read(BufferedSource sink) throws IOException{
        return sink.readByte() == 1;
    }

    @Override
    public int evaluateSize(Object obj){
        return 1;
    }
}
