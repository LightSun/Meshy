package com.heaven7.java.meshy.adapter;

import com.heaven7.java.meshy.TestContext;
import com.heaven7.java.meshy.TypeAdapter;
import com.heaven7.java.meshy.entity.TestEntity;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public abstract class BaseAdapterTest<T> extends TestContext{

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final TypeAdapter mAdapter;
    private final T mValue;

    public BaseAdapterTest(){
        mAdapter = onCreateTypeAdapter();
        this.mValue = null;
    }
    public BaseAdapterTest(TypeAdapter mAdapter) {
        this.mAdapter = mAdapter;
        this.mValue = null;
    }
    public BaseAdapterTest(TypeAdapter mAdapter, T value) {
        this.mAdapter = mAdapter;
        this.mValue = value;
    }

    public ObjectTypeAdapter createObjectTypeAdapter(){
        return new ObjectTypeAdapter(getMeshy(), getMeshy().getVersion());
    }

    protected TypeAdapter onCreateTypeAdapter(){
        return null;
    }

    public BufferedSink ofSink(){
        baos.reset();
        return Okio.buffer(Okio.sink(baos));
    }

    public BufferedSource ofSinkAsSource(){
        if(baos.size() == 0){
            throw new RuntimeException();
        }
        return Okio.buffer(Okio.source(new ByteArrayInputStream(baos.toByteArray())));
    }

    public static TestEntity[] createTestEntities(){
        TestEntity[] array = new TestEntity[3];
        for (int i = 0 ; i < 3 ; i ++){
            TestEntity mEntity = new TestEntity();
            mEntity.setArg1((byte) i);
            mEntity.setArg2((short) 2);
            mEntity.setArg3( i * 100);
            mEntity.setArg4(4);
            mEntity.setArg5(true);
            mEntity.setArg6(2.5f);
            mEntity.setArg7(3.843884584353);
            mEntity.setArg8("fdjgjfgjfdgjfdgf");
            array[i] = mEntity;
        }
        return array;
    }

    protected void testReadAndWrite(T value) throws Exception{
        BufferedSink sink = ofSink();
        int writeSize = mAdapter.write(sink, value);
        sink.flush();
        Assert.assertTrue(writeSize == mAdapter.evaluateSize(value));
        T readValue = (T) mAdapter.read(ofSinkAsSource());
        Assert.assertTrue( value != null ? equals(value, readValue) : readValue == null);
    }
    protected boolean equals(T src, T readVal){
        return src.equals(readVal);
    }

    @Test
    public void testReadAndWrite() throws Exception{
        testReadAndWrite(mValue);
    }
}
