package com.heaven7.java.message.protocol;

/**
 * @author heaven7
 */
/*public*/ abstract class BaseMemberProxy implements MemberProxy {

    private boolean packed;

    @Override
    public final boolean isPackedType() {
        return packed;
    }

    protected int parseType(Class<?> memClazz) {
        packed = false;
        if(memClazz == byte.class){
            return TYPE_BYTE;
        }else if(memClazz == Byte.class){
            packed = true;
            return TYPE_BYTE;
        }else if(memClazz == short.class){
            return TYPE_SHORT;
        }
        else if(memClazz == Short.class){
            packed = true;
            return TYPE_SHORT;
        }
        else if(memClazz == int.class){
            return TYPE_INT;
        } else if(memClazz == Integer.class){
            packed = true;
            return TYPE_INT;
        }
        else if(memClazz == long.class){
            return TYPE_LONG;
        }else if(memClazz == Long.class){
            packed = true;
            return TYPE_LONG;
        }

        else if(memClazz == float.class){
            return TYPE_FLOAT;
        }else if(memClazz == Float.class){
            packed = true;
            return TYPE_FLOAT;
        }
        else if(memClazz == double.class){
            return TYPE_DOUBLE;
        }else if(memClazz == Double.class){
            packed = true;
            return TYPE_DOUBLE;
        }
        else if(memClazz == boolean.class){
            return TYPE_BOOLEAN;
        }else if(memClazz == Boolean.class){
            packed = true;
            return TYPE_BOOLEAN;
        }
        else if(memClazz == char.class){
            return TYPE_CHAR;
        }else if(memClazz == Character.class){
            packed = true;
            return TYPE_CHAR;
        }
        else if(memClazz == String.class){
            return TYPE_STRING;
        }
        return TYPE_OBJECT;
    }
}
