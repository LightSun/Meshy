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
 * the base packed adapter for packed primitive type.
 * @author heaven7
 */
public abstract class BasePackedAdapter extends TypeAdapter {

    @Override
    public final int write(BufferedSink sink, Object value) throws IOException{
        if(value == null){
            sink.writeByte(0);
            return 1;
        }else {
            sink.writeByte(1);
            return writeValue(sink, value) + 1;
        }
    }

    @Override
    public final Object read(BufferedSource sink) throws IOException{
        boolean isNull = sink.readByte() == 0;
        if(isNull){
            return null;
        }else {
            return readValue(sink);
        }
    }

    @Override
    public final int evaluateSize(Object value){
        return value != null ? evaluateSize0(value) + 1 : 1;
    }

    /**
     * write the value to the sink
     * @param sink the out sink
     * @param value the value. never null
     * @return the write size as byte count
     * @throws IOException if an I/O error occurs.
     */
    protected abstract int writeValue(BufferedSink sink, Object value) throws IOException;

    /**
     * read the value from source
     * @param source the source
     * @return the value . never null
     * @throws IOException if an I/O error occurs.
     */
    protected abstract Object readValue(BufferedSource source) throws IOException;

    /**
     * evaluate the size of target value in message-protocol
     * @param value the value . never null
     * @return the size as byte count
     */
    protected abstract int evaluateSize0(Object value);
}
