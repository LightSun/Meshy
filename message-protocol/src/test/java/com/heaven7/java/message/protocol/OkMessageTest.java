package com.heaven7.java.message.protocol;

import com.heaven7.java.message.protocol.entity.Person;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.heaven7.java.message.protocol.MessageConfigManagerTest.TYPE_RSA;

/**
 * @author heaven7
 */
public class OkMessageTest {

    public OkMessageTest() {
    }

    @Test
    public void testNoCompat() throws Exception{
        //we want to test Person.class(start with 2.0).
        // Person1.class start with 1.0.
        // so we use version 2.0
        try {
            MessageConfigManagerTest.initConfig(2.0f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String msg = "testNoCompat";
        Person person = new Person();
        person.setAge(18);
        person.setName("Google");
        person.setAuchor("heaven7");
        Message<Person> mess = Message.create(Message.COMMON, msg, person);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = OkMessage.evaluateMessageSize(mess, TYPE_RSA);
        int writeSize = OkMessage.writeMessage(bufferedSink,mess, TYPE_RSA);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person> mess2 = OkMessage.readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess.getEntity().equals(mess2.getEntity()));
    }

    @Test // start means server use version 2.0. client use version 1.0
    public void testCompat() throws Exception{
        //we want to test Person.class(start with 2.0).
        // Person1.class start with 1.0.
        // so we use version 2.0
        try {
            MessageConfigManagerTest.initConfig(2.0f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //TODO write client data.
        //消息兼容包含2个方面： 首先服务器版本一定高于客户端版本。
        // 2, 高版本数据 发送给低版本，  序列化的对象一定要发送的是低版本的class.
        // 3, 低版本数据 发送给高版本。  高版本找到低版本的class.来执行反序列化.

        String msg = "testNoCompat";
        Person person = new Person();
        person.setAge(18);
        person.setName("Google");
        person.setAuchor("heaven7");
        Message<Person> mess = Message.create(Message.COMMON, msg, person);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = OkMessage.evaluateMessageSize(mess, TYPE_RSA);
        int writeSize = OkMessage.writeMessage(bufferedSink,mess, TYPE_RSA);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person> mess2 = OkMessage.readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess.getEntity().equals(mess2.getEntity()));
    }
}
