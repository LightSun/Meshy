package com.heaven7.java.message.protocol;

import com.heaven7.java.message.protocol.entity.Person;
import com.heaven7.java.message.protocol.entity.Person2;
import com.heaven7.java.message.protocol.policy.DefaultRSASegmentationPolicy;
import com.heaven7.java.message.protocol.secure.MessageSecureFactory;
import com.heaven7.java.message.protocol.secure.RSAMessageSecure;
import com.heaven7.java.message.protocol.secure.SingleRSAMessageSecure;
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
public class MessageConfigManagerTest {

    public static final float LOW_VERSION = 1.0f;
    public static final float HIGH_VERSION = 2.0f;
    public static final int TYPE_RSA_PRIVATE = 1;
    public static final int TYPE_RSA_PUBLIC  = 2;
    public static final int TYPE_RSA         = 3;

    private static String priKey;
    private static String pubKey;

    public MessageConfigManagerTest() {
        try {
            initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void initConfig() throws Exception{
        initConfig(LOW_VERSION);
    }
    public static void initConfig(float version) throws Exception{
        try {
            KeyPair keyPair = RSACoder.initKeys();
            priKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            pubKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        MessageConfig config = MessageConfig.newConfig();
        config.version = version;
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

        List<MessageConfig.Pair<Class<?>, Float>> list = new ArrayList<>();
        list.add(new MessageConfig.Pair<>(Person.class, LOW_VERSION));
        list.add(new MessageConfig.Pair<>(Person2.class, HIGH_VERSION));
        config.compatMap.put(Person.class.getName(), list);
        MessageConfigManager.initialize(config);
    }

    @Test
    public void test1() throws Exception{
        byte[] arr = new byte[1288];
        arr[0] = 1;
        arr[1] = 1;
        arr[2] = 2;
        arr[3] = 3;
        Assert.assertTrue(MessageConfigManager.getVersion() == LOW_VERSION);
        MessageSecureWrapper privateSe = MessageConfigManager.getMessageSecure(TYPE_RSA_PRIVATE);
        MessageSecureWrapper publicSe = MessageConfigManager.getMessageSecure(TYPE_RSA_PUBLIC);
        //private encode and public decode
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        privateSe.encodeWithFlush(Okio.buffer(Okio.sink(baos)), arr);
        byte[] result = publicSe.decode(Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray()))));
        Assert.assertTrue(result.length == arr.length);
        assertEquals(arr, result);

        //public encode and private decode
        baos = new ByteArrayOutputStream();
        publicSe.encodeWithFlush(Okio.buffer(Okio.sink(baos)), arr);
        result = privateSe.decode(Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray()))));
        Assert.assertTrue(result.length == arr.length);
        assertEquals(arr, result);
    }

    @Test
    public void test2() throws Exception{
        Class<?> clazz = MessageConfigManager.getCompatClass(Person.class.getName(), 1f);
        Assert.assertTrue(clazz == Person.class);
        clazz = MessageConfigManager.getCompatClass(Person.class.getName(), 2f);
        Assert.assertTrue(clazz == Person2.class);
        try{
            clazz = MessageConfigManager.getCompatClass(Person2.class.getName(), 3f);
        }catch (MessageConfigManager.ConfigException e){
            //expect
        }
    }

    public static void assertEquals(byte[] data, byte[] result) {
        if(data.length != result.length){
            throw new RuntimeException();
        }
        for(int i= 0 , size = data.length ; i < size ; i ++){
            if(data[i] != result[i]){
                throw new RuntimeException();
            }
        }
    }

    public static float getLowVersion() {
        return LOW_VERSION;
    }
    public static float getHigherVersion() {
        return HIGH_VERSION;
    }
}
