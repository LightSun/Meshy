package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.TypeAdapters;
import com.heaven7.java.message.protocol.*;
import com.heaven7.java.meshy.entity.TestEntity;

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
