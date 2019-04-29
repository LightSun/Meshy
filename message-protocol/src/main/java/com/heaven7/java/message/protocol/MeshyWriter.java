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

import okio.Buffer;
import okio.BufferedSink;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @since 1.2.0
 * @author heaven7
 */
public class MeshyWriter {

    private final Meshy meshy;

    /*public*/ MeshyWriter(Meshy meshy) {
        this.meshy = meshy;
    }

    /**
     * write message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @param version the target version to write
     * @param encodeType the encode type .which effect the message size
     * @return the write size as bytes count
     */
    public int writeMessage(BufferedSink sink, Message<?> msg, int encodeType, float version){
        try {
            return writeMessageProtocol0(sink, msg, encodeType, version);
        } catch (IOException | GeneralSecurityException e) {
            throw new MeshyException(e);
        }
    }

    /**
     * write message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @param encodeType the encode type .which effect the message size
     * @return the write size as bytes count
     */
    public int writeMessage(BufferedSink sink, Message<?> msg, int encodeType){
        try {
            return writeMessageProtocol0(sink, msg, encodeType, meshy.getVersion());
        } catch (IOException | GeneralSecurityException e) {
            throw new MeshyException(e);
        }
    }

    /**
     * evaluate the message size as the whole message protocol.
     * @param msg the message
     * @param encodeType the encode type .which effect the message size
     * @return the expect message size as bytes count
     */
    public int evaluateMessageSize(Message<?> msg, int encodeType){
        try {
            return writeMessageProtocol0(null, msg, encodeType, meshy.getVersion());
        } catch (IOException | GeneralSecurityException e) {
            throw new MeshyException(e);
        }
    }
    /**
     * evaluate the message size as the whole message protocol.
     * @param msg the message
     * @param encodeType the encode type .which effect the message size
     * @param version the version to as write message
     * @return the expect message size as bytes count
     */
    public int evaluateMessageSize(Message<?> msg, int encodeType, float version){
        try {
            return writeMessageProtocol0(null, msg, encodeType, version);
        } catch (IOException | GeneralSecurityException e) {
            throw new MeshyException(e);
        }
    }
    private int writeMessageProtocol0(BufferedSink sink, Message<?> message, int encodeType, float version)
            throws IOException, GeneralSecurityException {

        Buffer buffer = new Buffer();
        int size = MessageIO.writeMessage(buffer, message, meshy, version);
        byte[] bufferBytes = buffer.readByteArray();
        assert size == bufferBytes.length;
        String sign = meshy.signatureMessage(bufferBytes);

        //version- sign_len-sign-encode_type-encode_size
        int dataSize = 4 + 4 + sign.length() + 4;
        if (sink != null) {
            buffer.writeInt(Float.floatToIntBits(version));
            buffer.writeInt(sign.length());
            buffer.writeUtf8(sign);
            buffer.writeInt(encodeType);
            dataSize += meshy.getMessageSecure(encodeType).encode(buffer, bufferBytes, meshy.getSegmentationPolicy());
            buffer.flush();
            sink.writeInt(MessageProtocol.MAGIC);
            sink.writeInt(dataSize);
            buffer.readAll(sink);
            sink.flush();
        } else {
            dataSize += meshy.getMessageSecure(encodeType).encode(null, bufferBytes, meshy.getSegmentationPolicy());
        }
        return dataSize + 8; //magic +
    }
}
