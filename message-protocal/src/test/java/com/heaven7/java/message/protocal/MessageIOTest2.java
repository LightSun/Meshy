package com.heaven7.java.message.protocal;

import com.heaven7.java.message.protocal.entity.TestEntity2;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

/**
 * base test for simple entity
 * @author heaven7
 */
public class MessageIOTest2 extends MessageIOTest{

    protected void initEntity() {
        TestEntity2 mEntity = new TestEntity2();
        mEntity.setArg1((byte) 1); // 1
        mEntity.setArg2((short) 2); // 4 (saved as int)
        mEntity.setArg3(3);   //4
        mEntity.setArg4(4);   //8
        mEntity.setArg5(true); // 1
        mEntity.setArg6(2.5f);
        mEntity.setArg7(3.843884584353);
        mEntity.setArg8("fdjgjfgjfdgjfdgf");

        mEntity.setArgt1((byte) 11);
        mEntity.setArgt2((short) 12);
        mEntity.setArgt3(13);
        mEntity.setArgt4(14L);
        mEntity.setArgt5(16.6f);
        mEntity.setArgt6(17.7d);
        mEntity.setArgt7(true);
        mEntity.setArgt8(Character.MAX_SURROGATE);
        this.mEntity = mEntity;
    }

}
