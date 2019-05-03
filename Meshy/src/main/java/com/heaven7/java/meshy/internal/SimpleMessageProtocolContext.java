package com.heaven7.java.meshy.internal;

import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.meshy.MemberProxy;
import com.heaven7.java.meshy.MessageProtocolContext;
import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.adapter.*;
import com.heaven7.java.meshy.anno.FieldMember;
import com.heaven7.java.meshy.anno.FieldMembers;
import com.heaven7.java.meshy.anno.Inherit;
import com.heaven7.java.meshy.anno.MethodMember;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.heaven7.java.meshy.internal.MUtils.getPropertyFromMethod;

/**
 * @author heaven7
 */
public class SimpleMessageProtocolContext implements MessageProtocolContext {

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

    static class Creator{
       static final SimpleMessageProtocolContext INSTANCE = new SimpleMessageProtocolContext();
    }
    private SimpleMessageProtocolContext(){}

    public static SimpleMessageProtocolContext getDefault(){
        return Creator.INSTANCE;
    }

    /**
     * init the members to the cache.
     * @param classes the classes to cache members.
     * @see MemberProxy
     */
    public static void initializeMembers(List<Class<?>> classes){
        for (Class<?> clazz : classes){
            getMemberProxies2(clazz);
        }
    }
    private static List<MemberProxy> getMemberProxies2(Class<?> clazz) {
        List<MemberProxy> memberProxies = sCache.get(clazz);
        if(memberProxies == null){
            memberProxies = getMemberProxies1(clazz);
            sCache.put(clazz, memberProxies);
        }
        return memberProxies;
    }
    private static List<MemberProxy> getMemberProxies1(Class<?> clazz) {
        // desc
        List<MemberProxy> out = new ArrayList<>();
        getMemberProxies0(clazz, out);
        Collections.sort(out, sCOMPARATOR);
        return out;
    }

    private static void getMemberProxies0(Class<?> clazz, List<MemberProxy> out) {
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
                            FieldProxy proxy = new FieldProxy(clazz, f, f.getAnnotation(FieldMember.class));
                            out.add(proxy);
                            allowInherits.add(proxy);
                        }
                    }
                } else {
                    // has FieldMembers, and fields is self.
                    for (Field f : fields) {
                        f.setAccessible(true);
                        FieldProxy proxy = new FieldProxy(clazz, f, f.getAnnotation(FieldMember.class));
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
                                FieldProxy proxy = new FieldProxy(clazz, f, mm);
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
                        FieldProxy proxy = new FieldProxy(clazz, f, mm);
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
                    makePairMethods(clazz, gets, sets, true, out, allowInherits);
                }else {
                    makePairMethods(clazz, sets, gets, false, out, allowInherits);
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
    private static void makePairMethods(Class<?> owner,List<Method> src, List<Method> others, boolean srcIsGet,
                                        List<MemberProxy> out, List<MemberProxy> shareOut) {
        // assert src.size() >= others.size();
        int bigSize = src.size();
        for (int i = 0; i < bigSize; i++) {
            Method method = src.get(i);
            String property = getPropertyFromMethod(method);
            Method other = null;
            for (Method m2 : others){
                if(getPropertyFromMethod(m2).equals(property)){
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
                proxy = new MethodProxy(owner, method, other);
                main = method;
            }else {
                proxy = new MethodProxy(owner, other, method);
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

    private static TypeAdapter getTypeAdapter(int type, boolean wrapper) {
        if (wrapper) {
            return sPackAdapters.get(type);
        }
        return sTypeAdapters.get(type);
    }
    //-------------------------------------------------------------------

    @Override
    public List<MemberProxy> getMemberProxies(Class<?> clazz) {
        return getMemberProxies2(clazz);
    }

    //baseClass can be primitive and wrapper and string
    @Override
    public TypeAdapter getBaseTypeAdapter(Class<?> baseClass) {
        int type = BaseMemberProxy.parseType(baseClass);
        if(type < 0){
            return null;
        }
        int acType = BaseMemberProxy.getType(type);
        int extra = BaseMemberProxy.getFlags(type);
        return getTypeAdapter(acType, extra == MemberProxy.FLAG_PACKED);
    }

}
