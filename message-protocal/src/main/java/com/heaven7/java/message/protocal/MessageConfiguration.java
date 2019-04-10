package com.heaven7.java.message.protocal;

/**
 * @author heaven7
 */
public class MessageConfiguration {

    public String version;
    public String signKey;
    public String signClassName;
    public Pair<Integer, String> securesPairs;

    public static class Pair<K, V>{
        public final K key;
        public final V value;
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
