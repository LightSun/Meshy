package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.*;
import com.heaven7.java.message.protocol.internal.MUtils;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author heaven7
 */
public class ObjectTypeAadpter extends TypeAdapter {

    private final TypeAdapterContext context;
    private final float version;

    public ObjectTypeAadpter(TypeAdapterContext context, float applyVersion) {
        this.context = context;
        this.version = applyVersion;
    }

    private List<MemberProxy> getMemberProxies(Type type){
        //return context.getMemberProxies(type);
        return null;
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
            if(name == null){
                name = rawClass.getName();
            }

            float localVersion = MessageConfigManager.getVersion();
            if(localVersion != version){
                targetClass = MessageConfigManager.getCompatClass(name, Math.min(localVersion, version));
                //target class is a new class.
                if(!targetClass.isAssignableFrom(rawClass)){
                    try {
                        Object obj2 = targetClass.newInstance();
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
                if (proxy.getType() == MemberProxy.TYPE_OBJECT) {
                    len += write(sink, proxy.getObject(obj));
                } else {
                   /* TypeAdapter adapter = context.getTypeAdapter(proxy);
                    if (adapter == null) {
                        throw new UnsupportedOperationException(
                                "un-register type adapter. type = " + proxy.getType());
                    }
                    len += adapter.write(sink, proxy.getObject(obj));*/
                }
            }
        } catch ( ClassNotFoundException | IllegalAccessException | IOException | InvocationTargetException e) {
            throw new MessageException(e);
        }
        return len;
    }


    @Override
    public Object read(BufferedSource source) throws IOException {
        return null;
    }

    @Override
    public int evaluateSize(Object obj) {
        return 0;
    }

}
