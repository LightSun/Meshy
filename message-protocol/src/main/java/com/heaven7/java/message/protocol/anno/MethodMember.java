package com.heaven7.java.message.protocol.anno;

import java.lang.annotation.*;

/**
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
