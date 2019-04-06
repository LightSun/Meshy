package com.heaven7.java.message.protocal;


import com.heaven7.java.base.util.Predicates;

import java.lang.reflect.Field;
import java.util.List;
import java.util.WeakHashMap;

public class MessageFactory {

    private static final WeakHashMap<Class<?>, List<FieldProxy>> sCache;

    static {
        sCache = new WeakHashMap<>();
    }

    public static <T> Message<T> parseMessage(int type, String fullName, byte[] data){
        try {
            Class<?> clazz = Class.forName(fullName);
            Object obj = clazz.newInstance();
            List<FieldProxy> proxies = sCache.get(fullName);
            if(Predicates.isEmpty(proxies)){
                proxies = getFieldProxies(clazz);
                sCache.put(clazz, proxies);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static List<FieldProxy> getFieldProxies(Class<?> clazz) {
        return null;
    }

}
