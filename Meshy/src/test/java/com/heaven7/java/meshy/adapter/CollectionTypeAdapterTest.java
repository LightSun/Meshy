package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.entity.TestEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionTypeAdapterTest extends BaseAdapterTest<List<TestEntity>> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        ObjectTypeAdapter componentTypeAdapter = createObjectTypeAdapter();
        return new CollectionTypeAdapter(getMeshy().getTypeAdapterContext(), componentTypeAdapter);
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
        testReadAndWrite(new ArrayList<>(Arrays.asList(createTestEntities())));
    }
}
