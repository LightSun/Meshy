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

import com.heaven7.java.base.anno.NonNull;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.message.protocol.adapter.ObjectTypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

import static com.heaven7.java.message.protocol.TypeAdapters.getTypeAdapter;

/**
 * the message io used to write adn read message.
 * @author heaven7
 */
/*public*/ final class MessageIO {

    /**
     * evaluate the size of message which will be write.
     * @param message the message
     * @return the size as bytes count
     */
    public static int evaluateSize(Message<?> message, Meshy meshy) {
        return evaluateSize(message, meshy, meshy.getVersion());
    }
    /**
     * evaluate the size of message which will be write.
     * @param message the message
     * @param version the apply version
     * @return the size as bytes count
     */
    public static int evaluateSize(Message<?> message, Meshy meshy, float version) {
        Throwables.checkNull(message);
        int size = 4 + 4; // type and state is int
        if (!Predicates.isEmpty(message.getMsg())) {
            size += 4 + message.getMsg().length();
        } else {
            size += 4;
        }
        size += evaluateSize(message.getEntity(), meshy, version);
        return size;
    }
    /**
     * evaluate the size of object.
     * @param obj the object if null return 0.
     * @return the size as bytes count
     */
    public static int evaluateSize(Object obj,Meshy meshy) {
        return evaluateSize(obj, meshy, meshy.getVersion());
    }
    /**
     * evaluate the size of object.
     * @param obj the object if null return 0.
     * @param version the apply version
     * @return the size as bytes count
     */
    public static int evaluateSize(Object obj, Meshy meshy, float version) {
        TypeAdapter adapter = getTypeAdapter(obj, meshy, version);
        return adapter.evaluateSize(obj);
    }
    /**
     * write the message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @return the write length as bytes count
     */
    @SuppressWarnings("unchecked")
    public static int writeMessage(BufferedSink sink, @NonNull Message<?> msg, Meshy meshy) {
        return writeMessage(sink, msg, meshy, meshy.getVersion());
    }
    /**
     * write the message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @param version the communication version. if current is server .the version is client version.
     * @return the write length as bytes count
     */
    @SuppressWarnings("unchecked")
    public static int writeMessage(BufferedSink sink, @NonNull Message<?> msg,  Meshy meshy, float version) {
        int len = 0;
        try {
            sink.writeInt(msg.getType());
            len += 4;
            if (msg.getMsg() == null) {
                sink.writeInt(-1);
                len += 4;
            } else if (msg.getMsg().length() == 0) {
                sink.writeInt(0);
                len += 4;
            } else {
                sink.writeInt(msg.getMsg().length());
                sink.writeUtf8(msg.getMsg());
                len += 4 + msg.getMsg().length();
            }
            sink.writeInt(msg.getState());
            len += 4;

            TypeAdapter adapter = getTypeAdapter(msg.getEntity(), meshy, version);
            len += adapter.write(sink, msg.getEntity());
            sink.flush();
        } catch (IOException e) {
            throw new MeshyException(e);
        }
        return len;
    }
    /**
     * read message from the source
     * @param source the source to read
     * @param meshy the meshy
     * @param <T> the entity type
     * @return the message that was read.
     */
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source, Meshy meshy) {
        return readMessage(source, meshy, meshy.getVersion());
    }
    /**
     * read message from the source by target meshy and version.
     * @param source the source to read
     * @param meshy the meshy
     * @param version the version code to read message
     * @param <T> the entity type
     * @return the message that was read.
     */
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source, Meshy meshy, float version) {
        return readMessage(source, new ObjectTypeAdapter(meshy, version));
    }
    /**
     * read message from the source
     * @param source the source to read
     * @param adapter the adapter to read the source
     * @param <T> the entity type
     * @return the message that was read.
     */
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source, TypeAdapter adapter) {
        final int type;
        String str;
        final int state;
        try {
            type = source.readInt();
            int len = source.readInt();
            if (len == -1) {
                str = null;
            } else if (len == 0) {
                str = "";
            } else if (len > 0) {
                str = source.readUtf8(len);
            } else {
                throw new UnsupportedOperationException("readMessage >> wrong string length.");
            }
            state = source.readInt();
        } catch (IOException e) {
            throw new MeshyException(e);
        }
        Object obj;
        try {
            obj = adapter.read(source);
        } catch (IOException e) {
            throw new RuntimeException("read message from remote. you should care that only support Object type.", e);
        }
        Message<T> msg = new Message<>();
        try {
            msg.setType(type);
            msg.setMsg(str);
            msg.setState(state);
            msg.setEntity((T) obj);
        } catch (ClassCastException e) {
            throw new RuntimeException(
                    "wrong class name = " + (obj != null ? obj.getClass().getName() : null));
        }
        return msg;
    }

}
