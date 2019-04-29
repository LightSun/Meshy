package com.heaven7.java.message.protocol.signature;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author heaven7
 */
public class MD5SaltSignatureTest {

    private static final String KEY = "sd23842904902";

    @Test
    public void test1() {
        String data = "Hello Google";
        MD5SaltSignature signature = new MD5SaltSignature();
        String sign = signature.signature(data.getBytes(StandardCharsets.UTF_8), KEY);
        Assert.assertEquals(sign.length(), 32);
    }
}
