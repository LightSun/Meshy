package com.heaven7.java.message.protocal;


import com.heaven7.java.base.util.Predicates;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class MessageIO {

    private static final WeakHashMap<Class<?>, List<MemberProxy>> sCache;

    static {
        sCache = new WeakHashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static void writeMessage(BufferedSink sink, Message<?> msg){
        try {
            sink.writeInt(msg.getType());
            if(msg.getEntity() == null){
                throw new NullPointerException();
            }
            if(msg.getMsg() != null){
                sink.writeInt(msg.getMsg().length());
                sink.writeUtf8(msg.getMsg());
            }else {
                sink.writeInt(0);
            }
            writeObject(sink, msg.getEntity());
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source){
        final int type;
        String str;
        try {
            type = source.readInt();
            int len = source.readInt();
            str = len > 0 ? source.readUtf8(len) : null;
        } catch (IOException e) {
            throw new MessageException(e);
        }
        Object obj = readObject(source);
        Message<T> msg = new Message<>();
        try{
            msg.setType(type);
            msg.setMsg(str);
            msg.setEntity((T) obj);
        }catch (ClassCastException e){
            throw new RuntimeException("wrong class name = " + obj.getClass().getName());
        }
        return msg;
    }

    private static Object readObject(BufferedSource source) {
        try {
            int classLen = source.readInt();
            final String fullName = source.readUtf8(classLen);
            Class<?> clazz = Class.forName(fullName);
            Object obj = clazz.newInstance();

            List<MemberProxy> proxies = sCache.get(clazz);
            if(Predicates.isEmpty(proxies)){
                proxies = getFieldProxies(clazz);
                sCache.put(clazz, proxies);
            }
            for (MemberProxy proxy : proxies){
                switch (proxy.getType()){
                    case MemberProxy.TYPE_BYTE:
                        proxy.setByte(obj, source.readByte());
                        break;
                    case MemberProxy.TYPE_SHORT:
                        proxy.setShort(obj, (short)source.readInt());
                        break;
                    case MemberProxy.TYPE_INT:
                        proxy.setInt(obj, source.readInt());
                        break;
                    case MemberProxy.TYPE_LONG:
                        proxy.setLong(obj, source.readLong());
                        break;
                    case MemberProxy.TYPE_BOOLEAN :
                        proxy.setBoolean(obj, source.readByte() == 1);
                        break;
                    case MemberProxy.TYPE_FLOAT:
                        String fstr = source.readUtf8(source.readShort());
                        proxy.setFloat(obj, Float.valueOf(fstr).floatValue());
                        break;
                    case MemberProxy.TYPE_DOUBLE :
                        String dstr = source.readUtf8(source.readShort());
                        proxy.setDouble(obj, Double.valueOf(dstr).doubleValue());
                        break;
                    case MemberProxy.TYPE_STRING :
                        int strLen = source.readInt();
                        if(strLen > 0){
                            proxy.setString(obj, source.readUtf8(strLen));
                        }
                        break;
                    case MemberProxy.TYPE_OBJECT :
                       //source.
                        byte flag = source.readByte();
                        if(flag > 0){
                            proxy.setObject(obj, readObject(source));
                        }
                        break;
                }
            }
            return obj;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException e) {
            throw new MessageException(e);
        }
    }

    private static void writeObject(BufferedSink sink, Object obj) {
        try {
            String name = obj.getClass().getName();
            sink.writeInt(name.length());
            sink.writeUtf8(name);
            Class<?> clazz = obj.getClass();

            List<MemberProxy> proxies = sCache.get(clazz);
            if(Predicates.isEmpty(proxies)){
                proxies = getFieldProxies(clazz);
                sCache.put(clazz, proxies);
            }
            for (MemberProxy proxy : proxies){
                switch (proxy.getType()){
                    case MemberProxy.TYPE_BYTE:
                        sink.writeByte(proxy.getByte(obj));
                        break;
                    case MemberProxy.TYPE_SHORT:
                        sink.writeInt(proxy.getShort(obj));
                        break;
                    case MemberProxy.TYPE_INT:
                        sink.writeInt(proxy.getInt(obj));
                        break;
                    case MemberProxy.TYPE_LONG:
                        sink.writeLong(proxy.getLong(obj));
                        break;
                    case MemberProxy.TYPE_BOOLEAN :
                        sink.writeByte(proxy.getBoolean(obj) ? 1 : 0);
                        break;
                    case MemberProxy.TYPE_FLOAT:
                        String fstr = String.valueOf(proxy.getFloat(obj));
                        sink.writeShort(fstr.length());
                        sink.writeUtf8(fstr);
                        break;
                    case MemberProxy.TYPE_DOUBLE :
                        String dstr = String.valueOf(proxy.getDouble(obj));
                        sink.writeShort(dstr.length());
                        sink.writeUtf8(dstr);
                        break;
                    case MemberProxy.TYPE_STRING :
                        String str = proxy.getString(obj);
                        sink.writeInt(str.length());
                        sink.writeUtf8(str);
                        break;
                    case MemberProxy.TYPE_OBJECT :
                        //source.
                        Object pobj = proxy.getObject(obj);
                        if(pobj == null){
                            sink.writeByte(-1);
                        }else {
                            writeObject(sink, pobj);
                        }
                        break;
                }
            }
        } catch (IllegalAccessException | IOException e) {
            throw new MessageException(e);
        }
    }

    private static List<MemberProxy> getFieldProxies(Class<?> clazz) {
        //desc
        List<MemberProxy> out = new ArrayList<>();
        getFieldProxies0(clazz, out);
        Collections.sort(out, new Comparator<MemberProxy>() {
            @Override
            public int compare(MemberProxy o1, MemberProxy o2) {
                return Integer.compare(o2.getPriority(), o1.getPriority());
            }
        });
        return out;
    }

    private static void getFieldProxies0(Class<?> clazz, List<MemberProxy> out) {
        while (clazz != Object.class){
            Field[] fields = clazz.getDeclaredFields();
            if(!Predicates.isEmpty(fields)){
                for (Field f : fields){
                    f.setAccessible(true);
                    MessageMember mm = f.getAnnotation(MessageMember.class);
                    if(mm == null){
                        continue;
                    }
                    out.add(new FieldProxy(f, mm));
                }
            }
            //latter will support method
           /* Method[] methods = clazz.getMethods();
            if(!Predicates.isEmpty(methods)){
                 for (Method method : methods){
                     method.setAccessible(true);
                     MessageMember mm = method.getAnnotation(MessageMember.class);
                     if(mm  == null){
                         continue;
                     }
                     out.add(new FieldProxy(f, mm));
                 }
            }*/
           clazz = clazz.getSuperclass();
           if(clazz == null){
               break;
           }
        }
    }
}
