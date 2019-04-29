package com.heaven7.java.message.protocol.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author heaven7
 */
public class ArrayUtilsTest {

    @Test
    public void test1(){
        byte[] arr = {1,2,3,4,5};
        List<byte[]> list = ArrayUtils.splitArray(arr, 3);

        Assert.assertTrue(list.size() == 2);
        Assert.assertTrue(list.get(0).length == 3);
        Assert.assertTrue(list.get(1).length == 2);

        Assert.assertTrue(list.get(0)[0] == 1);
        Assert.assertTrue(list.get(0)[1] == 2);
        Assert.assertTrue(list.get(0)[2] == 3);
        Assert.assertTrue(list.get(1)[0] == 4);
        Assert.assertTrue(list.get(1)[1] == 5);
    }
}
