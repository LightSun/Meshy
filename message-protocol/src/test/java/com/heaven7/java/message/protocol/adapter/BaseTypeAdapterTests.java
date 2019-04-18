package com.heaven7.java.message.protocol.adapter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * test all base-types include string.
 */
public final class BaseTypeAdapterTests {

    private final List<BaseAdapterTest<?>> mTestLists = new ArrayList<>();

    public BaseTypeAdapterTests() {
        mTestLists.add(new BaseAdapterTest<Integer>(new IntAdapter(),5){});
        mTestLists.add(new BaseAdapterTest<Short>(new ShortAdapter(),(short)5){});
        mTestLists.add(new BaseAdapterTest<Byte>(new ByteAdapter(),(byte)5){});
        mTestLists.add(new BaseAdapterTest<Long>(new LongAdapter(),5L){});
        mTestLists.add(new BaseAdapterTest<Float>(new FloatAdapter(),5.5f){});
        mTestLists.add(new BaseAdapterTest<Double>(new DoubleAdapter(),6.2){});
        mTestLists.add(new BaseAdapterTest<Character>(new CharAdapter(),(char)97){});
        mTestLists.add(new BaseAdapterTest<Boolean>(new BooleanAdapter(),true){});

        mTestLists.add(new BaseAdapterTest<Integer>(new IntPackedAdapter(),5){});
        mTestLists.add(new BaseAdapterTest<Short>(new ShortPackedAdapter(),(short)5){});
        mTestLists.add(new BaseAdapterTest<Byte>(new BytePackedAdapter(),(byte)5){});
        mTestLists.add(new BaseAdapterTest<Long>(new LongPackedAdapter(),5L){});
        mTestLists.add(new BaseAdapterTest<Float>(new FloatPackedAdapter(),5.5f){});
        mTestLists.add(new BaseAdapterTest<Double>(new DoublePackedAdapter(),6.2){});
        mTestLists.add(new BaseAdapterTest<Character>(new CharPackedAdapter(),(char)97){});
        mTestLists.add(new BaseAdapterTest<Boolean>(new BooleanPackedAdapter(),true){});

        mTestLists.add(new BaseAdapterTest<Integer>(new IntPackedAdapter(),null){});
        mTestLists.add(new BaseAdapterTest<Short>(new ShortPackedAdapter(),null){});
        mTestLists.add(new BaseAdapterTest<Byte>(new BytePackedAdapter(),null){});
        mTestLists.add(new BaseAdapterTest<Long>(new LongPackedAdapter(),null){});
        mTestLists.add(new BaseAdapterTest<Float>(new FloatPackedAdapter(),null){});
        mTestLists.add(new BaseAdapterTest<Double>(new DoublePackedAdapter(),null){});
        mTestLists.add(new BaseAdapterTest<Character>(new CharPackedAdapter(),null){});
        mTestLists.add(new BaseAdapterTest<Boolean>(new BooleanPackedAdapter(),null){});

        mTestLists.add(new BaseAdapterTest<String>(new StringAdapter(), "Google/heaven7"){});
        mTestLists.add(new BaseAdapterTest<Void>(NullTypeAdapter.INSTANCE, null){});
    }

    @Test
    public void testBaseTypes() throws Exception{
        for (BaseAdapterTest<?> test : mTestLists){
            test.testReadAndWrite();
        }
    }
}
