package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.TypeAdapters;
import com.heaven7.java.message.protocol.*;
import com.heaven7.java.meshy.entity.TestEntity;
import com.heaven7.java.meshy.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

public class CommonCollectionTypeAdapterTest extends BaseAdapterTest<List<TestEntity>> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return TypeAdapters.ofTypeToken(new TypeToken<List<TestEntity>>(){}, getMeshy());
    }

    @Override
    protected boolean equals(List<TestEntity> src, List<TestEntity> readVal) {
        if(src.size() != readVal.size()){
            return false;
        }
        int length = src.size();
        for (int i = 0 ; i < length ; i ++){
            if(!src.get(i).equals(readVal.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public void testReadAndWrite() throws Exception {
        testReadAndWrite(Arrays.asList(createTestEntities()));
    }
}
