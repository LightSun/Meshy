package com.heaven7.java.message.protocal;

/**
 * len,version,len,sign,en-type,len.all-data
 *                                   msg
 *                                   type[login, logout, tick, normal],len,full-class-name,ds[len,value,len,value...]
 * @author heaven7
 */
public class MessageProtocal {

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
