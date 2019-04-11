package com.heaven7.java.message.protocol;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.message.protocol.internal.MUtils;
import com.heaven7.java.message.protocol.secure.MessageSecureFactory;

import java.util.*;

/**
 * @author heaven7
 */
public final class MessageConfigManager {

    private static final Comparator<MessageConfig.Pair<String, Float>> sCompatComparator =
            new Comparator<MessageConfig.Pair<String, Float>>() {
        @Override
        public int compare(MessageConfig.Pair<String, Float> o1, MessageConfig.Pair<String, Float> o2) {
            return Float.compare(o1.value, o2.value);
        }
    };
    //-------------------------
    private static final HashMap<String, List<MessageConfig.Pair<String, Float>>> sCompatClass; //AESC
    private static final SparseArrayDelegate<MessageSecure> sSecures;
    private static MessageSignature sMsgSignature;
    private static String sSignKey;
    private static float sVersion;

    static {
        sSecures = SparseFactory.newSparseArray(5);
        sCompatClass = new HashMap<>();
    }

    public static void initialize(MessageConfig config){
        sVersion = Float.valueOf(config.version);
        sSignKey = config.signKey;
        try{
            sMsgSignature = (MessageSignature) Class.forName(config.signClassName).newInstance();
        }catch (Exception e){
            throw new ConfigException("wrong Message Signature class. " + config.signClassName, e);
        }
        //handle secures
        MessageConfig.Pair<Integer, String> currentPair = null;
        try{
            //sample : 1, xxx.xxx(sffsfsf, true);
            for(MessageConfig.Pair<Integer, String> pair: config.securesPairs){
                currentPair = pair;
                String value = pair.value;
                int index1 = value.indexOf("(");
                int index2 = value.indexOf(")");
                String className = value.substring(0, index1);
                String[] params = value.substring(index1 + 1, index2).trim().split(",");
                sSecures.put(pair.key, MessageSecureFactory.createMessageSecure(className, params));
            }
        }catch (Exception e){
            throw MUtils.runtime(e, ConfigException.class, "when handle MessageSecure: " + currentPair.value);
        }
        //handle compat class
        if(!Predicates.isEmpty(config.compatClasses)){
            //sort and put
            for (MessageConfig.Pair<String, List<MessageConfig.Pair<String, Float>>> pair: config.compatClasses){
                Collections.sort(pair.value, sCompatComparator);
                sCompatClass.put(pair.key, pair.value);
            }
        }
    }

    /**
     * get the compat class. this is useful for compat message entity classes.
     * @param className the expect classname
     * @param version the version code
     * @return the actually class as expect
     * @throws ClassNotFoundException if {@linkplain Class#forName(String)} occurs
     */
    public static Class<?> getCompatClass(String className, float version) throws ClassNotFoundException {
        List<MessageConfig.Pair<String, Float>> pairs = sCompatClass.get(className);
        if(Predicates.isEmpty(pairs)){
            return Class.forName(className);
        }
        for (MessageConfig.Pair<String, Float> pair : pairs){
            if(pair.value >= version){
                return Class.forName(pair.key);
            }
        }
        throw new ConfigException("wrong version code("+ version +") or class compat had not configuration.");
    }

    /**
     * get the encode type by random .
     * @return the encode type
     */
    public static int randomEncodeType(){
        try{
            int index = new Random().nextInt(sSecures.size());
            return sSecures.keyAt(index);
        }catch (IllegalArgumentException e){
            throw new ConfigException("you must config the message secure.");
        }
    }

    public static MessageSecure getMessageSecure(int type){
        return sSecures.get(type);
    }
    public static String signatureMessage(byte[] data){
        return sMsgSignature.signature(data, sSignKey);
    }

    public static float getVersion() {
        return sVersion;
    }
    public static class ConfigException extends RuntimeException{
        public ConfigException() {
        }
        public ConfigException(String message) {
            super(message);
        }
        public ConfigException(String message, Throwable cause) {
            super(message, cause);
        }
        public ConfigException(Throwable cause) {
            super(cause);
        }
    }
}
