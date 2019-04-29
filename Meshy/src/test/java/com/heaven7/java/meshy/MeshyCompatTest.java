package com.heaven7.java.meshy;


import com.heaven7.java.meshy.entity.Person;
import com.heaven7.java.meshy.entity.Person2;
import com.heaven7.java.meshy.entity.Person3;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author heaven7
 */
public class MeshyCompatTest {

    private TestContext mContext;

    public void initContext(float version){
        mContext = new TestContext(version);
    }

    @Test
    public void testNoCompat() throws Exception{
        //we want to test Person.class(start with 2.0).
        // Person1.class start with 1.0.
        // so we use version 2.0
        initContext(2.0F);

        String msg = "testNoCompat";
        Person2 person = new Person2();
        person.setAge(18);
        person.setName("Google");
        person.setAuchor("heaven7");
        Message<Person2> mess = Message.create(Message.COMMON, msg, person);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = mContext.getWriter().evaluateMessageSize(mess, TestContext.TYPE_RSA);
        int writeSize = mContext.getWriter().writeMessage(bufferedSink, mess, TestContext.TYPE_RSA);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person2> mess2 = mContext.getReader().readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess.getEntity().equals(mess2.getEntity()));
    }

    /**
     * test compat 'high to low'.  'low to high' is promised by Meshy.
     * @throws Exception
     */
    @Test // start means server(local) use version 2.0. client(remote) use version 1.0
    public void testCompatHighToLow() throws Exception{
        //we want to test Person.class(start with 1.0).
        // Person2.class start with 2.0.
        // so we use version 2.0
        initContext(2.0F);

        String msg = "testCompatHighToLow";
        //sender object
        Person person = new Person();
        person.setAge(18);
        person.setName("Google");
        Message<Person> mess = Message.create(Message.COMMON, msg, person);

        //sender is lower
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //here we want to send message to client. but client's  version is lower. so we need assign version
        float applyVersion = TestContext.getLowVersion();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = mContext.getWriter().evaluateMessageSize(mess, TestContext.TYPE_RSA, applyVersion);
        int writeSize = mContext.getWriter().writeMessage(bufferedSink, mess, TestContext.TYPE_RSA, applyVersion);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        //receiver is higher
        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person> mess2 = mContext.getReader().readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess2.getEntity().getClass() == Person.class);
        Assert.assertEquals(mess2.getEntity().getName(), mess.getEntity().getName());
        Assert.assertEquals(mess2.getEntity().getAge(), mess.getEntity().getAge());
    }

    @Test //local is lower. remote is higher.
    public void testCompatLowToHigh() throws Exception{
        initContext(1.0F);

        String msg = "testCompatLowToHigh";
        Person2 person = new Person2();
        person.setAge(18);
        person.setName("Google");
        person.setAuchor("heaven7");
        Message<Person2> mess = Message.create(Message.COMMON, msg, person);

        //sender is higher.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //here we want to send message to client. but client's  version is higher. so we need assign version
        float applyVersion = TestContext.getHigherVersion();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = mContext.getWriter().evaluateMessageSize(mess, TestContext.TYPE_RSA, applyVersion);

        int writeSize = mContext.getWriter().writeMessage(bufferedSink, mess, TestContext.TYPE_RSA, applyVersion);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        //receiver is lower
        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person> mess2 = mContext.getReader().readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess2.getEntity().getClass() == Person.class);
        Assert.assertEquals(mess2.getEntity().getName(), mess.getEntity().getName());
        Assert.assertEquals(mess2.getEntity().getAge(), mess.getEntity().getAge());
    }

    @Test // test write non-extend data to lower version
    public void test3() throws Exception{
        initContext(1.0F);

        //expect Person3 auto cast to Person.
        String msg = "testCompatLowToHigh";
        Person3 person = new Person3();
        person.setName("Google");
        Message<Person3> mess = Message.create(Message.COMMON, msg, person);

        //sender is higher.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //here we want to send message to client. but client's  version is higher. so we need assign version
        float applyVersion = TestContext.getHigher2Version();
        BufferedSink bufferedSink = Okio.buffer(Okio.sink(baos));
        int evaluateSize = mContext.getWriter().evaluateMessageSize(mess, TestContext.TYPE_RSA, applyVersion);

        int writeSize = mContext.getWriter().writeMessage(bufferedSink, mess, TestContext.TYPE_RSA, applyVersion);
        bufferedSink.close();
        Assert.assertTrue(evaluateSize == writeSize);
        Assert.assertTrue(writeSize == baos.size());

        //receiver is lower
        BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
        Message<Person> mess2 = mContext.getReader().readMessage(source);
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess2.getEntity().getClass() == Person.class);
        Assert.assertEquals(mess2.getEntity().getName(), mess.getEntity().getName());
    }
}
