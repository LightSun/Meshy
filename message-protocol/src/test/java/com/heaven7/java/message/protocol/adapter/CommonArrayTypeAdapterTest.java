package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.*;
import com.heaven7.java.message.protocol.entity.TestEntity;

import java.lang.reflect.Array;

public class CommonArrayTypeAdapterTest extends BaseAdapterTest<TestEntity[]> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        try {
            MessageConfigManagerTest.initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TypeAdapters.getTypeAdapter(createTestEntities(), MessageConfigManager.getVersion());
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
