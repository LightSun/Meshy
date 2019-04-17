package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.message.protocol.MessageProtocolContext;
import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.TypeAdapterContext;
import com.heaven7.java.message.protocol.adapter.ArrayTypeAdapter;
import com.heaven7.java.message.protocol.adapter.CollectionTypeAdapter;
import com.heaven7.java.message.protocol.adapter.MapTypeAdapter;
import com.heaven7.java.message.protocol.adapter.ObjectTypeAdapter;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author heaven7
 */
public class $MPTypes {

    private static WeakHashMap<Class<?>, List<TypeVariablePair>> sTypeVars = new WeakHashMap<>();

    /**
     * parse the class as generic node.
     * @param ownerClass the owner class
     * @param type the generic type of field/method
     * @param parent the node to populate
     */
    public static void parseNode(final Class ownerClass, Type type, GenericNode parent) {
        if(type instanceof ParameterizedType){
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            List<GenericNode> subs = new ArrayList<>();
            GenericNode node;
            for (Type t : types){
                node = new GenericNode();
                parseNode(ownerClass, t,  node);
                subs.add(node);
            }
            parent.type = (Class<?>) ((ParameterizedType) type).getRawType();
            parent.subType = subs;
        }else if(type instanceof GenericArrayType){
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            parent.isArray = true;
            GenericNode node = new GenericNode();
            parseNode(ownerClass, componentType,  node);
            parent.addNode(node);
        }else if(type instanceof WildcardType){
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if(Predicates.isEmpty(lowerBounds)){
                parseNode(ownerClass, upperBounds[0],  parent);
            }else {
                parseNode(ownerClass, lowerBounds[0], parent);
            }
        }else if(type instanceof TypeVariable){
            //indicates  Wildcard from object. that means only can be known as runtime.
            String name = ((TypeVariable) type).getName();
            if(sTypeVars.get(ownerClass) == null){
                ParameterizedType pt = (ParameterizedType) ownerClass.getGenericSuperclass();
                if(pt == null){
                    throw new UnsupportedOperationException("must extend the generic super class");
                }
                TypeVariable<?>[] types = ((TypeVariable) type).getGenericDeclaration().getTypeParameters();
                List<TypeVariablePair> pairs = new ArrayList<>();
                Type[] tts = pt.getActualTypeArguments();
                for (int i = 0 , size = tts.length ; i < size ; i ++){
                    GenericNode node = new GenericNode();
                    parseNode(ownerClass, tts[i], node);
                    pairs.add(new TypeVariablePair(types[i].getName(), node));
                }
                sTypeVars.put(ownerClass, pairs);
            }
            TypeVariablePair pair = getTypeVariablePair(ownerClass, name);
            parent.addTypeVariablePair(pair);
        } else if(type instanceof Class){
            Class<?> clazz = (Class<?>) type;
            if(clazz.isArray()){
                parent.isArray = true;
                GenericNode node = new GenericNode();
                parseNode(ownerClass, clazz.getComponentType(),  node);
                parent.addNode(node);
            }else {
                parent.type = clazz;
            }
        }else {
            throw new RuntimeException("" + type);
        }
    }

    private static TypeVariablePair getTypeVariablePair(Class<?> clazz, String declareName){
        List<TypeVariablePair> pairs = sTypeVars.get(clazz);
        if(Predicates.isEmpty(pairs)){
            return null;
        }
        for (TypeVariablePair pair : pairs){
            if(pair.declareName.equals(declareName)){
                return pair;
            }
        }
        return null;
    }

    public static class GenericNode{
        public Class<?> type;
        public List<TypeVariablePair> pairs;
        public List<GenericNode> subType;
        public boolean isArray;

        public void addNode(GenericNode node) {
            if(subType == null){
                subType = new ArrayList<>();
            }
            subType.add(node);
        }
        public void addTypeVariablePair(TypeVariablePair pair){
            if(pairs == null){
                pairs = new ArrayList<>();
            }
            pairs.add(pair);
        }
        public Class<?> getTypeClass(int index) {
            if(type != null){
                return type;
            }
            if(isArray){
                return Array.newInstance(subType.get(0).getTypeClass(0), 0).getClass();
            }
            return pairs.get(index).node.getTypeClass(0);
        }
        public GenericNode getSubNode(int index){
            if(Predicates.isEmpty(subType)){
                if(!Predicates.isEmpty(pairs)){
                    return pairs.get(index).node;
                }
                return null;
            }else {
                return subType.get(index);
            }
        }
        public TypeAdapter getTypeAdapter(MessageProtocolContext mpContext, TypeAdapterContext context, float applyVersion) {
            if(isArray){
                GenericNode subNode = getSubNode(0);
                return new ArrayTypeAdapter(subNode.getTypeClass(0),
                        subNode.getTypeAdapter(mpContext, context, applyVersion));
            }
            if(type != null){
                //prefer dynamic register
                TypeAdapter typeAdapter = context.getTypeAdapter(type);
                if(typeAdapter != null){
                    return typeAdapter;
                }
                //base types
                TypeAdapter adapter = mpContext.getBaseTypeAdapter(type);
                if(adapter != null){
                    return adapter;
                }
                if(Collection.class.isAssignableFrom(type)){
                    return new CollectionTypeAdapter(context,
                            getSubNode(0).getTypeAdapter(mpContext, context, applyVersion));
                }else if(Map.class.isAssignableFrom(type) || context.isMap(type)){
                    return new MapTypeAdapter(context, getSubNode(0).getTypeAdapter(mpContext, context, applyVersion),
                            getSubNode(1).getTypeAdapter(mpContext, context, applyVersion));
                }else {
                    return new ObjectTypeAdapter(mpContext, context, applyVersion);
                }
            }else if(!Predicates.isEmpty(pairs)){
                return pairs.get(0).node.getTypeAdapter(mpContext, context, applyVersion);
            }else {
                throw new UnsupportedOperationException("un-reach here");
            }
        }
    }
    public static class TypeVariablePair{
        public final String declareName;
        public final GenericNode node;
        public TypeVariablePair(String declareName, GenericNode node) {
            this.declareName = declareName;
            this.node = node;
        }
    }
}
