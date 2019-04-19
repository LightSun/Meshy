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

    public static @Nullable MessageProtocol readMessageProtocol(BufferedSource source) throws IOException {
        //peek the source to check data is all reached or not,
        BufferedSource peek = source.peek();
        if(!peek.request(8)){
            return null;
        }
        peek.skip(4);
        final int totalLength = peek.readInt();
        //not all reached
        if(!peek.request(totalLength)){
            return null;
        }
        source.skip(8);

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
            throw new RuntimeException("decode data error.", e);
        }
        //handle sign
        String msg = MessageConfigManager.signatureMessage(protocal.getDecodeData());
        if (!msg.equals(protocal.getSign())) {
            throw new IllegalStateException("message sign error. local is " + msg + " ,remote is " + protocal.getSign());
        }
        return protocal;
    }

    public static int writeMessageProtocol(BufferedSink sink, Message<?> message,
                                           int encodeType, float version) throws IOException, GeneralSecurityException {
        return writeMessageProtocol0(sink, message, encodeType, version);
    }

    public static int evaluateMessageProtocolSize(Message<?> message,
                                                  int encodeType, float version) throws IOException, GeneralSecurityException {
        return writeMessageProtocol0(null, message, encodeType, version);
    }

    private static int writeMessageProtocol0(BufferedSink sink, Message<?> message,
                                             int encodeType, float version) throws IOException, GeneralSecurityException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        MessageIO.writeMessage(Okio.buffer(Okio.sink(buffer)), message, version);
        byte[] bufferBytes = buffer.toByteArray();
        String sign = MessageConfigManager.signatureMessage(bufferBytes);

        //version- sign_len-sign-encode_type-encode_size
        int dataSize = 4 + 4 + sign.length() + 4;
        if (sink != null) {
            ByteArrayOutputStream totalBuffer = new ByteArrayOutputStream();
            BufferedSink tmpSink = Okio.buffer(Okio.sink(totalBuffer));
            tmpSink.writeInt(Float.floatToIntBits(version));
            tmpSink.writeInt(sign.length());
            tmpSink.writeUtf8(sign);
            tmpSink.writeInt(encodeType);
            dataSize += MessageConfigManager.getMessageSecure(encodeType).encode(tmpSink, bufferBytes);
            tmpSink.flush();
            sink.writeInt(MessageProtocol.MAGIC);
            sink.writeInt(dataSize);
            sink.write(totalBuffer.toByteArray());
            sink.flush();
        } else {
            dataSize += MessageConfigManager.getMessageSecure(encodeType).encode(null, bufferBytes);
        }
        return dataSize + 8; //magic +
    }
}
