package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.*;
import com.heaven7.java.message.protocol.entity.TestEntity;

public class CommonObjectTypeAdapterTest extends BaseAdapterTest<TestEntity> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return TypeAdapters.getTypeAdapter(new TestEntity(), getMeshy());
    }

    @Override
    public void testReadAndWrite() throws Exception {
        testReadAndWrite(createTestEntities()[0]);
    }
}
