package com.heaven7.java.message.protocol;

/**
 * the wrapper class which wrap the type 'T'.
 * @param <T> the type to wrap
 */
public interface Wrapper<T> {

    /**
     * unwrap and get the raw object
     * @return the unwrapped object
     */
    T unwrap();

}
