package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.message.protocol.MemberProxy;
import com.heaven7.java.message.protocol.anno.MethodMember;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * the method proxy
 * @author heaven7
 */
public class MethodProxy extends BaseMemberProxy implements MemberProxy {

    private final Method get;
    private final Method set;
    private final int priority;
    private final String property;

    //method param count: must be one ,and must support message protocol.
    public MethodProxy(Class<?> ownerClass,Method get, Method set) {
        super(ownerClass, get.getGenericReturnType());
        this.get = get;
        this.set = set;
        this.priority = get.getAnnotation(MethodMember.class).priority();
        this.property = MUtils.getPropertyFromMethod(get);
    }

    @Override
    public int getPriority() {
        return priority;
    }
    @Override
    public String getPropertyName() {
        return property;
    }
    @Override
    public void setObject(Object obj, Object value) throws IllegalAccessException, InvocationTargetException {
        set.invoke(obj, value);
    }
    @Override
    public Object getObject(Object obj) throws IllegalAccessException, InvocationTargetException {
        return get.invoke(obj);
    }
}
