package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.*;
import com.heaven7.java.message.protocol.entity.TestEntity;

public class CommonNullTypeAdapterTest extends BaseAdapterTest<Void> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return TypeAdapters.getTypeAdapter(null, getMeshy(), getMeshy().getVersion());
    }

    @Override
    public void testReadAndWrite() throws Exception {
        testReadAndWrite(null);
    }
}
