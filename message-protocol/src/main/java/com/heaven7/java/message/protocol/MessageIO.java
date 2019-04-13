package com.heaven7.java.message.protocol;

import com.heaven7.java.base.anno.NonNull;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.*;
import com.heaven7.java.message.protocol.adapter.*;
import com.heaven7.java.message.protocol.anno.*;
import com.heaven7.java.message.protocol.internal.MUtils;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.heaven7.java.message.protocol.internal.MUtils.getPropertyFromMethod;

/**
 * the message io.
 * @author heaven7
 */
public final class MessageIO {

    private static final Comparator<MemberProxy> sCOMPARATOR = new Comparator<MemberProxy>() {
        @Override
        public int compare(MemberProxy o1, MemberProxy o2) {
            return Integer.compare(o2.getPriority(), o1.getPriority());
        }
    };
    private static final WeakHashMap<Class<?>, List<MemberProxy>> sCache;
    // share cache for sub class .
    private static final WeakHashMap<Class<?>, List<MemberProxy>> sShareCache;

    private static final SparseArrayDelegate<TypeAdapter> sTypeAdapters;
    private static final SparseArrayDelegate<TypeAdapter> sPackAdapters;



    static {
        sCache = new WeakHashMap<>();
        sShareCache = new WeakHashMap<>();
        sTypeAdapters = SparseFactory.newSparseArray(10);
        sTypeAdapters.put(MemberProxy.TYPE_BYTE, new ByteAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_SHORT, new ShortAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_INT, new IntAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_LONG, new LongAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_FLOAT, new FloatAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_DOUBLE, new DoubleAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_BOOLEAN, new BooleanAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_CHAR, new CharAdapter());
        sTypeAdapters.put(MemberProxy.TYPE_STRING, new StringAdapter());

        sPackAdapters = SparseFactory.newSparseArray(10);
        sPackAdapters.put(MemberProxy.TYPE_BYTE, new BytePackedAdapter());
        sPackAdapters.put(MemberProxy.TYPE_SHORT, new ShortPackedAdapter());
        sPackAdapters.put(MemberProxy.TYPE_INT, new IntPackedAdapter());
        sPackAdapters.put(MemberProxy.TYPE_LONG, new LongPackedAdapter());
        sPackAdapters.put(MemberProxy.TYPE_FLOAT, new FloatPackedAdapter());
        sPackAdapters.put(MemberProxy.TYPE_DOUBLE, new DoublePackedAdapter());
        sPackAdapters.put(MemberProxy.TYPE_BOOLEAN, new BooleanPackedAdapter());
        sPackAdapters.put(MemberProxy.TYPE_CHAR, new CharPackedAdapter());
    }

    /**
     * init the members to the cache.
     * @param classes the classes to cache members.
     * @see MemberProxy
     */
    public static void initializeMembers(List<Class<?>> classes){
        for (Class<?> clazz : classes){
            getMemberProxies(clazz);
        }
    }

