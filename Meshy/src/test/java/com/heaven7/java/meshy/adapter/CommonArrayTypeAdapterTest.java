package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.TypeAdapters;
import com.heaven7.java.meshy.entity.TestEntity;
import com.heaven7.java.meshy.reflect.TypeToken;

public class CommonArrayTypeAdapterTest extends BaseAdapterTest<TestEntity[]> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return TypeAdapters.ofTypeToken(new TypeToken<TestEntity[]>(){}, getMeshy());
    }

    @Override
    protected boolean equals(TestEntity[] src, TestEntity[] readVal) {
        if(src.length != readVal.length){
            return false;
        }
        int length = src.length;
        for (int i = 0 ; i < length ; i ++){
            if(!src[i].equals(readVal[i])){
                return false;
            }
        }
        return true;
    }

    @Override
    public void testReadAndWrite() throws Exception {
        testReadAndWrite(createTestEntities());
    }
}
