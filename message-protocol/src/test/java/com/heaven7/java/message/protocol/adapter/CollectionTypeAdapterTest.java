package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.MessageConfigManager;
import com.heaven7.java.message.protocol.MessageConfigManagerTest;
import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.entity.TestEntity;
import com.heaven7.java.message.protocol.internal.SimpleMessageProtocolContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionTypeAdapterTest extends BaseAdapterTest<List<TestEntity>> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        try {
            MessageConfigManagerTest.initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectTypeAdapter componentTypeAdapter = new ObjectTypeAdapter(SimpleMessageProtocolContext.getDefault(),
                MessageConfigManager.getTypeAdapterContext(),
                MessageConfigManager.getVersion());
        return new CollectionTypeAdapter(MessageConfigManager.getTypeAdapterContext(), componentTypeAdapter);
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
