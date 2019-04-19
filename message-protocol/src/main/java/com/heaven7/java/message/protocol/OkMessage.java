package com.heaven7.java.message.protocol;

import com.heaven7.java.base.util.IOUtils;
import com.heaven7.java.message.protocol.internal.SimpleMessageProtocolContext;
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

    public static MessageProtocolContext getDefaultMessageProtocolContext(){
        return SimpleMessageProtocolContext.getDefault();
    }

    /**
     * read message from input source. often from socket. if not all data reached return null.
     * @param source the source as input to read
     * @param <T> the entity type
     * @return the read message. if not all data reached return null.
     */
    public static <T> Message<T> readMessage(BufferedSource source){
        MessageProtocol protocal;
        try {
            protocal = MessageProtocolUtils.readMessageProtocol(source);
        } catch (IOException e) {
            throw new MessageException("format is error.", e);
        }
        //since 1.0
        if(protocal == null){
            return null;
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
     * @param version the target version to write
     * @param encodeType the encode type .which effect the message size
     * @return the write size as bytes count
     */
    public static int writeMessage(BufferedSink sink, Message<?> msg, int encodeType, float version){
        try {
            return MessageProtocolUtils.writeMessageProtocol(sink, msg, encodeType, version);
        } catch (IOException | GeneralSecurityException e) {
            throw new MessageException(e);
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
            return MessageProtocolUtils.writeMessageProtocol(sink, msg, encodeType, MessageConfigManager.getVersion());
        } catch (IOException | GeneralSecurityException e) {
            throw new MessageException(e);
        }
    }

    /**
     * evaluate the message size as the whole message protocol.
     * @param msg the message
     * @param encodeType the encode type .which effect the message size
     * @return the expect message size as bytes count
     */
    public static int evaluateMessageSize(Message<?> msg, int encodeType){
        try {
            return MessageProtocolUtils.evaluateMessageProtocolSize(msg, encodeType, MessageConfigManager.getVersion());
        } catch (IOException | GeneralSecurityException e) {
            throw new MessageException(e);
        }
    }
    /**
     * evaluate the message size as the whole message protocol.
     * @param msg the message
     * @param encodeType the encode type .which effect the message size
     * @param version the version to as write message
     * @return the expect message size as bytes count
     */
    public static int evaluateMessageSize(Message<?> msg, int encodeType, float version){
        try {
            return MessageProtocolUtils.evaluateMessageProtocolSize(msg, encodeType, version);
        } catch (IOException | GeneralSecurityException e) {
            throw new MessageException(e);
        }
    }
}
