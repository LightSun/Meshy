package com.heaven7.java.message.protocol;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heaven7
 */
public class WrappedTypeAdapterContext implements TypeAdapterContext {

    private final TypeAdapterContext base;

    public WrappedTypeAdapterContext(TypeAdapterContext base) {
        this.base = base;
    }
    @Override
    public Map createMap(String name) {
        Map map = base.createMap(name);
        if(map != null){
            return map;
        }
        return new HashMap();
    }
    @Override
    public Map getMap(Object obj) {
        return base.getMap(obj);
    }
    @Override
    public Collection createCollection(String name) {
        Collection collection = base.createCollection(name);
        if(collection != null){
            return collection;
        }
        return new ArrayList();
    }

    @Override
    public boolean isMap(Class<?> rawType) {
        return base.isMap(rawType);
    }
    @Override
    public void registerTypeAdapter(Type type, TypeAdapter adapter) {
        base.registerTypeAdapter(type, adapter);
    }
    @Override
    public TypeAdapter getTypeAdapter(Type type) {
        return base.getTypeAdapter(type);
    }
}
