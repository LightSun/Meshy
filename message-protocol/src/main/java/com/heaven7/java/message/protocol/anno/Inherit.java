package com.heaven7.java.message.protocol.anno;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Inherit {

    /**
     * if {@code true}. indicate the member/members allow inherit to sub class.
     * @return true if allow inherit. false otherwise. default is true.
     */
   boolean value() default true;
}