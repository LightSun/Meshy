package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.entity.TestComplexEntity;

import java.util.*;

/**
 * @author heaven7
 */
public class ComplexEntityTest extends BaseAdapterTest<TestComplexEntity> {

    @Override
    protected TypeAdapter onCreateTypeAdapter() {
        return createObjectTypeAdapter();
    }

    @Override
    public void testReadAndWrite() throws Exception {
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);

        List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(5);
        list.add(6);

        List<Integer> list2 = new ArrayList<>();
        list2.add(14);
        list2.add(15);
        list2.add(16);

        List<Integer> list3 = new ArrayList<>();
        list3.add(14);
        list3.add(15);
        list3.add(16);

        Map<Integer, List<Integer>> map = new HashMap<>();
        map.put(1, list);
        map.put(2, list2);
        map.put(3, list3);

        //build entity
        TestComplexEntity tce = new TestComplexEntity();
        tce.setSet(set);
        tce.setList(list);
        tce.setMap(map);

        testReadAndWrite(tce);
    }
}
