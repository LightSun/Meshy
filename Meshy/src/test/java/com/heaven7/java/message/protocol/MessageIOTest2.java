package com.heaven7.java.message.protocol;

import com.heaven7.java.message.protocol.entity.TestEntity2;

/**
 * base test for simple entity
 * @author heaven7
 */
public class MessageIOTest2 extends MessageIOTest{

    private Object expectEntity;

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

        setEntity2(mEntity);
        this.mEntity = mEntity;
        //only arg1 and arg2 allow inherit. so entity is not the same
        TestEntity2 mEntity2 = new TestEntity2();
        mEntity2.setArg1((byte) 1); // 1
        mEntity2.setArg2((short) 2); // 4 (saved as int)
        setEntity2(mEntity2);
        expectEntity = mEntity2;
    }

    private void setEntity2(TestEntity2 mEntity) {
        mEntity.setArgt1((byte) 11);
        mEntity.setArgt2((short) 12);
        mEntity.setArgt3(13);
        mEntity.setArgt4(14L);
        mEntity.setArgt5(16.6f);
        mEntity.setArgt6(17.7d);
        mEntity.setArgt7(true);
        mEntity.setArgt8(Character.MAX_SURROGATE);
    }

    @Override
    public Object getEqualsEntity() {
        return expectEntity;
    }

}
