package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.*;
import com.heaven7.java.message.protocol.internal.MUtils;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * the object type adapter .support null.
 * @author heaven7
 */
public class ObjectTypeAdapter extends TypeAdapter {

    private final MessageProtocolContext mMPContext;
    private final TypeAdapterContext context;
    private final float version;

    public ObjectTypeAdapter(MessageProtocolContext mMPContext, TypeAdapterContext context, float applyVersion) {
        this.mMPContext = mMPContext;
        this.context = context;
        this.version = applyVersion;
    }

    private List<MemberProxy> getMemberProxies(Class<?> clazz){
        return mMPContext.getMemberProxies(clazz);
    }

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException {
        int len = 0;
        try {
            if (obj == null) {
                sink.writeByte(0);
                return 1;
            } else {
                sink.writeByte(1);
                len += 1;
            }
            final Class<?> rawClass = obj.getClass();
            Class<?> targetClass;   //target class to find member proxy.
            //target version > < = local version
            String name = MessageConfigManager.getRepresentClassName(rawClass);

            float localVersion = MessageConfigManager.getVersion();
            if(localVersion != version){
                targetClass = MessageConfigManager.getCompatClass(name, Math.min(localVersion, version));
                //target class is a new class.
                if(!targetClass.isAssignableFrom(rawClass)){
                    try {
                        Object obj2 = context.newInstance(targetClass);
                        MUtils.copyProperties(obj, obj2, getMemberProxies(rawClass),
                                getMemberProxies(targetClass));
                        obj = obj2;
                    }catch (Exception e){
                        throw new MessageException("create compat object failed, class is "
                                + targetClass.getName(), e);
                    }
                }
            } else {
                targetClass = MessageConfigManager.getCompatClass(name, version);
            }
            //write class name
            sink.writeInt(name.length());
            sink.writeUtf8(name);
            len += 4 + name.length();
            // write data.
            List<MemberProxy> proxies = getMemberProxies(targetClass);
            for (MemberProxy proxy : proxies) {
               // System.out.println("write >>> object:  propName = " + proxy.getPropertyName());
                TypeAdapter adapter = proxy.getTypeAdapter(mMPContext, context, version);
                len += adapter.write(sink, proxy.getObject(obj));
            }
        } catch ( ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            throw new MessageException(e);
        }
        return len;
    }


    @Override
    public Object read(BufferedSource source) throws IOException {
        try {
            if (source.readByte() != 1) {
                return null;
            }
            int classLen = source.readInt();
            final String fullName = source.readUtf8(classLen);
            // remote version > < = local version
            Class<?> clazz;
            clazz = MessageConfigManager.getCompatClass(fullName,
                    Math.min(version, MessageConfigManager.getVersion()));
            Object obj = context.newInstance(clazz);

            List<MemberProxy> proxies = getMemberProxies(clazz);
            for (MemberProxy proxy : proxies) {
                TypeAdapter adapter = proxy.getTypeAdapter(mMPContext, context, version);
                Object value = adapter.read(source);
                proxy.setObject(obj, value);
            }
            return obj;
        } catch (ClassNotFoundException
                | IllegalAccessException
                | NullPointerException
                | InvocationTargetException e) {
            throw new MessageException(e);
        }
    }

    @Override
    public int evaluateSize(Object obj) {
        int len = 0;
        try {
            if (obj == null) {
                return 1;
            } else {
                len += 1;
            }
            final Class<?> rawClass = obj.getClass();
            Class<?> targetClass;   //target class to find member proxy.
            //target version > < = local version
            String name = MessageConfigManager.getRepresentClassName(rawClass);

            float localVersion = MessageConfigManager.getVersion();
            if(localVersion != version){
                targetClass = MessageConfigManager.getCompatClass(name, Math.min(localVersion, version));
                //target class is a new class.
                if(!targetClass.isAssignableFrom(rawClass)){
                    try {
                        Object obj2 = context.newInstance(targetClass);
                        MUtils.copyProperties(obj, obj2, getMemberProxies(rawClass),
                                getMemberProxies(targetClass));
                        obj = obj2;
                    }catch (Exception e){
                        throw new MessageException("create compat object failed, class is "
                                + targetClass.getName(), e);
                    }
                }
            } else {
                targetClass = MessageConfigManager.getCompatClass(name, version);
            }
            //write class name
            len += 4 + name.length();
            // write data.
            List<MemberProxy> proxies = getMemberProxies(targetClass);
            for (MemberProxy proxy : proxies) {
                TypeAdapter adapter = proxy.getTypeAdapter(mMPContext, context, version);
                len += adapter.evaluateSize(proxy.getObject(obj));
            }
        } catch ( ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            throw new MessageException(e);
        }
        return len;
    }

}
