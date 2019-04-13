package com.heaven7.java.message.protocol.anno;

import java.lang.annotation.*;


/**
 * make a class compat with target class. this often used to write object class name for {@linkplain com.heaven7.java.message.protocol.MessageIO}.
 * and also often used to compat high version to lower version.
 * @author heaven7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CompatKeyClass {

    /**
     * make this class compat with target class.
     * that means when write object to message. the object class name is represent by target class.
     * @return the class.
     */
   Class<?> value();
}