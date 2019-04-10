package com.heaven7.java.message.protocal.secure;

import com.heaven7.java.message.protocal.util.RSAKeyUtil;
import org.junit.Test;

import java.security.KeyPair;

/**
 * @author heaven7
 */
public class SingleRSAMessageSecureTest {

    @Test
    public void test1(){
        byte[] data = {1,2,3,6,6,89,78,112,123};
        try {
            KeyPair keyPair = RSAKeyUtil.generateKey();
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
}
