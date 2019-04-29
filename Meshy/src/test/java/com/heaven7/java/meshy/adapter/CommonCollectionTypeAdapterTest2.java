package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.TypeAdapters;
import com.heaven7.java.meshy.entity.TestEntity;
import com.heaven7.java.meshy.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonCollectionTypeAdapterTest2 extends BaseAdapterTest<List<List<TestEntity>>> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return TypeAdapters.ofTypeToken(new TypeToken<List<List<TestEntity>>>(){}, getMeshy());
    }

    @Override
    protected boolean equals(List<List<TestEntity>> src, List<List<TestEntity>> readVal) {
        if(src.size() != readVal.size()){
            return false;
        }
        int length = src.size();
        for (int i = 0 ; i < length ; i ++){
            if(!equals0(src.get(i), readVal.get(i))){
                return false;
            }
        }
        return true;
    }

    private boolean equals0(List<TestEntity> src, List<TestEntity> readVal) {
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
        List<List<TestEntity>> list = new ArrayList<>();
        list.add(Arrays.asList(createTestEntities()));
        list.add(Arrays.asList(createTestEntities()));
        list.add(Arrays.asList(createTestEntities()));

        testReadAndWrite(list);
    }
}
