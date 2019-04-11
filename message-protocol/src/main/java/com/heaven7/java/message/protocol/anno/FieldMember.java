package com.heaven7.java.message.protocol.anno;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMember {

    /** the priority of this field. in message
     * @return the priority */
    int value() default 0;
}
