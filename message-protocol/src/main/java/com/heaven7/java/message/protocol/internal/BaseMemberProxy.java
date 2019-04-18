package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.message.protocol.*;

import java.lang.reflect.Type;

/**
 * @author heaven7
 */
/*public*/ abstract class BaseMemberProxy implements MemberProxy {

    private static final int TYPE_MASK = 0xff;
    private static final int TYPE_FLAG_MASK = 0xff00;

    private final Class<?> mOwnerClass;
    private final TypeNode mNode;

    @Override
    public Class<?> getOwnerClass() {
        return mOwnerClass;
    }
    @Override
    public final TypeAdapter getTypeAdapter(MessageProtocolContext mpContext, TypeAdapterContext context, float applyVersion) {
        return mNode.getTypeAdapter(mpContext, context, applyVersion);
    }
    public static int getType(int value){
        return value & TYPE_MASK;
    }
    public static int getFlags(int value){
        return (value & TYPE_FLAG_MASK ) >> 8;
    }
    public static int parseType(Class<?> clazz){
        if(clazz == byte.class){
            return MemberProxy.TYPE_BYTE;
        }else if(clazz == Byte.class){
            return MemberProxy.TYPE_BYTE + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == short.class){
            return MemberProxy.TYPE_SHORT ;
        }
        else if(clazz == Short.class){
            return MemberProxy.TYPE_SHORT + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == int.class){
            return MemberProxy.TYPE_INT ;
        } else if(clazz == Integer.class){
            return MemberProxy.TYPE_INT  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == long.class){
            return MemberProxy.TYPE_LONG  ;
        }else if(clazz == Long.class){
            return MemberProxy.TYPE_LONG  + (MemberProxy.FLAG_PACKED << 8);
        }

        else if(clazz == float.class){
            return MemberProxy.TYPE_FLOAT ;
        }else if(clazz == Float.class){
            return MemberProxy.TYPE_FLOAT  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == double.class){
            return MemberProxy.TYPE_DOUBLE ;
        }else if(clazz == Double.class){
            return MemberProxy.TYPE_DOUBLE  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == boolean.class){
            return MemberProxy.TYPE_BOOLEAN  ;
        }else if(clazz == Boolean.class){
            return MemberProxy.TYPE_BOOLEAN  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == char.class){
            return MemberProxy.TYPE_CHAR;
        }else if(clazz == Character.class){
            return MemberProxy.TYPE_CHAR  + (MemberProxy.FLAG_PACKED << 8);
        }
        else if(clazz == String.class){
            return MemberProxy.TYPE_STRING;
        }else {
            return -1;
        }
    }

    public BaseMemberProxy(Class<?> ownerClass, Type genericType) {
        this.mNode = $MPTypes.getTypeNode(ownerClass, genericType);
        this.mOwnerClass = ownerClass;
    }
}
