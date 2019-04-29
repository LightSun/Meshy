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

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.IOUtils;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author heaven7
 */
public class MeshyReader {

    private final Meshy meshy;

    /*public*/ MeshyReader(Meshy meshy) {
        this.meshy = meshy;
    }

    /**
     * read message from input source. often from socket. if not all data reached return null.
     * @param source the source as input to read
     * @param <T> the entity type
     * @return the read message. if not all data reached return null.
     */
    public <T> Message<T> readMessage(BufferedSource source){
        MessageProtocol protocal;
        try {
            protocal = readMessageProtocol(source);
        } catch (IOException e) {
            throw new MeshyException("format is error.", e);
        }
        //since 1.0
        if(protocal == null){
            return null;
        }
        Buffer buffer = new Buffer();
        buffer.write(protocal.getDecodeData());
        try{
            return MessageIO.readMessage(buffer, meshy, protocal.getVersion());
        }finally{
            IOUtils.closeQuietly(buffer);
        }
    }
    /*
     * read message from input source. often from socket. if not all data reached return null.
     * @param source the source as input to read
     * @param <T> the entity type
     * @return the read message. if not all data reached return null.
     */
   /* public <T> Message<T> readMessageWithoutMagic(BufferedSource source){
        MessageProtocol protocal;
        try {
            protocal = readMessageProtocolWithoutMagic(source);
        } catch (IOException e) {
            throw new MeshyException("format is error.", e);
        }
        //since 1.0
        if(protocal == null){
            return null;
        }
        Buffer buffer = new Buffer();
        buffer.write(protocal.getDecodeData());
        try{
            return MessageIO.readMessage(buffer, meshy, protocal.getVersion());
        }finally{
            IOUtils.closeQuietly(buffer);
        }
    }*/
   private @Nullable MessageProtocol readMessageProtocol(BufferedSource source) throws IOException {
        //peek the source to check data is all reached or not,
        //peek.request() have bug . it will blocked all the time(even if the socket write data and flushed.)
        /*BufferedSource peek = source.peek();
        if(!peek.request(8)){
            return null;
        }
        peek.skip(4);
        final int totalLength = peek.readInt();
        if(!peek.request(totalLength)){
            return null;
        }
        source.skip(8);*/
        int magic = source.readInt();
        if(magic != MessageProtocol.MAGIC){
            throw new MeshyException("magic error.");
        }
        return readMessageProtocolWithoutMagic(source);
    }

    private @Nullable MessageProtocol readMessageProtocolWithoutMagic(BufferedSource source) throws IOException {
        int totalLen = source.readInt();
        if(!source.request(totalLen)){ // not all reached.
            return null;
        }

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
            protocal.setDecodeData(meshy.getMessageSecure(protocal.getEncodeType()).decode(source));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("decode data error.", e);
        }
        //handle sign
        String msg = meshy.signatureMessage(protocal.getDecodeData());
        if (!msg.equals(protocal.getSign())) {
            throw new IllegalStateException("message sign error. local is " + msg + " ,remote is " + protocal.getSign());
        }
        return protocal;
    }
}
