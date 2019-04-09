package com.heaven7.java.message.protocal;


import com.heaven7.java.base.anno.NonNull;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.message.protocal.adapters.*;
import com.heaven7.java.message.protocal.anno.FieldMember;
import com.heaven7.java.message.protocal.anno.FieldMembers;
import com.heaven7.java.message.protocal.anno.Inherit;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MessageIO {

    private static final WeakHashMap<Class<?>, List<MemberProxy>> sCache;
    private static final SparseArrayDelegate<TypeAdapter> sTypeAdapters;

    static {
        sCache = new WeakHashMap<>();
        sTypeAdapters = SparseFactory.newSparseArray(10);
        sTypeAdapters.put(MemberProxy.TYPE_BYTE, new ByteAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_SHORT, new ShortAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_INT, new IntAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_LONG, new LongAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_FLOAT, new FloatAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_DOUBLE, new DoubleAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_BOOLEAN, new BooleanAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_STRING, new StringAdapter());
    }
    public static int eveluateSize(Message<?> message){
        int size = 4; //type is int
        if(!Predicates.isEmpty(message.getMsg())){
            size += 4 + message.getMsg().length();
        }else {
            size += 4;
        }
        size += 1 + eveluateSize(message.getEntity());
        return size;
    }
    public static int eveluateSize(Object obj){
        if(obj == null){
            return 0;
        }
        int size = 0;
        String name = obj.getClass().getName();
        size += 4 + name.length();
        List<MemberProxy> proxies = getMemberProxies(obj.getClass());
        try {
            for (MemberProxy proxy : proxies){
                if(proxy.getType() == MemberProxy.TYPE_OBJECT){
                    Object obj2 = proxy.getObject(obj);
                    size += eveluateSize(obj2) + 1;
                }else {
                    TypeAdapter adapter = sTypeAdapters.get(proxy.getType());
                    if(adapter == null){
                        throw new UnsupportedOperationException("un-register type adapter. type = " + proxy.getType());
                    }
                    size += adapter.evaluateSize(obj, proxy);
                }
            }
        }catch (IllegalAccessException | InvocationTargetException e){
            throw new MessageException(e);
        }
        return size;
    }

    @SuppressWarnings("unchecked")
    public static int writeMessage(BufferedSink sink, @NonNull Message<?> msg){
        int len = 0;
        try {
            sink.writeInt(msg.getType());
            len += 4;
            if(msg.getMsg() == null){
                sink.writeInt(-1);
                len += 4;
            }else if(msg.getMsg().length() == 0){
                sink.writeInt(0);
                len += 4;
            } else{
                sink.writeInt(msg.getMsg().length());
                sink.writeUtf8(msg.getMsg());
                len += 4 + msg.getMsg().length();
            }
            len += writeObject(sink, msg.getEntity());
            sink.flush();
        } catch (IOException e) {
            throw new MessageException(e);
        }
        return len;
    }
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source){
        final int type;
        String str;
        try {
            type = source.readInt();
            int len = source.readInt();
            if(len == -1){
                str = null;
            }else if (len == 0){
                str = "";
            }else if (len > 0){
                str =  source.readUtf8(len);
            }else {
                throw new UnsupportedOperationException("readMessage >> wrong string length.");
            }
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
            throw new RuntimeException("wrong class name = " + (obj != null ? obj.getClass().getName() : null));
        }
        return msg;
    }

    private static Object readObject(BufferedSource source) {
        try {
            if(source.readByte() != 1){
                return null;
            }
            int classLen = source.readInt();
            final String fullName = source.readUtf8(classLen);
            Class<?> clazz = Class.forName(fullName);
            Object obj = clazz.newInstance();

            List<MemberProxy> proxies = getMemberProxies(clazz);
            for (MemberProxy proxy : proxies){
                if(proxy.getType() == MemberProxy.TYPE_OBJECT){
                    Object obj2 = readObject(source);
                    proxy.setObject(obj, obj2);
                }else {
                    TypeAdapter adapter = sTypeAdapters.get(proxy.getType());
                    if(adapter == null){
                        throw new UnsupportedOperationException("un-register type adapter. type = " + proxy.getType());
                    }
                    adapter.read(source, obj, proxy);
                }
            }
            return obj;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | IOException e) {
            throw new MessageException(e);
        }
    }

    /**
     * wtite the object to the out sink.
     * @param sink the sink
     * @param obj the object
     * @return the byte count by write
     */
    private static int writeObject(BufferedSink sink, @Nullable Object obj) {
        int len = 0;
        try {
            if(obj == null){
                sink.writeByte(0);
                return 1;
            }else {
                sink.writeByte(1);
                len += 1;
            }
            String name = obj.getClass().getName();
            sink.writeInt(name.length());
            sink.writeUtf8(name);
            len += 4 + name.length();

            List<MemberProxy> proxies = getMemberProxies(obj.getClass());
            for (MemberProxy proxy : proxies){
                if(proxy.getType() == MemberProxy.TYPE_OBJECT){
                    len += writeObject(sink, proxy.getObject(obj));
                }else {
                    TypeAdapter adapter = sTypeAdapters.get(proxy.getType());
                    if(adapter == null){
                        throw new UnsupportedOperationException("un-register type adapter. type = " + proxy.getType());
                    }
                    len += adapter.write(sink, obj, proxy);
                }
            }
        } catch (IllegalAccessException | IOException | InvocationTargetException e) {
            throw new MessageException(e);
        }
        return len;
    }

    private static List<MemberProxy> getMemberProxies(Class<?> clazz) {
        List<MemberProxy> proxies = sCache.get(clazz);
        if(proxies == null){
            proxies = getFieldProxies(clazz);
            sCache.put(clazz, proxies);
        }
        return proxies;
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
        final Class<?> rawClass = clazz;
        /*
         * 1, has @FieldMembers. isInherit ? judge if has @Inherit.
         */
        while (clazz != Object.class){
            final boolean isInherit = clazz != rawClass;
            //handle fields
            Field[] fields = clazz.getDeclaredFields();
            FieldMembers fieldMembers = clazz.getAnnotation(FieldMembers.class);
            if(fieldMembers != null){
                 if(isInherit){
                     //has FieldMembers and in 'inherit'
                     for (Field f : fields){
                         f.setAccessible(true);
                         Inherit fieldInherit = f.getAnnotation(Inherit.class);
                         //allow inherit
                         if(fieldInherit != null && fieldInherit.value()){
                             out.add(new FieldProxy(f, f.getAnnotation(FieldMember.class)));
                         }
                     }
                 }else {
                     //has FieldMembers, and fields is self.
                     for (Field f : fields){
                         f.setAccessible(true);
                         out.add(new FieldProxy(f, f.getAnnotation(FieldMember.class)));
                     }
                 }
            }else {
                //no assign include all fields. need FieldMember.
                if(isInherit){
                    //in 'inherit'
                    for (Field f : fields){
                        f.setAccessible(true);
                        FieldMember mm = f.getAnnotation(FieldMember.class);
                        //allow inherit
                        if(mm != null){
                            Inherit fieldInherit = f.getAnnotation(Inherit.class);
                            if(fieldInherit != null && fieldInherit.value()){
                                out.add(new FieldProxy(f, mm));
                            }
                        }
                    }
                }else {
                    //self fields
                    for (Field f : fields){
                        f.setAccessible(true);
                        FieldMember mm = f.getAnnotation(FieldMember.class);
                        if(mm == null){
                            continue;
                        }
                        out.add(new FieldProxy(f, mm));
                    }
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
