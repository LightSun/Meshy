package com.heaven7.java.message.protocol;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author heaven7
 */
/*public*/ final class MessageProtocolUtils {

    public static MessageProtocol readMessageProtocol(BufferedSource source) throws IOException{
        MessageProtocol protocal = new MessageProtocol();
        //version
        protocal.setVersion(Float.intBitsToFloat(source.readInt()));
        //sign
        int len = source.readInt();
        protocal.setSign(source.readUtf8(len));
        //encode type
        protocal.setEncodeType(source.readInt());

        //handle encode data
        try {
            protocal.setDecodeData(MessageConfigManager.getMessageSecure(protocal.getEncodeType()).decode(source));
        } catch (GeneralSecurityException e) {
           throw new RuntimeException("decode data error." , e);
        }
        //handle sign
        String msg = MessageConfigManager.signatureMessage(protocal.getDecodeData());
        if(!msg.equals(protocal.getSign())){
            throw new IllegalStateException("message sign error. local is " + msg + " ,remote is " + protocal.getSign());
        }
        return protocal;
    }
    public static int writeMessageProtocol(BufferedSink sink, Message<?> message,
                                           int encodeType, float version) throws IOException, GeneralSecurityException{
        return writeMessageProtocol0(sink, message, encodeType, version);
    }
    public static int evaluateMessageProtocolSize(Message<?> message,
                                                  int encodeType, float version) throws IOException, GeneralSecurityException{
        return writeMessageProtocol0(null, message, encodeType, version);
    }
    private static int writeMessageProtocol0(BufferedSink sink, Message<?> message,
                                             int encodeType, float version) throws IOException, GeneralSecurityException{
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        MessageIO.writeMessage(Okio.buffer(Okio.sink(buffer)), message, version);
        byte[] bufferBytes = buffer.toByteArray();
        String sign = MessageConfigManager.signatureMessage(bufferBytes);
        if (sink != null) {
              sink.writeInt(Float.floatToIntBits(version));
              sink.writeInt(sign.length());
              sink.writeUtf8(sign);
              sink.writeInt(encodeType);
        }
        int encodeSize = MessageConfigManager.getMessageSecure(encodeType).encode(sink, bufferBytes);
        //start write
        if(sink != null){
            sink.flush();
        }
        return 4  + 4 + sign.length() + 4 + encodeSize;
    }
}
