package com.example.testmvpapp.component.events;

/**
 * 定义一个事件的封装对象。在程序内部就使用该对象作为通信的信息
 */
public class MessageWrap {

    public final String message;

    public static MessageWrap getInstance(String message) {
        return new MessageWrap(message);
    }

    private MessageWrap(String message) {
        this.message = message;
    }
}
