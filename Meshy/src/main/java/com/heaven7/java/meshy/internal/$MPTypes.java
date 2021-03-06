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
package com.heaven7.java.meshy.internal;

import com.heaven7.java.base.anno.Hide;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.meshy.*;
import com.heaven7.java.meshy.adapter.ArrayTypeAdapter;
import com.heaven7.java.meshy.adapter.CollectionTypeAdapter;
import com.heaven7.java.meshy.adapter.MapTypeAdapter;
import com.heaven7.java.meshy.adapter.ObjectTypeAdapter;

import java.lang.reflect.*;
import java.util.*;

/**
 * this is a helper class used for internal.
 * @author heaven7
 */
@Hide
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
        public int getSubNodeCount(){
            if(Predicates.isEmpty(subType)){
                if(!Predicates.isEmpty(varNodes)){
                    return varNodes.size();
                }
                return 0;
            }else {
                return subType.size();
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

        public TypeAdapter getTypeAdapter(Meshy meshy, float applyVersion) {
            if(isArray){
                GenericNode subNode = getSubNode(0);
                return new ArrayTypeAdapter(subNode.getTypeClass(0),
                        subNode.getTypeAdapter(meshy, applyVersion));
            }
            TypeAdapterContext context = meshy.getTypeAdapterContext();
            MessageProtocolContext mpContext = meshy.getMessageProtocolContext();
            if(type != null){
                TypeAdapter typeAdapter = meshy.getTypeAdapter(this, applyVersion);
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
                            getSubNode(0).getTypeAdapter(meshy, applyVersion));
                }else if(Map.class.isAssignableFrom(type) || context.isMap(type)){
                    TypeAdapter key, value;

                    int count = getSubNodeCount();
                    switch (count){
                        case 0:
                            key = mpContext.getKeyAdapter(type);
                            value = mpContext.getValueAdapter(type);
                            break;
                        //often key type can be fixed. but value not. like sparse array.
                        case 1:
                            key = mpContext.getKeyAdapter(type);
                            value = getSubNode(0).getTypeAdapter(meshy, applyVersion);
                            break;

                        case 2:
                            key = getSubNode(0).getTypeAdapter(meshy, applyVersion);
                            value = getSubNode(1).getTypeAdapter(meshy, applyVersion);
                            break;

                        default:
                            throw new UnsupportedOperationException("sub node count for map must <= 2. but is " + count);
                    }
                    if(key == null){
                        throw new IllegalStateException("can't find target key adapter for map class = " + type.getName());
                    }
                    if(value == null){
                        throw new IllegalStateException("can't find target value adapter for map class = " + type.getName());
                    }
                    return new MapTypeAdapter(context, key, value);
                }else {
                    return new ObjectTypeAdapter(meshy, applyVersion);
                }
            }else if(!Predicates.isEmpty(varNodes)){
                return varNodes.get(0).getTypeAdapter(meshy, applyVersion);
            }else {
                throw new UnsupportedOperationException("un-reach here");
            }
        }
    }
    private static class TypeVariablePair{
        public final String declareName;
        public final GenericNode node;
        public TypeVariablePair(String declareName, GenericNode node) {
            this.declareName = declareName;
            this.node = node;
        }
    }
}
