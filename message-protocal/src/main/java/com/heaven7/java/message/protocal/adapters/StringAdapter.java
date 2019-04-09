package com.heaven7.java.message.protocal.adapters;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.message.protocal.MemberProxy;
import com.heaven7.java.message.protocal.TypeAdapter;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author heaven7
 */
public class StringAdapter extends TypeAdapter {

    @Override
    public int write(BufferedSink sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        String msg = proxy.getString(obj);
        if(msg == null){
            sink.writeInt(-1);
            return 4;
        }else if(msg.length() == 0){
            sink.writeInt(0);
            return 4;
        }else {
            sink.writeInt(msg.length());
            sink.writeUtf8(msg);
        }
        return 4 + msg.length();
    }

    @Override
    public void read(BufferedSource sink, Object obj, MemberProxy proxy) throws IOException, IllegalAccessException, InvocationTargetException {
        int len = sink.readInt();
        if(len == -1){
            proxy.setString(obj, null);
        }else if(len == 0){
            proxy.setString(obj, "");
        }else if(len > 0){
            proxy.setString(obj, sink.readUtf8(len));
        }else {
            throw new UnsupportedOperationException("wrong len of string.");
        }
    }

    @Override
    public int evaluateSize(Object obj, MemberProxy proxy) throws IllegalAccessException, InvocationTargetException {
        String msg = proxy.getString(obj);
        if(Predicates.isEmpty(msg)){
            return 4;
        }else {
            return 4 + msg.length();
        }
    }
}
