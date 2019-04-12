package com.heaven7.java.message.protocol;

import com.heaven7.java.message.protocol.entity.Person;
import com.heaven7.java.message.protocol.entity.Person1;
import com.heaven7.java.message.protocol.policy.DefaultRSASegmentationPolicy;
import com.heaven7.java.message.protocol.secure.MessageSecureFactory;
import com.heaven7.java.message.protocol.secure.RSAMessageSecure;
import com.heaven7.java.message.protocol.secure.SingleRSAMessageSecure;
import com.heaven7.java.message.protocol.secure.UnsafeMessageSecure;
import com.heaven7.java.message.protocol.signature.HMAC_SHA1Signature;
import com.heaven7.java.message.protocol.util.RSACoder;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author heaven7
 */
public class OkMessageTest {

    public static final float VERSION = 1.0f;
    public static final int TYPE_RSA_PRIVATE = 1;
    public static final int TYPE_RSA_PUBLIC  = 2;
    public static final int TYPE_RSA         = 3;
    public static final int TYPE_NONE        = 0;

    private String priKey;
    private String pubKey;

    public OkMessageTest() {
        try {
            KeyPair keyPair = RSACoder.initKeys();
            priKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            pubKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            initConfig();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void initConfig() throws Exception{
        MessageConfig config = MessageConfig.newConfig();
        config.version = VERSION;
        config.signKey = "Google/heaven7";
        config.signature = new HMAC_SHA1Signature();
        //rsa-512 means max 53 . 1024 means max 117
        config.segmentationPolicy = new DefaultRSASegmentationPolicy(53);
        config.secures.put(TYPE_RSA_PRIVATE, MessageSecureFactory.createMessageSecure(
                SingleRSAMessageSecure.class.getName(), priKey, String.valueOf(true)));
        config.secures.put(TYPE_RSA_PUBLIC, MessageSecureFactory.createMessageSecure(
                SingleRSAMessageSecure.class.getName(), pubKey, String.valueOf(false)));
        config.secures.put(TYPE_RSA, MessageSecureFactory.createMessageSecure(
                RSAMessageSecure.class.getName(), pubKey, priKey));
        config.secures.put(TYPE_NONE, new UnsafeMessageSecure());

        List<MessageConfig.Pair<Class<?>, Float>> list = new ArrayList<>();
        list.add(new MessageConfig.Pair<>(Person1.class, 1f));
        list.add(new MessageConfig.Pair<>(Person.class, 2f));
        config.compatMap.put(Person.class.getName(), list);
        MessageConfigManager.initialize(config);
    }

    @Test
    public void testNoCompat(){
        String msg = "testNoCompat";
        Person person = new Person();
        person.setAge(18);
        person.setName("Google");
        person.setAuchor("heaven7");
        Message<Person> mess = Message.create(Message.COMMON, msg, person);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int evaluateSize = OkMessage.evaluateMessageSize(mess, TYPE_NONE);
        int writeSize = OkMessage.writeMessage(Okio.buffer(Okio.sink(baos)),mess, TYPE_NONE);
        Assert.assertTrue(evaluateSize == writeSize);

        Message<Object> mess2 = OkMessage.readMessage(Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray()))));
        Assert.assertTrue(mess.getType() == mess2.getType());
        Assert.assertTrue(mess.getMsg().equals(mess2.getMsg()));
        Assert.assertTrue(mess.getEntity().equals(mess2.getEntity()));
    }
}
