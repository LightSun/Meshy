package com.heaven7.java.message.protocal.secure;

import com.heaven7.java.message.protocal.utils.RSAKeyUtil;
import org.junit.Test;

import java.security.KeyPair;
import java.util.Base64;

public class RSAMessageSecureTest {

    @Test
    public void test1(){
        byte[] data = {1,2,3};
        try {
            KeyPair keyPair = RSAKeyUtil.generateKey();
            RSAMessageSecure secure = new RSAMessageSecure(keyPair.getPrivate(), keyPair.getPublic());
            byte[] bytes = secure.encode(data);
            byte[] result = secure.decode(bytes);
            assertEquals(data, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        byte[] data = {1,2,3,6,6,89,78,112,123};
        try {
            KeyPair keyPair = RSAKeyUtil.generateKey();
            String privateStr = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            String publicStr = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

            RSAMessageSecure secure = new RSAMessageSecure(
                    RSAKeyUtil.getPrivateKey(privateStr),
                    RSAKeyUtil.getPubKey(publicStr));
            byte[] bytes = secure.encode(data);
            byte[] result = secure.decode(bytes);
            assertEquals(data, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void assertEquals(byte[] data, byte[] result) {
        if(data.length != result.length){
            throw new RuntimeException();
        }
        for(int i= 0 , size = data.length ; i < size ; i ++){
           if(data[i] != result[i]){
               throw new RuntimeException();
           }
        }
    }
}
