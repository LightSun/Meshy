package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.MessageConfigManager;
import com.heaven7.java.message.protocol.MessageConfigManagerTest;
import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.entity.TestEntity;

/**
 * object type adapter use the {@linkplain com.heaven7.java.message.protocol.MessageConfigManager} so need init
 */
public class ObjectTypeAdapterTest extends BaseAdapterTest<TestEntity>{

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        try {
            MessageConfigManagerTest.initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ObjectTypeAdapter(
                MessageConfigManager.getTypeAdapterContext(),
                MessageConfigManager.getVersion());
    }

    @Override
    public void testReadAndWrite() throws Exception {
        TestEntity mEntity = new TestEntity();
        mEntity.setArg1((byte) 1); // 1
        mEntity.setArg2((short) 2); // 4 (saved as int)
        mEntity.setArg3(3);   //4
        mEntity.setArg4(4);   //8
        mEntity.setArg5(true); // 1
        mEntity.setArg6(2.5f);
        mEntity.setArg7(3.843884584353);
        mEntity.setArg8("fdjgjfgjfdgjfdgf");

        testReadAndWrite(mEntity);
    }
}
