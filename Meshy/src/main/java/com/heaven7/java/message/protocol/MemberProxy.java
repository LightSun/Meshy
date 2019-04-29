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
package com.heaven7.java.message.protocol;

import java.lang.reflect.InvocationTargetException;

/**
 * the member proxy for field or method
 * @author heaven7
 */
public interface MemberProxy{

    int TYPE_BYTE = 1;
    int TYPE_SHORT = 2;
    int TYPE_INT = 3;
    int TYPE_LONG = 4;
    int TYPE_BOOLEAN = 5;
    int TYPE_CHAR   = 6;
    int TYPE_FLOAT = 7;
    int TYPE_DOUBLE = 8;

    byte TYPE_STRING = 9;

    byte FLAG_PACKED = 1;

    int getPriority();

    String getPropertyName();

    Class<?> getOwnerClass();

    TypeAdapter getTypeAdapter(Meshy meshy, float applyVersion);

    void setObject(Object obj, Object value) throws IllegalAccessException, InvocationTargetException;
    Object getObject(Object obj)  throws IllegalAccessException, InvocationTargetException;

}