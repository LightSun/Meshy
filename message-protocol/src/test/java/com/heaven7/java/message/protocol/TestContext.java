package com.heaven7.java.message.protocol;

import com.heaven7.java.message.protocol.entity.Person;
import com.heaven7.java.message.protocol.entity.Person2;
import com.heaven7.java.message.protocol.entity.Person3;
import com.heaven7.java.message.protocol.policy.DefaultRSASegmentationPolicy;
import com.heaven7.java.message.protocol.secure.MessageSecureFactory;
import com.heaven7.java.message.protocol.secure.RSAMessageSecure;
import com.heaven7.java.message.protocol.secure.SingleRSAMessageSecure;
import com.heaven7.java.message.protocol.signature.HMAC_SHA1Signature;
import com.heaven7.java.message.protocol.util.Pair;
import com.heaven7.java.message.protocol.util.RSACoder;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author heaven7
 */
public class TestContext {

    public static final float LOW_VERSION = 1.0f;
    public static final float HIGH_VERSION = 2.0f;
    public static final float HIGH_2_VERSION = 3.0f;
    public static final int TYPE_RSA_PRIVATE = 1;
    public static final int TYPE_RSA_PUBLIC = 2;
    public static final int TYPE_RSA = 3;

    private Meshy meshy;

    public TestContext() {
        this(LOW_VERSION);
    }
    public TestContext(float version) {
        String priKey;
        String pubKey;
        try {
            KeyPair keyPair = RSACoder.initKeys();
            priKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            pubKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Pair<Class<?>, Float>> list = new ArrayList<>();
        list.add(new Pair<Class<?>, Float>(Person.class, LOW_VERSION));
        list.add(new Pair<Class<?>, Float>(Person2.class, HIGH_VERSION));
        list.add(new Pair<Class<?>, Float>(Person3.class, HIGH_2_VERSION));

        try {
            this.meshy = new MeshyBuilder()
                    .setVersion(version)
                    .setSignatureKey("Google/heaven7")
                    .setSignature(new HMAC_SHA1Signature())
                    .setSegmentationPolicy(new DefaultRSASegmentationPolicy(53))
                    .registerMessageSecure(TYPE_RSA_PRIVATE, MessageSecureFactory.createMessageSecure(
                            SingleRSAMessageSecure.class.getName(), priKey, String.valueOf(true)))
                    .registerMessageSecure(TYPE_RSA_PUBLIC, MessageSecureFactory.createMessageSecure(
                            SingleRSAMessageSecure.class.getName(), pubKey, String.valueOf(false)))
                    .registerMessageSecure(TYPE_RSA, MessageSecureFactory.createMessageSecure(
                            RSAMessageSecure.class.getName(), pubKey, priKey))
                    .registerCompatClasses(Person.class.getName(), list)
                    .setTypeAdapterContext(new BaseTypeAdapterContext())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Meshy getMeshy() {
        return meshy;
    }
    public MeshyReader getReader(){
        return meshy.getReader();
    }
    public MeshyWriter getWriter(){
        return meshy.getWriter();
    }

    public static float getLowVersion() {
        return LOW_VERSION;
    }

    public static float getHigherVersion() {
        return HIGH_VERSION;
    }

    public static float getHigher2Version() {
        return HIGH_2_VERSION;
    }
}
