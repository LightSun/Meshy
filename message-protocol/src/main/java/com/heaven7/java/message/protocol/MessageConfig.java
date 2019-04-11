package com.heaven7.java.message.protocol;

import java.util.List;

/**
 * @author heaven7
 */
public class MessageConfig {

    public String version;
    public String signKey;
    public String signClassName;
    public List<Pair<Integer, String>> securesPairs; // key is type, value is class name of MessageSecure
            // share-classname, real-class, since-version
    public List<Pair<String, List<Pair<String, Float>>>> compatClasses;

    public static class Pair<K, V>{
        public final K key;
        public final V value;
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
