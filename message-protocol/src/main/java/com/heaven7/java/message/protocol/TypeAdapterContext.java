package com.heaven7.java.message.protocol;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * @author heaven7
 */
public interface TypeAdapterContext {

    /**
     * create object for target class
     * @param clazz the clazz
     * @return the object
     */
    Object newInstance(Class<?> clazz);

    /**
     * create map from target 'Map' class. eg: {@linkplain com.heaven7.java.base.util.SparseArray}.
     * @param name the class name of 'Map'
     * @return the map
     */
    Map createMap(String name);

    /**
     * get the map from target object . obj can be normal map or {@linkplain com.heaven7.java.base.util.SparseArrayDelegate}
     * and etc.
     * @param obj the object type of 'map'
     * @return the transformed map
     */
    Map getMap(Object obj);

    /**
     * create collection from target class name
     * @param name the class which often extend collection. like list, set and etc.
     * @return the collection
     */
    Collection createCollection(String name);

    /**
     * indicate the class can be used as map or not.
     * @param rawType the class type
     * @return true if can used as map.
     */
    boolean isMap(Class<?> rawType);

    void registerTypeAdapter(Type type, TypeAdapter adapter);

    TypeAdapter getTypeAdapter(Type type);
}
