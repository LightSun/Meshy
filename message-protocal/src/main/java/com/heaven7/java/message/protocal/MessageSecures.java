package com.heaven7.java.message.protocal;

import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;

/**
 * @author heaven7
 */
public final class MessageSecures {

    private static final SparseArrayDelegate<MessageSecure> sSecures;
    private static MessageSignature sMsgSignature;
    private static String sSignKey;

    static {
        sSecures = SparseFactory.newSparseArray(5);
    }

    public static MessageSecure getMessageSecure(int type){
        return sSecures.get(type);
    }
    public static void putMessageSecure(int type, MessageSecure secure){
        sSecures.put(type, secure);
    }
    public static void setMessageSignature(String key, MessageSignature ms){
        sSignKey = key;
        sMsgSignature = ms;
    }
    public static String signatureMessage(byte[] data){
        return sMsgSignature.signature(data, sSignKey);
    }
    public static int randomEncodeType(){
        //TODO random encode types
        return 0;
    }
}
