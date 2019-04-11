package com.heaven7.java.message.protocol.anno;

import java.lang.annotation.*;

/**
 * @author heaven7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Until {

    /**
     * the value indicating a version number until this member
     * or type has been present.
     */
    float value() default 1.0f;
}
