package com.heaven7.java.meshy.signature;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author heaven7
 */
public class HMAC_SHA1SignatureTest {

    private static final String KEY = "~!@#$%^&*()";
    private final HMAC_SHA1Signature mSign = new HMAC_SHA1Signature();

    @Test
    public void test1(){
        String data = "Hello Google";
        String signature = mSign.signature(data.getBytes(StandardCharsets.UTF_8), KEY);
        System.out.println(signature);
        Assert.assertEquals(28, signature.length());
    }
}
