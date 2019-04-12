package com.heaven7.java.message.protocol;

import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * the message config used for communicate protocol.
 * @author heaven7
 */
public class MessageConfig {

    /**
     * the version of current used
     */
    public float version;
    /**
     * the sign key
     */
    public String signKey;
    /**
     * the message signature
     */
    public MessageSignature signature;

    /**
     * the segmentation policy for {@linkplain MessageSecure}
     */
    public SegmentationPolicy segmentationPolicy;
    /**
     * the message secures. key is type, value is MessageSecure
     */
    public SparseArrayDelegate<MessageSecure> secures;
            // share-classname, real-class, since-version
    /**
     * the message entity compat map. key is classname which used for 'CS' communication.
     * Pair<Class<?>, Float>: means key is the real class used , float means the version this class since used.
     */
    public Map<String, List<Pair<Class<?>, Float>>> compatMap;


    public static MessageConfig newConfig(){
        MessageConfig config = new MessageConfig();
        config.compatMap = new HashMap<>();
        config.secures = SparseFactory.newSparseArray(5);
        return config;
    }

    /**
     * a pair contains key and value
     * @param <K> the key type
     * @param <V> the value type
     */
    public static class Pair<K, V>{
        public final K key;
        public final V value;
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(key, pair.key) &&
                    Objects.equals(value, pair.value);
        }
        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
