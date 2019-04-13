package com.heaven7.java.message.protocol;

import com.heaven7.java.message.protocol.entity.Person;
import com.heaven7.java.message.protocol.entity.Person2;
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
        Person2 person = new Person2();
        person.setAge(18);
        person.setName("Google");
        person.setAuchor("heaven7");
        Message<Person2> mess = Message.create(Message.COMMON, msg, person);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = OkMessage.evaluateMessageSize(mess, TYPE_RSA);
        int writeSize = OkMessage.writeMessage(bufferedSink,mess, TYPE_RSA);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person2> mess2 = OkMessage.readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess.getEntity().equals(mess2.getEntity()));
    }

    /**
     * test compat 'high to low'.  'low to high' is promised by {@linkplain MessageConfig#compatMap}.
     * @throws Exception
     */
    @Test // start means server(local) use version 2.0. client(remote) use version 1.0
    public void testCompatHighToLow() throws Exception{
        //we want to test Person.class(start with 1.0).
        // Person2.class start with 2.0.
        // so we use version 2.0
        try {
            MessageConfigManagerTest.initConfig(2.0f);//as server
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // for compat entity, note this
        // 1, 发送消息的俩端，和高版本的端的要兼容的class ，低版本的端必须要有
        // 2, 高版本数据 发送给低版本，  序列化的对象一定要发送的是低版本的class.
        // 3, 低版本数据 发送给高版本。  高版本找到低版本的class.来执行反序列化.

        String msg = "testCompatHighToLow";
        Person2 person = new Person2();
        person.setAge(18);
        person.setName("Google");
        person.setAuchor("heaven7");
        Message<Person2> mess = Message.create(Message.COMMON, msg, person);
        //sender is higher
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //here we want to send message to client. but client's  version is lower. so we need assign version
        float applyVersion = MessageConfigManagerTest.getLowVersion();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = OkMessage.evaluateMessageSize(mess, TYPE_RSA);
        int writeSize = OkMessage.writeMessage(bufferedSink, mess, TYPE_RSA, applyVersion);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        //receiver is lower
        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person> mess2 = OkMessage.readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess2.getEntity().getClass() == Person.class);
        Assert.assertEquals(mess2.getEntity().getName(), mess.getEntity().getName());
        Assert.assertEquals(mess2.getEntity().getAge(), mess.getEntity().getAge());
    }

    @Test //local is lower. remote is higher.
    public void testCompatLowToHigh() throws Exception{
        try {
            MessageConfigManagerTest.initConfig(1.0f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String msg = "testCompatLowToHigh";
        Person person = new Person();
        person.setAge(18);
        person.setName("Google");
        Message<Person> mess = Message.create(Message.COMMON, msg, person);

        //sender is lower.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //here we want to send message to client. but client's  version is higher. so we need assign version
        float applyVersion = MessageConfigManagerTest.getHigherVersion();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = OkMessage.evaluateMessageSize(mess, TYPE_RSA);

        int writeSize = OkMessage.writeMessage(bufferedSink, mess, TYPE_RSA, applyVersion);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        //receiver is higher
        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person> mess2 = OkMessage.readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess2.getEntity().getClass() == Person.class);
        Assert.assertEquals(mess2.getEntity().getName(), mess.getEntity().getName());
        Assert.assertEquals(mess2.getEntity().getAge(), mess.getEntity().getAge());
    }
}
