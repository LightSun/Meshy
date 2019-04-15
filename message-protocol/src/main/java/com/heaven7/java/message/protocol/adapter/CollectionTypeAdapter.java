package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.TypeAdapterContext;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author heaven7
 */
public class CollectionTypeAdapter extends TypeAdapter {

    private final TypeAdapterContext context;
    private final TypeAdapter componentAdapter;

    public CollectionTypeAdapter(TypeAdapterContext context, TypeAdapter componentAdapter) {
        this.context = context;
        this.componentAdapter = componentAdapter;
    }

    @Override
    public int write(BufferedSink sink, Object coll) throws IOException{
        if(coll == null){
            sink.writeInt(-1);
            return 4;
        }
        int total = 0;
        int size = ((Collection)coll).size();
        sink.writeInt(size);
        total += 4;

        String name = coll.getClass().getName();
        sink.writeInt(name.length());
        sink.writeUtf8(name);
        total += 4 + name.length();

        if(size > 0){
            if(coll instanceof List){
                List list = (List) coll;
                for(int i = 0 ; i < size ; i ++){
                    total += componentAdapter.write(sink, list.get(i));
                }
            }else {
                for (Object child: ((Collection) coll)){
                    total += componentAdapter.write(sink, child);
                }
            }
        }
        return total;
    }

    @Override
    public Object read(BufferedSource source) throws IOException{
        int size = source.readInt();
        if (size == -1) {
            return null;
        }
        String name = source.readUtf8(source.readInt());
        Collection coll = context.createCollection(name);

        for (int i = 0 ; i < size ; i ++){
            coll.add(componentAdapter.read(source));
        }
        return coll;
    }

    @Override
    public int evaluateSize(Object coll){
        if(coll == null){
            return 4;
        }
        int total = 0;
        int size = ((Collection)coll).size();
        total += 4;

        String name = coll.getClass().getName();
        total += 4 + name.length();

        if(size > 0){
            if(coll instanceof List){
                List list = (List) coll;
                for(int i = 0 ; i < size ; i ++){
                    total += componentAdapter.evaluateSize(list.get(i));
                }
            }else {
                for (Object child: ((Collection) coll)){
                    total += componentAdapter.evaluateSize(child);
                }
            }
        }
        return total;
    }

}
