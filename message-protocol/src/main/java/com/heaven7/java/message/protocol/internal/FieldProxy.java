package com.heaven7.java.message.protocol.internal;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.message.protocol.MemberProxy;
import com.heaven7.java.message.protocol.anno.FieldMember;

import java.lang.reflect.Field;

/**
 * the field proxy
 * @author heaven7
 */
/*public*/ class FieldProxy extends BaseMemberProxy implements MemberProxy {

    private final Field field;
    private int priority;

    public FieldProxy(Class<?> ownerClass, Field field, @Nullable FieldMember mm) {
        super(ownerClass, field.getGenericType());
        this.field = field;
        this.priority = mm != null ? mm.value() : 0;
    }

    @Override
    public String getPropertyName() {
        return field.getName();
    }
    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setObject(Object obj, Object value) throws IllegalAccessException {
        field.set(obj, value);
    }

    @Override
    public Object getObject(Object obj) throws IllegalAccessException  {
        return field.get(obj);
    }
}
