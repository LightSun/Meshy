package com.heaven7.java.message.protocol;

/**
 * @author heaven7
 */
public class MessageException extends RuntimeException {

    public MessageException() {
    }
    public MessageException(String message) {
        super(message);
    }
    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
    public MessageException(Throwable cause) {
        super(cause);
    }
}
