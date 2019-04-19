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

/**
 * @author heaven7
 */
public final class NullTypeAdapter extends TypeAdapter{

    public static final NullTypeAdapter INSTANCE = new NullTypeAdapter();

    private NullTypeAdapter(){}

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException {
        sink.writeByte(-1);
        return 1;
    }
    @Override
    public Object read(BufferedSource source) throws IOException {
        source.skip(1);
        return null;
    }
    @Override
    public int evaluateSize(Object obj) {
        return 1;
    }
}
