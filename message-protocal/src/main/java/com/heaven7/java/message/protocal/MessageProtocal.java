package com.heaven7.java.message.protocal;

/**
 * len,sign,len,version,en-type,len.all-data
 *              msg-type[login, logout, tick, normal], msg
 *                                                    len,full-class-name,ds[len,value,len,value...]
 */
public class MessageProtocal {

    private String sign;
    private String version;
    private int encodeType; // -1 means none.
    private byte[] encodedData;
}
