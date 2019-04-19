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

/**
 * magic,total-len,version,len,sign,en-type,len.all-data
 *                                   msg
 *                                   type[login, logout, tick, normal],len,full-class-name,ds[len,value,len,value...]
 * @author heaven7
 */
public class MessageProtocol {

    public static final int MAGIC = 0x0053;  //12 + 15 + 7 + 4 + 0 + 21 + 4 + 13 + 7 = 83 (mpheaven7)

    private float version;
    private String sign;
    private int encodeType;    //-1 means none.
    private byte[] decodeData;

    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }

    public float getVersion() {
        return version;
    }
    public void setVersion(float version) {
        this.version = version;
    }

    public int getEncodeType() {
        return encodeType;
    }
    public void setEncodeType(int encodeType) {
        this.encodeType = encodeType;
    }

    public byte[] getDecodeData() {
        return decodeData;
    }

    public void setDecodeData(byte[] decodeData) {
        this.decodeData = decodeData;
    }
}
