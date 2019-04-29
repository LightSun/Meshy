package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.TypeAdapters;

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
