package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.TypeAdapters;
import com.heaven7.java.meshy.entity.TestEntity;
import com.heaven7.java.meshy.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommonMapTypeAdapterTest extends BaseAdapterTest<Map<Integer, TestEntity>> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return TypeAdapters.ofTypeToken(new TypeToken<Map<Integer, TestEntity>>(){}, getMeshy());
    }

    @Override
    protected boolean equals(Map<Integer, TestEntity> src, Map<Integer, TestEntity> readVal) {
        if(src.size() != readVal.size()){
            return false;
        }
        Set<Integer> keys = src.keySet();
        for (Integer key : keys){
            if(!src.get(key).equals(readVal.get(key))){
                return false;
            }
        }
        return true;
    }

    @Override
    public void testReadAndWrite() throws Exception {
        Map<Integer, TestEntity> map = new HashMap<>();
        TestEntity[] entities = createTestEntities();
        for (int i = 0 ; i < entities.length ; i ++){
            map.put(i, entities[i]);
        }
        super.testReadAndWrite(map);
    }
}
