package com.heaven7.java.message.protocol;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author heaven7
 */
/*public*/ final class MessageProtocolUtils {

    public static MessageProtocol readMessageProtocal(BufferedSource source) throws IOException{
        MessageProtocol protocal = new MessageProtocol();
        //version
        protocal.setVersion(Float.intBitsToFloat(source.readInt()));
        //sign
        int len = source.readInt();
        protocal.setSign(source.readUtf8(len));
        //encode type
        protocal.setEncodeType(source.readInt());
        //encoded data
        len = source.readInt();
        byte[] encodeData = new byte[len];
        int size = source.read(encodeData);
        if(size != len){
            throw new RuntimeException("message error. size is not matched.");
        }
        //handle decode
        MessageSecure secure = MessageConfigManager.getMessageSecure(protocal.getEncodeType());
        if(secure == null){
            throw new RuntimeException("the encode type not register. type = " + protocal.getEncodeType());
        }
        try {
            protocal.setDecodeData(secure.decode(encodeData));
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
    public static int writeMessageProtocal(BufferedSink sink, Message<?> message,
                                           float version, int encodeType) throws IOException, GeneralSecurityException{
        return writeMessageProtocal0(sink, message, version, encodeType);
    }
    public static int evaluateMessageProtocalSize(Message<?> message,
                                                  float version, int encodeType) throws IOException, GeneralSecurityException{
        return writeMessageProtocal0(null, message, version, encodeType);
    }
    private static int writeMessageProtocal0(BufferedSink sink, Message<?> message,
                                            float version, int encodeType) throws IOException, GeneralSecurityException{
        ByteOutputStream buffer = new ByteOutputStream();
        int size = MessageIO.writeMessage(Okio.buffer(Okio.sink(buffer)), message);
        String sign = MessageConfigManager.signatureMessage(buffer.getBytes());
        byte[] encodeData = MessageConfigManager.getMessageSecure(encodeType).encode(buffer.getBytes());
        //start write
        if(sink != null){
            sink.writeInt(Float.floatToIntBits(version));
            sink.writeInt(sign.length());
            sink.writeUtf8(sign);
            sink.writeInt(encodeType);
            sink.writeInt(encodeData.length);
            sink.write(encodeData);
            sink.flush();
        }
        return size + 4  + 4 + sign.length() + 4 + 4 + encodeData.length;
    }
}
