package com.heaven7.java.message.protocol.adapter;


import com.heaven7.java.base.util.SparseArrayDelegate;
import com.heaven7.java.base.util.SparseFactory;
import com.heaven7.java.message.protocol.MessageConfigManager;
import com.heaven7.java.message.protocol.MessageConfigManagerTest;
import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.entity.TestEntity;
import com.heaven7.java.message.protocol.internal.SimpleMessageProtocolContext;

public class MapTypeAdapterTest2 extends BaseAdapterTest<SparseArrayDelegate<TestEntity>> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        try {
            MessageConfigManagerTest.initConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectTypeAdapter componentTypeAdapter = new ObjectTypeAdapter(
                MessageConfigManager.getTypeAdapterContext(),
                MessageConfigManager.getVersion());
        return new MapTypeAdapter(MessageConfigManager.getTypeAdapterContext(), new IntPackedAdapter(), componentTypeAdapter);
    }

    @Override
    protected boolean equals(SparseArrayDelegate<TestEntity> src, SparseArrayDelegate<TestEntity> readVal) {
        if(src.size() != readVal.size()){
            return false;
        }
        int size = src.size();
        for (int i = 0 ; i < size ; i ++){
            if( src.keyAt(i) != readVal.keyAt(i)){
                return false;
            }
            if(!src.valueAt(i).equals(readVal.valueAt(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public void testReadAndWrite() throws Exception {
        SparseArrayDelegate<TestEntity> map = SparseFactory.newSparseArray(10);
        TestEntity[] entities = createTestEntities();
        for (int i = 0 ; i < entities.length ; i ++){
            map.put(i, entities[i]);
        }
        super.testReadAndWrite(map);
    }
}
