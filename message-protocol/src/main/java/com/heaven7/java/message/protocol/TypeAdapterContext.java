package com.heaven7.java.message.protocol;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * @author heaven7
 */
public interface TypeAdapterContext {

    //name is class name
    Map createMap(String name);

    //obj can be map or sparseArray and etc.
    Map getMap(Object obj);

    //name is class name
    Collection createCollection(String name);

    boolean isMap(Class<?> rawType);

    void registerTypeAdapter(Type type, TypeAdapter adapter);

    TypeAdapter getTypeAdapter(Type type);
}