    /**
     * evaluate the size of message which will be write.
     * @param message the message
     * @return the size as bytes count
     */
    public static int evaluateSize(Message<?> message) {
        Throwables.checkNull(message);
        int size = 4; // type is int
        if (!Predicates.isEmpty(message.getMsg())) {
            size += 4 + message.getMsg().length();
        } else {
            size += 4;
        }
        size += 1 + evaluateSize(message.getEntity());
        return size;
    }
    /**
     * evaluate the size of object.
     * @param obj the object if null return 0.
     * @return the size as bytes count
     */
    public static int evaluateSize(Object obj) {
        if (obj == null) {
            return 0;
        }
        int size = 0;
        String name = obj.getClass().getName();
        size += 4 + name.length();
        List<MemberProxy> proxies = getMemberProxies(obj.getClass());
        try {
            for (MemberProxy proxy : proxies) {
                if (proxy.getType() == MemberProxy.TYPE_OBJECT) {
                    Object obj2 = proxy.getObject(obj);
                    size += evaluateSize(obj2) + 1;
                } else {
                    TypeAdapter adapter = getTypeAdapter(proxy);
                    if (adapter == null) {
                        throw new UnsupportedOperationException(
                                "un-register type adapter. type = " + proxy.getType());
                    }
                    size += adapter.evaluateSize(obj, proxy);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MessageException(e);
        }
        return size;
    }

    /**
     * write the message to the sink
     * @param sink the sink as out
     * @param msg the message to write
     * @param version the communication version. if current is server .the version is client version.
     * @return the write length as bytes count
     */
    @SuppressWarnings("unchecked")
    public static int writeMessage(BufferedSink sink, @NonNull Message<?> msg, float version) {
        int len = 0;
        try {
            sink.writeInt(msg.getType());
            len += 4;
            if (msg.getMsg() == null) {
                sink.writeInt(-1);
                len += 4;
            } else if (msg.getMsg().length() == 0) {
                sink.writeInt(0);
                len += 4;
            } else {
                sink.writeInt(msg.getMsg().length());
                sink.writeUtf8(msg.getMsg());
                len += 4 + msg.getMsg().length();
            }
            len += writeObject(sink, msg.getEntity(), version);
            sink.flush();
        } catch (IOException e) {
            throw new MessageException(e);
        }
        return len;
    }

    /**
     * read message from the source
     * @param source the source to read
     * @param version the version code to read message
     * @param <T> the entity type
     * @return the message that was read.
     */
    @SuppressWarnings("unchecked")
    public static <T> Message<T> readMessage(BufferedSource source, float version) {
        final int type;
        String str;
        try {
            type = source.readInt();
            int len = source.readInt();
            if (len == -1) {
                str = null;
            } else if (len == 0) {
                str = "";
            } else if (len > 0) {
                str = source.readUtf8(len);
            } else {
                throw new UnsupportedOperationException("readMessage >> wrong string length.");
            }
        } catch (IOException e) {
            throw new MessageException(e);
        }
        Object obj = readObject(source, version);
        Message<T> msg = new Message<>();
        try {
            msg.setType(type);
            msg.setMsg(str);
            msg.setEntity((T) obj);
        } catch (ClassCastException e) {
            throw new RuntimeException(
                    "wrong class name = " + (obj != null ? obj.getClass().getName() : null));
        }
        return msg;
    }

    //-------------------------------------- privates ===========================================

    //version: communication version
    private static Object readObject(BufferedSource source, float version) {
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
            Object obj = clazz.newInstance();

            List<MemberProxy> proxies = getMemberProxies(clazz);
            for (MemberProxy proxy : proxies) {
                if (proxy.getType() == MemberProxy.TYPE_OBJECT) {
                    Object obj2 = readObject(source, version);
                    proxy.setObject(obj, obj2);
                } else {
                    TypeAdapter adapter = getTypeAdapter(proxy);
                    if (adapter == null) {
                        throw new UnsupportedOperationException(
                                "un-register type adapter. type = " + proxy.getType());
                    }
                    adapter.read(source, obj, proxy);
                }
            }
            return obj;
        } catch (ClassNotFoundException
                | IllegalAccessException
                | InstantiationException
                | InvocationTargetException
                | IOException e) {
            throw new MessageException(e);
        }
    }

    /**
     * write the object to the out sink.
     *
     * @param sink the sink
     * @param obj the object
     * @param version the communication/target version.
     * @return the byte count by write
     */
    private static int writeObject(BufferedSink sink, @Nullable Object obj, float version) {
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
            if(version > localVersion){
                //low -> high ==> write high version . obj not change
                targetClass = MessageConfigManager.getCompatClass(name, localVersion);
                //target class is a new class.
                if(!targetClass.isAssignableFrom(rawClass)){
                    targetClass = rawClass;//TODO 是否要支持非继承类型数据的兼容?
                }
            }else if(version < localVersion){
                // high -> low -> write lower version , obj -> degrade
                //target version is below local version. we need compat for write.
                //or else do nothing
                final Class<?> compatClass = MessageConfigManager.getCompatClass(name, version);
                if(compatClass != rawClass){
                    targetClass = compatClass;
                    //not the same. so need transfer
                    if(!compatClass.isAssignableFrom(rawClass)){
                        //not extend. create and copy data.
                        try {
                            Object obj2 = compatClass.newInstance();
                            MUtils.copyProperties(obj, obj2, getMemberProxies(rawClass),
                                    getMemberProxies(compatClass));
                            obj = obj2;
                        }catch (Exception e){
                            throw new MessageException("create compat object failed, class is "
                                    + compatClass.getName(), e);
                        }
                    }
                }else {
                    targetClass = rawClass;
                }
            }else {
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
                    len += writeObject(sink, proxy.getObject(obj), version);
                } else {
                    TypeAdapter adapter = getTypeAdapter(proxy);
                    if (adapter == null) {
                        throw new UnsupportedOperationException(
                                "un-register type adapter. type = " + proxy.getType());
                    }
                    len += adapter.write(sink, obj, proxy);
                }
            }
        } catch ( ClassNotFoundException | IllegalAccessException | IOException | InvocationTargetException e) {
            throw new MessageException(e);
        }
        return len;
    }

    private static TypeAdapter getTypeAdapter(MemberProxy proxy) {
        if (proxy.isPackedType()) {
            return sPackAdapters.get(proxy.getType());
        }
        return sTypeAdapters.get(proxy.getType());
    }

    private static List<MemberProxy> getMemberProxies(Class<?> clazz) {
        List<MemberProxy> proxies = sCache.get(clazz);
        if (proxies == null) {
            proxies = getMemberProxies0(clazz);
            sCache.put(clazz, proxies);
        }
        return proxies;
    }

    private static List<MemberProxy> getMemberProxies0(Class<?> clazz) {
        // desc
        List<MemberProxy> out = new ArrayList<>();
        getMemberProxies1(clazz, out);
        Collections.sort(out, sCOMPARATOR);
        return out;
    }

    private static void getMemberProxies1(Class<?> clazz, List<MemberProxy> out) {
        final Class<?> rawClass = clazz;
        /*
         * 1, has @FieldMembers. isInherit ? judge if has @Inherit.
         */
        while (clazz != Object.class) {
            final boolean isInherit = clazz != rawClass;
            // from super. check super's allow members.
            if (isInherit) {
                List<MemberProxy> proxies = sShareCache.get(clazz);
                if (proxies != null) {
                    out.addAll(0, proxies);
                    clazz = clazz.getSuperclass();
                    if (clazz == null) {
                        break;
                    }else{
                        continue;
                    }
                }
            }
            // a list. which allow subclass inherit. this will share to cache
            List<MemberProxy> allowInherits = new ArrayList<>();
            // handle fields
            Field[] fields = clazz.getDeclaredFields();
            FieldMembers fieldMembers = clazz.getAnnotation(FieldMembers.class);
            if (fieldMembers != null) {
                if (isInherit) {
                    // has FieldMembers and in 'inherit'
                    for (Field f : fields) {
                        f.setAccessible(true);
                        Inherit fieldInherit = f.getAnnotation(Inherit.class);
                        // allow inherit
                        if (fieldInherit != null && fieldInherit.value()) {
                            FieldProxy proxy = new FieldProxy(f, f.getAnnotation(FieldMember.class));
                            out.add(proxy);
                            allowInherits.add(proxy);
                        }
                    }
                } else {
                    // has FieldMembers, and fields is self.
                    for (Field f : fields) {
                        f.setAccessible(true);
                        FieldProxy proxy = new FieldProxy(f, f.getAnnotation(FieldMember.class));
                        out.add(proxy);
                        // for share cache
                        Inherit fieldInherit = f.getAnnotation(Inherit.class);
                        if (fieldInherit != null && fieldInherit.value()) {
                            allowInherits.add(proxy);
                        }
                    }
                }
            } else {
                // no assign include all fields. need FieldMember.
                if (isInherit) {
                    // in 'inherit'
                    for (Field f : fields) {
                        f.setAccessible(true);
                        FieldMember mm = f.getAnnotation(FieldMember.class);
                        // allow inherit
                        if (mm != null) {
                            Inherit fieldInherit = f.getAnnotation(Inherit.class);
                            if (fieldInherit != null && fieldInherit.value()) {
                                FieldProxy proxy = new FieldProxy(f, mm);
                                out.add(proxy);
                                allowInherits.add(proxy);
                            }
                        }
                    }
                } else {
                    // self fields
                    for (Field f : fields) {
                        f.setAccessible(true);
                        FieldMember mm = f.getAnnotation(FieldMember.class);
                        if (mm == null) {
                            continue;
                        }
                        FieldProxy proxy = new FieldProxy(f, mm);
                        out.add(proxy);
                        // for share cache
                        Inherit fieldInherit = f.getAnnotation(Inherit.class);
                        if (fieldInherit != null && fieldInherit.value()) {
                            allowInherits.add(proxy);
                        }
                    }
                }
            }
            //handle methods
            List<Method> gets = new ArrayList<>();
            List<Method> sets = new ArrayList<>();
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                MethodMember mm = method.getAnnotation(MethodMember.class);
                if (mm == null) {
                    continue;
                }
                if (mm.value() == MethodMember.TYPE_GET) {
                    //get must be no arguments
                    if(method.getParameterTypes().length > 0){
                        throw new IllegalStateException("as 'get' method for @MethodMember must have no arguments.");
                    }
                    gets.add(method);
                } else {
                    if(method.getParameterTypes().length != 1){
                        throw new IllegalStateException("as 'set' method for @MethodMember can only have one argument.");
                    }
                    sets.add(method);
                }
            }
            //not all empty
            if(!gets.isEmpty() || !sets.isEmpty()){
                if(gets.size() >= sets.size()){
                    makePairMethods(gets, sets, true, out, allowInherits);
                }else {
                    makePairMethods(sets, gets, false, out, allowInherits);
                }
            }
            //put share cache
            sShareCache.put(clazz, allowInherits);

            clazz = clazz.getSuperclass();
            if (clazz == null) {
                break;
            }
        }
    }

    // src.size() > other.size(). for methods. method name should not be proguard.
    private static void makePairMethods(List<Method> src, List<Method> others, boolean srcIsGet,
                                        List<MemberProxy> out, List<MemberProxy> shareOut) {
       // assert src.size() >= others.size();
        int bigSize = src.size();
        for (int i = 0; i < bigSize; i++) {
            Method method = src.get(i);
            String property = getPropertyFromMethod(method);
            Method other = null;
            for (Method m2 : others){
                if(getPropertyFromMethod(method).equals(property)){
                    other = m2;
                    break;
                }
            }
            if(other == null){
                throw new IllegalStateException("MessageProtocal: can't make-pair for method. " + method.toString());
            }
            MethodProxy proxy;
            Method main;
            if(srcIsGet){
                proxy = new MethodProxy(method, other);
                main = method;
            }else {
                proxy = new MethodProxy(other, method);
                main = other;
            }
            out.add(proxy);
            //share cache for sub class
            Inherit inherit = main.getAnnotation(Inherit.class);
            if(inherit != null && inherit.value()){
                shareOut.add(proxy);
            }
        }
    }
}
