package com.heaven7.java.message.protocal.secure;

import com.heaven7.java.message.protocal.util.RSAKeyUtil;
import org.junit.Test;

import java.security.KeyPair;
import java.util.Base64;

public class RSAMessageSecureTest {

    @Test
    public void test1(){
        byte[] data = {1,2,3};
        try {
            KeyPair keyPair = RSAKeyUtil.generateKey();
            RSAMessageSecure secure = new RSAMessageSecure(keyPair.getPublic(), keyPair.getPrivate());
            byte[] bytes = secure.encode(data);
            byte[] result = secure.decode(bytes);
            assertEquals(data, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        byte[] data = {1,2,3};
        try {
            KeyPair keyPair = RSAKeyUtil.generateKey();
            RSAMessageSecure secure = new RSAMessageSecure(keyPair.getPublic(), keyPair.getPrivate(),
                    RSAMessageSecure.MODE_PUBLIC_DE_PRIVATE_EN);
            byte[] bytes = secure.encode(data);
            byte[] result = secure.decode(bytes);
            assertEquals(data, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        byte[] data = {1,2,3,6,6,89,78,112,123};
        try {
            KeyPair keyPair = RSAKeyUtil.generateKey();
            String publicStr = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateStr = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            RSAMessageSecure secure = new RSAMessageSecure(
                    RSAKeyUtil.getPubKey(publicStr),
                    RSAKeyUtil.getPrivateKey(privateStr)
            );
            byte[] bytes = secure.encode(data);
            byte[] result = secure.decode(bytes);
            assertEquals(data, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test4(){
        byte[] data = {1,2,3,6,6,89,78,112,123};
        try {
            KeyPair keyPair = RSAKeyUtil.generateKey();
            String publicStr = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateStr = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            RSAMessageSecure secure = new RSAMessageSecure(
                    RSAKeyUtil.getPubKey(publicStr),
                    RSAKeyUtil.getPrivateKey(privateStr),
                    RSAMessageSecure.MODE_PUBLIC_DE_PRIVATE_EN);
            byte[] bytes = secure.encode(data);
            byte[] result = secure.decode(bytes);
            assertEquals(data, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
}
