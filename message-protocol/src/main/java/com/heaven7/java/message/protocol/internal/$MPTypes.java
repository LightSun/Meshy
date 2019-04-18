package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.message.protocol.MessageProtocolContext;
import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.TypeAdapterContext;
import com.heaven7.java.message.protocol.TypeNode;
import com.heaven7.java.message.protocol.adapter.ArrayTypeAdapter;
import com.heaven7.java.message.protocol.adapter.CollectionTypeAdapter;
import com.heaven7.java.message.protocol.adapter.MapTypeAdapter;
import com.heaven7.java.message.protocol.adapter.ObjectTypeAdapter;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author heaven7
 */
public final class $MPTypes {

    private static WeakHashMap<Class<?>, List<TypeVariablePair>> sTypeVars = new WeakHashMap<>();

    public static TypeNode getTypeNode(Type type){
        $MPTypes.GenericNode node = new $MPTypes.GenericNode();
        $MPTypes.parseNode(null, type, node);
        return node;
    }
    public static TypeNode getTypeNode(Class ownerClass, Type type){
        $MPTypes.GenericNode node = new $MPTypes.GenericNode();
        $MPTypes.parseNode(ownerClass, type, node);
        return node;
    }
    /**
     * parse the class as generic node.
     * @param ownerClass the owner class
     * @param type the generic type of field/method
     * @param parent the node to populate
     */
    private static void parseNode(final Class ownerClass, Type type, GenericNode parent) {
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
            if(pair != null){
                parent.addVariableNode(pair.node);
            }else {
                throw new UnsupportedOperationException("can't find TypeVariablePair for "
                        + name + " ,from class " + ownerClass.getName());
            }
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

    private static class GenericNode implements TypeNode {
        public Class<?> type;
        public List<GenericNode> varNodes; //for variable nodes
        public List<GenericNode> subType;
        public boolean isArray;

        public void addNode(GenericNode node) {
            if(subType == null){
                subType = new ArrayList<>();
            }
            subType.add(node);
        }
        public void addVariableNode(GenericNode varNode){
            if(varNodes == null){
                varNodes = new ArrayList<>();
            }
            varNodes.add(varNode);
        }
        public Class<?> getTypeClass(int index) {
            if(type != null){
                return type;
            }
            if(isArray){
                return Array.newInstance(subType.get(0).getTypeClass(0), 0).getClass();
            }
            return varNodes.get(index).getTypeClass(0);
        }
        public GenericNode getSubNode(int index){
            if(Predicates.isEmpty(subType)){
                if(!Predicates.isEmpty(varNodes)){
                    return varNodes.get(index);
                }
                return null;
            }else {
                return subType.get(index);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GenericNode node = (GenericNode) o;
            return isArray == node.isArray &&
                    Objects.equals(type, node.type) &&
                    Objects.equals(varNodes, node.varNodes) &&
                    Objects.equals(subType, node.subType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, varNodes, subType, isArray);
        }

        public TypeAdapter getTypeAdapter(MessageProtocolContext mpContext, TypeAdapterContext context, float applyVersion) {
            if(isArray){
                GenericNode subNode = getSubNode(0);
                return new ArrayTypeAdapter(subNode.getTypeClass(0),
                        subNode.getTypeAdapter(mpContext, context, applyVersion));
            }
            if(type != null){
                TypeAdapter typeAdapter = context.getTypeAdapter(this, applyVersion);
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
            }else if(!Predicates.isEmpty(varNodes)){
                return varNodes.get(0).getTypeAdapter(mpContext, context, applyVersion);
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
