package com.heaven7.java.message.protocol;

import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heaven7
 */
public class BaseTypeAdapterContext implements TypeAdapterContext {

    private final HashMap<Type, TypeAdapter> mMap = new HashMap<>();

    @Override
    public Map createMap(String name) {
        if(SparseArrayMap.class.getName().equals(name)){
            return new SparseArrayMap(SparseFactory.newSparseArray(10));
        }else {
            try {
                Class<?> clazz = Class.forName(name);
                if(ConcurrentHashMap.class.isAssignableFrom(clazz)){
                    return new ConcurrentHashMap();
                }
                else if(WeakHashMap.class.isAssignableFrom(clazz)){
                    return new WeakHashMap();
                }
                else if(LinkedHashMap.class.isAssignableFrom(clazz)){
                    return new LinkedHashMap();
                }
                else if(SortedMap.class.isAssignableFrom(clazz)){
                    return new TreeMap();
                }
                else if(Map.class.isAssignableFrom(clazz)){
                    return new HashMap();
                }
            } catch (ClassNotFoundException e) {
                //ignore
            }
        }
        return null;
    }
    @Override
    public Map getMap(Object obj) {
        if(obj instanceof Map){
            return (Map) obj;
        }else if(obj instanceof SparseArrayDelegate){
            return new SparseArrayMap((SparseArrayDelegate)obj);
        }
        return null;
    }
    @Override
    public Collection createCollection(String name) {
        try {
            Class<?> clazz = Class.forName(name);
            if(LinkedList.class.isAssignableFrom(clazz)){
                return new LinkedList();
            }else if(Vector.class.isAssignableFrom(clazz)){
                return new Vector();
            }else if(List.class.isAssignableFrom(clazz)){
                return new ArrayList();
            }
        } catch (ClassNotFoundException e) {
           // e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean isMap(Class<?> rawType) {
        return Map.class.isAssignableFrom(rawType) || SparseArrayDelegate.class.isAssignableFrom(rawType);
    }

    @Override
    public void registerTypeAdapter(Type type, TypeAdapter adapter) {
        mMap.put(type, adapter);
    }
    @Override
    public TypeAdapter getTypeAdapter(Type type) {
        return mMap.get(type);
    }

    private static class SparseArrayMap<V> implements Map<Integer,V>{

        private final SparseArrayDelegate<V> sad ;
        public SparseArrayMap(SparseArrayDelegate<V> sad) {
            this.sad = sad;
        }
        @Override
        public int size() {
            return sad.size();
        }
        @Override
        public boolean isEmpty() {
            return sad.size() == 0;
        }
        @Override
        public boolean containsKey(Object key) {
            return sad.indexOfKey((Integer) key) >= 0;
        }
        @Override
        public boolean containsValue(Object value) {
            return sad.indexOfValue((V) value) >= 0;
        }
        @Override
        public V get(Object key) {
            return sad.get((Integer) key);
        }
        @Override
        public V put(Integer key, V value) {
            return  sad.put(key, value);
        }
        @Override
        public V remove(Object key) {
            return sad.getAndRemove((Integer) key);
        }
        @Override
        public void putAll(Map<? extends Integer, ? extends V> m) {
            for (Entry<? extends Integer, ? extends V> en : m.entrySet()){
                sad.put(en.getKey(), en.getValue());
            }
        }
        @Override
        public void clear() {
            sad.clear();
        }

        @Override
        public Set<Integer> keySet() {
            int size = sad.size();
            Set<Integer> set = new HashSet<>();
            for (int i = 0 ; i < size ; i ++){
                set.add(sad.keyAt(i));
            }
            return set;
        }
        @Override
        public Collection<V> values() {
            return sad.getValues();
        }
        @Override
        public Set<Entry<Integer, V>> entrySet() {
            int size = sad.size();
            Set<Entry<Integer, V>> set = new HashSet<>();
            for (int i = 0 ; i < size ; i ++){
                set.add(new Entry0<>(sad, sad.keyAt(i), sad.valueAt(i)));
            }
            return set;
        }

        private static class Entry0<V> implements Entry<Integer, V>{
            final SparseArrayDelegate<V> sad;
            Integer key;
            V value;
            public Entry0(SparseArrayDelegate<V> sad, Integer key, V value) {
                this.sad = sad;
                this.key = key;
                this.value = value;
            }
            @Override
            public Integer getKey() {
                return key;
            }
            @Override
            public V getValue() {
                return value;
            }
            @Override
            public V setValue(V value) {
                V old = sad.put(this.key, value);
                this.value = value;
                return old;
            }
            @Override
            public boolean equals(Object obj) {
                if(obj == null || !(obj instanceof Entry0)){
                   return false;
                }
                Entry0 e2 = (Entry0) obj;
                return (getKey() == null ? e2.getKey() == null : getKey().equals(e2.getKey()))
                        && (getValue() == null ? e2.getValue() == null : getValue().equals(e2.getValue()));
            }
            @Override
            public int hashCode() {
                return  (getKey()==null  ? 0 : getKey().hashCode()) ^
                        (getValue()==null ? 0 : getValue().hashCode());
            }
        }
    }
}
