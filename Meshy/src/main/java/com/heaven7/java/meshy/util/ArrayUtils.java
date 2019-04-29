/*
 * Copyright 2019
 * heaven7(donshine723@gmail.com)

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.java.meshy.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public class ArrayUtils {

    /**
     * split the target data to multi segment
     * @param data the data
     * @param len the max length of segment
     * @return the split data
     */
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
