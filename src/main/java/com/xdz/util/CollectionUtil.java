package com.xdz.util;

/**
 * Description: collection utils<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 23:53<br/>
 * Version: 1.0<br/>
 */
public class CollectionUtil {

    // do not check i, j, array
    public static <E> void swap(E[] array, int i, int j) {
        if (i == j) {
            return;
        }
        E tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
}
