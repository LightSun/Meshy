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
package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.base.anno.Hide;
import com.heaven7.java.base.util.TextUtils;
import com.heaven7.java.message.protocol.MemberProxy;
import com.heaven7.java.message.protocol.anno.MethodMember;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author heaven7
 */
@Hide
public final class MUtils {

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
