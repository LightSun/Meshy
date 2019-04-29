package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.entity.TestEntity;

public class ArrayTypeAdapterTest extends BaseAdapterTest<TestEntity[]> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return new ArrayTypeAdapter(TestEntity.class, createObjectTypeAdapter());
    }

    @Override
    public void testReadAndWrite() throws Exception {
        testReadAndWrite(createTestEntities());
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
}
