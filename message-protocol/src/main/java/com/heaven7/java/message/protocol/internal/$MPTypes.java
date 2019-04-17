package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.base.util.Predicates;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @author heaven7
 */
public class $MPTypes {

    private static WeakHashMap<Class<?>, List<TypeVariablePair>> sTypeVars = new WeakHashMap<>();

    /**
     * parse the class as generic node.
     * @param clazz the owner class
     * @param type the type of field/method
     * @param parent the node to populate
     */
    public static void parseNode(final Class clazz, Type type, GenericNode parent) {
        if(type instanceof ParameterizedType){
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            List<GenericNode> subs = new ArrayList<>();
            GenericNode node;
            for (Type t : types){
                node = new GenericNode();
                parseNode(clazz, t,  node);
                subs.add(node);
            }
            parent.type = (Class<?>) ((ParameterizedType) type).getRawType();
            parent.subType = subs;
        }else if(type instanceof GenericArrayType){
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            parent.isArray = true;
            GenericNode node = new GenericNode();
            parseNode(clazz, componentType,  node);
            parent.addNode(node);
        }else if(type instanceof WildcardType){
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            if(Predicates.isEmpty(lowerBounds)){
                parseNode(clazz, upperBounds[0],  parent);
            }else {
                parseNode(clazz, lowerBounds[0], parent);
            }
        }else if(type instanceof TypeVariable){
            //indicates  Wildcard from object. that means only can be known as runtime.
            String name = ((TypeVariable) type).getName();
            if(sTypeVars.get(clazz) == null){
                ParameterizedType pt = (ParameterizedType) clazz.getGenericSuperclass();
                if(pt == null){
                    throw new UnsupportedOperationException("must extend the generic super class");
                }
                TypeVariable<?>[] types = ((TypeVariable) type).getGenericDeclaration().getTypeParameters();
                List<TypeVariablePair> pairs = new ArrayList<>();
                Type[] tts = pt.getActualTypeArguments();
                for (int i = 0 , size = tts.length ; i < size ; i ++){
                    GenericNode node = new GenericNode();
                    parseNode(clazz, tts[i], node);
                    pairs.add(new TypeVariablePair(types[i].getName(), node));
                }
                sTypeVars.put(clazz, pairs);
            }
            TypeVariablePair pair = getTypeVariablePair(clazz, name);
            parent.addTypeVariablePair(pair);
        } else if(type instanceof Class){
            parent.type =(Class<?>) type;
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
