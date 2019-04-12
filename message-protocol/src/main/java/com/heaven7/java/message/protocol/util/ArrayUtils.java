package com.heaven7.java.message.protocol.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public class ArrayUtils {

    public static List<byte[]> splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int count = x + (y != 0 ? 1 : 0);

        List<byte[]> list = new ArrayList<>();
        byte[] arr;
        for (int i = 0; i < count; i++) {
            if (i == count - 1 && y != 0) {
                arr = new byte[y];
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                arr = new byte[len];
                System.arraycopy(data, i * len, arr, 0, len);
            }
            list.add(arr);
        }
        return list;
    }
}
