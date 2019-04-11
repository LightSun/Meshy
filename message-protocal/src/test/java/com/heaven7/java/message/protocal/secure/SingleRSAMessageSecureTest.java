package com.heaven7.java.message.protocal.secure;

import com.heaven7.java.message.protocal.util.RSACoder;
import com.heaven7.java.message.protocal.util.SecureUtils;
import org.junit.Test;

import java.security.KeyPair;
import java.util.Base64;

/**
 * test with SecureUtils
 * @author heaven7
 */
public class SingleRSAMessageSecureTest {

    @Test
    public void test1(){
        byte[] data = {1,2,3,6,6,89,78,112,123};
        try {
            KeyPair keyPair = RSACoder.initKeys();
            SingleRSAMessageSecure privateSecure = new SingleRSAMessageSecure(keyPair.getPrivate().getEncoded(), true);
            SingleRSAMessageSecure publicSecure = new SingleRSAMessageSecure(keyPair.getPublic().getEncoded(), false);

            byte[] bytes = privateSecure.encode(data);
            byte[] result = publicSecure.decode(bytes);
            RSAMessageSecureTest.assertEquals(data, result);

            bytes = publicSecure.encode(data);
            result = privateSecure.decode(bytes);
            RSAMessageSecureTest.assertEquals(data, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        byte[] data = {1,2,3,6,6,89,78,112,123};
        try {
            KeyPair keyPair = RSACoder.initKeys();
            String publicStr = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateStr = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            SingleRSAMessageSecure privateSecure =
                    new SingleRSAMessageSecure(SecureUtils.getPrivateKey(privateStr).getEncoded(), true);
            SingleRSAMessageSecure publicSecure = new SingleRSAMessageSecure(
                    SecureUtils.getPublicKey(publicStr).getEncoded(), false);

            byte[] bytes = privateSecure.encode(data);
            byte[] result = publicSecure.decode(bytes);
            RSAMessageSecureTest.assertEquals(data, result);

            bytes = publicSecure.encode(data);
            result = privateSecure.decode(bytes);
            RSAMessageSecureTest.assertEquals(data, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
