package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.base.util.Platforms;
import com.heaven7.java.base.util.TextUtils;
import com.heaven7.java.message.protocol.MemberProxy;
import com.heaven7.java.message.protocol.anno.MethodMember;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author heaven7
 */
public final class MUtils {

   /* public static RuntimeException runtime(Exception e, Class<? extends RuntimeException> clazz, String msg){
        if(clazz.isAssignableFrom(e.getClass())){
            msg = e.getMessage() + Platforms.getNewLine() + msg;
        }
        Throwable cause = e.getCause() != null ? e.getCause() : e;
        try{
            return clazz.getConstructor(String.class, Throwable.class).newInstance(msg, cause);
        }catch (Exception exp){
            exp.printStackTrace(); //unexpect
            if (e instanceof RuntimeException) {
                return (RuntimeException) e;
            }else{
                throw new RuntimeException(msg, e);
            }
        }
    }

    public static RuntimeException runtime(Exception e){
        if( e instanceof RuntimeException){
            return (RuntimeException) e;
        }else {
            throw new RuntimeException(e);
        }
    }*/

    public static void copyProperties(Object from, Object to, List<MemberProxy> fromProxies, List<MemberProxy> toProxies)
            throws InvocationTargetException, IllegalAccessException {
        //use cache if need
        CacheGroup group = getCacheGroup();
        Key key = new Key(from.getClass(), to.getClass());
        List<Value> values = group.getValues(key);
        if(values == null){
            //no cache
            for(MemberProxy proxy : fromProxies){
                String prop = proxy.getPropertyName();
                MemberProxy targetProxy = null;
                for (MemberProxy toProxy : toProxies){
                    if(toProxy.getPropertyName().equals(prop)){
                        targetProxy = toProxy;
                        break;
                    }
                }
                //have the same property. copy it
                if(targetProxy != null){
                    targetProxy.setObject(to, proxy.getObject(from));
                    group.add(key, new Value(proxy, targetProxy));
                }
            }
        }else if(!values.isEmpty()){
            //cache
           for (Value value : values){
               value.toProxy.setObject(to, value.fromProxy.getObject(from));
           }
        }
    }
    public static String getPropertyFromMethod(Method method){
        MethodMember ssm = method.getAnnotation(MethodMember.class);
        String value = ssm.property();
        if(TextUtils.isEmpty(value)){
            String name = method.getName();
            if(name.startsWith("get") || name.startsWith("set")){
                value = name.substring(3);
            }else{
                value = name;
            }
        }
        return value;
    }

    public static CacheGroup getCacheGroup(){
        if(sGroup == null){
            sGroup = new CacheGroup();
        }
        return sGroup;
    }
    private static volatile CacheGroup sGroup;


    private static class CacheGroup{
        final Map<Key, List<Value>> map = new HashMap<>();
        final ReentrantReadWriteLock mLock = new ReentrantReadWriteLock(false);

        public void add(Key key, Value value){
            List<Value> values;
            mLock.writeLock().lock();
            try {
                values = map.get(key);
                if(values == null){
                    values = new ArrayList<>();
                    map.put(key, values);
                }
                values.add(value);
            }finally {
                mLock.writeLock().unlock();
            }
        }
        public List<Value> getValues(Key key){
            mLock.readLock().lock();
            try {
                return map.get(key);
            }finally {
                mLock.readLock().unlock();
            }
        }
    }
    private static class Key{
        final Class fromClass;
        final Class toClass;

        public Key(Class fromClass, Class toClass) {
            this.fromClass = fromClass;
            this.toClass = toClass;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return fromClass == key.fromClass &&
                    toClass == key.toClass;
        }
        @Override
        public int hashCode() {
            return Objects.hash(fromClass, toClass);
        }
    }
    private static class Value{
        final MemberProxy fromProxy;
        final MemberProxy toProxy;
        public Value(MemberProxy fromProxy, MemberProxy toProxy) {
            this.fromProxy = fromProxy;
            this.toProxy = toProxy;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Value value = (Value) o;
            return fromProxy == value.fromProxy &&
                    toProxy == value.toProxy;
        }
        @Override
        public int hashCode() {
            return Objects.hash(fromProxy, toProxy);
        }
    }
}
