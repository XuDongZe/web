package com.xdz.web.dsa.sort;

import com.xdz.web.dsa.heap.MyHeap;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Description: heap sort<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 16:08<br/>
 * Version: 1.0<br/>
 */
public class MyHeapSort<E extends Comparable<E>> extends MyAbstractArraySort<E> {

    @Override
    protected void doSort(E[] array, int start, int end, Comparator<E> cmp) {
        MyHeap.sort(array, start, end, cmp);
    }

    public static void main(String[] args) {
        IMyArraySort<Integer> sort = new MyHeapSort<>();
        MyAbstractArraySort.test(sort);
        MyAbstractArraySort.testRange(sort);
    }
}
