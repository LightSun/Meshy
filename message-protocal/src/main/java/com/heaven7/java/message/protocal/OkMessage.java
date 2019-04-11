package com.heaven7.java.message.protocal;

import com.heaven7.java.base.util.IOUtils;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author heaven7
 */
public final class OkMessage {

    /**
     * read message from input source. often from socket.
     * @param source the source as input to read
     * @param <T> the entity type
     * @return the read message
     */
    public static <T> Message<T> readMessage(BufferedSource source){
        MessageProtocal protocal;
        try {
            protocal = MessageProtocalUtils.readMessageProtocal(source);
        } catch (IOException e) {
            throw new MessageException("format is error.", e);
        }
        BufferedSource src = Okio.buffer(Okio.source(new ByteArrayInputStream(protocal.getDecodeData())));
        try{
            return MessageIO.readMessage(src, protocal.getVersion());
        }finally{
            IOUtils.closeQuietly(src);
        }
    }

    /**
     * write message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @param encodeType the encode type .which effect the message size
     * @return the write size as bytes count
     */
    public static int writeMessage(BufferedSink sink, Message<?> msg, int encodeType){
        try {
            return MessageProtocalUtils.writeMessageProtocal(sink, msg, MessageConfigManager.getVersion(), encodeType);
        } catch (IOException | GeneralSecurityException e) {
            throw new MessageException(e);
        }
    }

    /**
     * evaluate the message size as the whole message protocal.
     * @param msg the message
     * @param encodeType the encode type .which effect the message size
     * @return the expect message size as bytes count
     */
    public static int evaluateMessageSize(Message<?> msg, int encodeType){
        try {
            return MessageProtocalUtils.evaluateMessageProtocalSize(msg, MessageConfigManager.getVersion(), encodeType);
        } catch (IOException | GeneralSecurityException e) {
            throw new MessageException(e);
        }
    }
}
