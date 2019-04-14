package com.heaven7.java.message.protocol;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author heaven7
 */
/*public*/ abstract class BaseMemberProxy implements MemberProxy {

    public static final int FLAG_PACKED    = 1;
    public static final int FLAG_ABSTRACT  = 2;
    private int flags;

    @Override
    public final boolean isPackedType() {
        return (flags & FLAG_PACKED) == FLAG_PACKED;
    }

    protected void addFlags(int flags){
        this.flags |= flags;
    }

    protected int parseType(Type memClazz) {
        flags = 0;
        if(memClazz instanceof Class){
            if(memClazz == byte.class){
                return TYPE_BYTE;
            }else if(memClazz == Byte.class){
                addFlags(FLAG_PACKED);
                return TYPE_BYTE;
            }else if(memClazz == short.class){
                return TYPE_SHORT;
            }
            else if(memClazz == Short.class){
                addFlags(FLAG_PACKED);
                return TYPE_SHORT;
            }
            else if(memClazz == int.class){
                return TYPE_INT;
            } else if(memClazz == Integer.class){
                addFlags(FLAG_PACKED);
                return TYPE_INT;
            }
            else if(memClazz == long.class){
                return TYPE_LONG;
            }else if(memClazz == Long.class){
                addFlags(FLAG_PACKED);
                return TYPE_LONG;
            }

            else if(memClazz == float.class){
                return TYPE_FLOAT;
            }else if(memClazz == Float.class){
                addFlags(FLAG_PACKED);
                return TYPE_FLOAT;
            }
            else if(memClazz == double.class){
                return TYPE_DOUBLE;
            }else if(memClazz == Double.class){
                addFlags(FLAG_PACKED);
                return TYPE_DOUBLE;
            }
            else if(memClazz == boolean.class){
                return TYPE_BOOLEAN;
            }else if(memClazz == Boolean.class){
                addFlags(FLAG_PACKED);
                return TYPE_BOOLEAN;
            }
            else if(memClazz == char.class){
                return TYPE_CHAR;
            }else if(memClazz == Character.class){
                addFlags(FLAG_PACKED);
                return TYPE_CHAR;
            }
            else if(memClazz == String.class){
                return TYPE_STRING;
            }
            return TYPE_OBJECT;
        }else {
            if(memClazz instanceof ParameterizedType){

            }
            /*if(List.class.isAssignableFrom(memClazz)){
                if(memClazz.isInterface() || (memClazz.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT){
                    addFlags(FLAG_ABSTRACT);
                }
                return TYPE_LIST;
            }
            else if(Set.class.isAssignableFrom(memClazz)){
                if(memClazz.isInterface() || (memClazz.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT){
                    addFlags(FLAG_ABSTRACT);
                }
                return TYPE_SET;
            }
            else if(Map.class.isAssignableFrom(memClazz)){
                TypeVariable<? extends Class<?>>[] parameters = memClazz.getTypeParameters();
                //TODO adapter for
                if(memClazz.isInterface() || (memClazz.getModifiers() & Modifier.ABSTRACT) == Modifier.ABSTRACT){
                    addFlags(FLAG_ABSTRACT);
                }
                return TYPE_MAP;
            }
            else if(memClazz.isArray()){
                return TYPE_ARRAY;
            }*/
        }
        return TYPE_OBJECT;
    }
}
