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
        try {
            MessageConfigManagerTest.initConfig();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testNoCompat() throws Exception{
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
        source.close();
        //TODO test failed. wait fix
    }
}
