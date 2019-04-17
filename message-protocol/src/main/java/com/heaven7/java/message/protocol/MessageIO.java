package com.heaven7.java.message.protocol;

import com.heaven7.java.base.anno.NonNull;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.message.protocol.adapter.ObjectTypeAdapter;
import com.heaven7.java.message.protocol.internal.SimpleMessageProtocolContext;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;

import static com.heaven7.java.message.protocol.TypeAdapters.getTypeAdapter;

/**
 * the message io.
 * @author heaven7
 */
public final class MessageIO {

    /**
     * evaluate the size of message which will be write.
     * @param message the message
     * @return the size as bytes count
     */
    public static int evaluateSize(Message<?> message) {
        return evaluateSize(message, MessageConfigManager.getVersion());
    }
    /**
     * evaluate the size of message which will be write.
     * @param message the message
     * @param applyVersion the apply version
     * @return the size as bytes count
     */
    public static int evaluateSize(Message<?> message, float applyVersion) {
        Throwables.checkNull(message);
        int size = 4; // type is int
        if (!Predicates.isEmpty(message.getMsg())) {
            size += 4 + message.getMsg().length();
        } else {
            size += 4;
        }
        size += evaluateSize(message.getEntity(), applyVersion);
        return size;
    }
    /**
     * evaluate the size of object.
     * @param obj the object if null return 0.
     * @return the size as bytes count
     */
    public static int evaluateSize(Object obj) {
        return evaluateSize(obj, MessageConfigManager.getVersion());
    }
    /**
     * evaluate the size of object.
     * @param obj the object if null return 0.
     * @param version the apply version
     * @return the size as bytes count
     */
    public static int evaluateSize(Object obj, float version) {
        TypeAdapter adapter = getTypeAdapter(obj, version);
        return adapter.evaluateSize(obj);
    }
    /**
     * write the message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @return the write length as bytes count
     */
    @SuppressWarnings("unchecked")
    public static int writeMessage(BufferedSink sink, @NonNull Message<?> msg) {
        return writeMessage(sink, msg, MessageConfigManager.getVersion());
    }
    /**
     * write the message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @param version the communication version. if current is server .the version is client version.
     * @return the write length as bytes count
     */
    @SuppressWarnings("unchecked")
    public static int writeMessage(BufferedSink sink, @NonNull Message<?> msg, float version) {
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
            TypeAdapter adapter = getTypeAdapter(msg.getEntity(), version);
            len += adapter.write(sink, msg.getEntity());
            sink.flush();
        } catch (IOException e) {
            throw new MessageException(e);
        }
        return len;
    }
    /**
     * read message from the source
     * @param source the source to read
     * @param <T> the entity type
     * @return the message that was read.
     */
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source) {
        return readMessage(source, MessageConfigManager.getVersion());
    }
    /**
     * read message from the source
     * @param source the source to read
     * @param version the version code to read message
     * @param <T> the entity type
     * @return the message that was read.
     */
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source, float version) {
        return readMessage(source, new ObjectTypeAdapter(SimpleMessageProtocolContext.getDefault(),
                MessageConfigManager.getTypeAdapterContext(), version));
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
        } catch (IOException e) {
            throw new MessageException(e);
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
            msg.setEntity((T) obj);
        } catch (ClassCastException e) {
            throw new RuntimeException(
                    "wrong class name = " + (obj != null ? obj.getClass().getName() : null));
        }
        return msg;
    }

}
