package com.heaven7.java.message.protocal.internal;

import com.heaven7.java.base.util.Platforms;

/**
 * @author heaven7
 */
public final class MUtils {

    public static RuntimeException runtime(Exception e, Class<? extends RuntimeException> clazz, String msg){
        if(clazz.isAssignableFrom(e.getClass())){
            msg = e.getMessage() + Platforms.getNewLine() + msg;
        }
        Throwable cause = e.getCause() != null ? e.getCause() : e;
        try{
            return clazz.getConstructor(String.class, Throwable.class).newInstance(msg, cause);
        }catch (Exception exp){
            exp.printStackTrace(); //unexpect
            if (e instanceof RuntimeException) {
                return (RuntimeException) e;
            }else{
                throw new RuntimeException(msg, e);
            }
        }
    }

    public static RuntimeException runtime(Exception e){
        if( e instanceof RuntimeException){
            return (RuntimeException) e;
        }else {
            throw new RuntimeException(e);
        }
    }
}
