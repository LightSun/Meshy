package com.heaven7.java.message.protocal.internal;

import com.heaven7.java.message.protocal.*;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author heaven7
 */
public final class MUtils {

    public static MessageProtocal readMessageProtocal(BufferedSource source) throws IOException{
        MessageProtocal protocal = new MessageProtocal();
        //version
        int len = source.readInt();
        protocal.setVersion(source.readUtf8(len));
        //sign
        len = source.readInt();
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
        MessageSecure secure = MessageSecures.getMessageSecure(protocal.getEncodeType());
        if(secure == null){
            throw new RuntimeException("the encode type not register. type = " + protocal.getEncodeType());
        }
        try {
            protocal.setDecodeData(secure.decode(encodeData));
        } catch (GeneralSecurityException e) {
           throw new RuntimeException("decode data error." , e);
        }
        //handle sign
        String msg = MessageSecures.signatureMessage(protocal.getDecodeData());
        if(!msg.equals(protocal.getSign())){
            throw new IllegalStateException("message sign error. local is " + msg + " ,remote is " + protocal.getSign());
        }
        return protocal;
    }
    public static int writeMessageProtocal(BufferedSink sink, Message<?> message, String version) throws IOException, GeneralSecurityException{
        return writeMessageProtocal(sink, message, version, true);
    }
    public static int evaluateMessageProtocalSize(BufferedSink sink, Message<?> message, String version) throws IOException, GeneralSecurityException{
        return writeMessageProtocal(sink, message, version, false);
    }
    private static int writeMessageProtocal(BufferedSink sink, Message<?> message, String version, boolean write) throws IOException, GeneralSecurityException{
        ByteOutputStream buffer = new ByteOutputStream();
        int size = MessageIO.writeMessage(Okio.buffer(Okio.sink(buffer)), message);
        String sign = MessageSecures.signatureMessage(buffer.getBytes());
        int type = MessageSecures.randomEncodeType();
        byte[] encodeData = MessageSecures.getMessageSecure(type).encode(buffer.getBytes());
        //start write
        if(write){
            sink.writeInt(version.length());
            sink.writeUtf8(version);
            sink.writeInt(sign.length());
            sink.writeUtf8(sign);
            sink.writeInt(type);
            sink.writeInt(encodeData.length);
            sink.write(encodeData);
            sink.flush();
        }
        return size + 4 + version.length() + 4 + sign.length() + 4 + 4 + encodeData.length;
    }
}
