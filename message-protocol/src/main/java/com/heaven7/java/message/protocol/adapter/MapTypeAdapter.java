package com.heaven7.java.message.protocol.adapter;

import com.heaven7.java.message.protocol.TypeAdapter;
import com.heaven7.java.message.protocol.TypeAdapterContext;
import okio.BufferedSink;
import okio.BufferedSource;

import java.io.IOException;
import java.util.Map;

/**
 * @author heaven7
 */
public class MapTypeAdapter extends TypeAdapter {

    private final TypeAdapterContext context;
    private final TypeAdapter keyAdapter;
    private final TypeAdapter valueAdapter;

    public MapTypeAdapter(TypeAdapterContext context, TypeAdapter keyAdapter, TypeAdapter valueAdapter) {
        this.context = context;
        this.keyAdapter = keyAdapter;
        this.valueAdapter = valueAdapter;
    }

    @Override
    public int write(BufferedSink sink, Object obj) throws IOException {
        if(obj == null){
            sink.writeInt(-1);
            return 4;
        }
        int total = 0;
        //size
        Map map = context.getMap(obj);
        int size = map.size();
        sink.writeInt(size);
        total += 4;
        //class name
        String name = obj.getClass().getName();
        sink.writeInt(name.length());
        sink.writeUtf8(name);
        total += 4 + name.length();

        for (Object oen : map.entrySet()){
            Map.Entry en = (Map.Entry) oen;
            total += keyAdapter.write(sink, en.getKey());
            total += valueAdapter.write(sink, en.getValue());
        }
        return total;
    }

    @Override
    public Object read(BufferedSource source) throws IOException {
        int size = source.readInt();
        if(size == -1){
            return null;
        }
        String name = source.readUtf8(source.readInt());
        Map map = context.createMap(name);
        for (int i = 0 ; i < size ; i ++){
            Object key = keyAdapter.read(source);
            Object value = valueAdapter.read(source);
            map.put(key, value);
        }
        return map;
    }

    @Override
    public int evaluateSize(Object obj){
        if(obj == null){
            return 4;
        }
        int total = 0;
        Map map = (Map) obj;
        total += 4;

        String name = obj.getClass().getName();
        total += 4 + name.length();
        for (Object oen : map.entrySet()){
            Map.Entry en = (Map.Entry) oen;
            total += keyAdapter.evaluateSize(en.getKey());
            total += valueAdapter.evaluateSize(en.getValue());
        }
        return total;
    }
}
