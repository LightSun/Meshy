/*
 * Copyright 2019
 * heaven7(donshine723@gmail.com)

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.java.meshy.internal;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.meshy.MemberProxy;
import com.heaven7.java.meshy.anno.FieldMember;

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
