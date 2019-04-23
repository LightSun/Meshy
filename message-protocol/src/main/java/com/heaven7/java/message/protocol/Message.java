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

/**
 * the message object
 * @param <T> the message entity type
 * @author heaven7
 */
public final class Message<T> {

    /**
     * indicate the message type is login
     */
    public static final int LOGIN  = 1;
    /**
     * indicate the message type is logout
     */
    public static final int LOGOUT = 2;
    /**
     * indicate the message type is tick(heartbeat)
     */
    public static final int TICK   = 3;
    /**
     * indicate the message type is common.
     */
    public static final int COMMON = 4;

    /**
     * indicate the response state of success.
     * @since 1.0.2
     */
    public static final int SUCCESS = 1;
    /**
     * indicate the response state of failed.
     * @since 1.0.2
     */
    public static final int FAILED  = 2;

    /**
     * the message type of request or response
     */
    private int type;
    private String msg;
    private T entity;

    /**
     * the state of response.
     * @since 1.0.2
     */
    private int state;

    /**
     * create a message from target arguments
     * @param type the message type
     * @param message the notice message
     * @param object the data entity object
     * @param <T> the message entity type
     * @return the message
     */
    public static <T> Message<T> create(int type, String message, T object){
        Message<T> msg = new Message<>();
        msg.setType(type);
        msg.setMsg(message);
        msg.setEntity(object);
        return msg;
    }
    /**
     * create a message from target arguments
     * @param type the message type
     * @param state the response message state of operation
     * @param message the notice message
     * @param object the data entity object
     * @param <T> the message entity type
     * @return the message
     * @since 1.0.2
     */
    public static <T> Message<T> create(int type, int state, String message, T object){
        Message<T> msg = new Message<>();
        msg.setState(state);
        msg.setType(type);
        msg.setMsg(message);
        msg.setEntity(object);
        return msg;
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
