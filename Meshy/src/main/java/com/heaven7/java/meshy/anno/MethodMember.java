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
package com.heaven7.java.meshy.anno;

import com.heaven7.java.meshy.MemberProxy;

import java.lang.annotation.*;

/**
 * indicate the method member.
 * @see MemberProxy
 * @author heaven7
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodMember {

    byte TYPE_GET = 1;
    byte TYPE_SET = 2;

    byte value() default TYPE_SET;

    /**
     * the priority of serialize or deserialize
     * @return the priority
     */
    int priority() default 0;

    /**
     * define the property of method which used to make-pair('get' with 'set'). if you do not proguard the method name. this can be empty.
     * @return the property name.
     */
    String property() default "";
}
