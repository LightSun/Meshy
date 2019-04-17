package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.*;
import com.heaven7.java.message.protocol.entity.TestEntity;

public class CommonObjectTypeAdapterTest extends BaseAdapterTest<TestEntity> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        try {
            MessageConfigManagerTest.initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TypeAdapters.getTypeAdapter(new TestEntity(), MessageConfigManager.getVersion());
    }

    @Override
    public void testReadAndWrite() throws Exception {
        testReadAndWrite(createTestEntities()[0]);
    }
}
