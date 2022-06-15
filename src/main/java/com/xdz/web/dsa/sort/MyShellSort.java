package com.xdz.web.dsa.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Description: shell-sort<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 15:20<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * shell-sort is the improve-version of insertion-sort: if we set sortByGap's param gap to 1,
 * then it's insertion-sort.
 *
 * so where is the improve?
 * for insertion-sort, array-copy will occur for move array data. and we note that there maybe a
 * part of data will move each sub-sort(assume that input-array is reverse order).
 *
 * todo
 *
 * </pre>
 */
public class MyShellSort<E extends Comparable<E>> extends MyAbstractArraySort<E> {

    @Override
    protected void doSort(E[] array, int start, int end, Comparator<E> cmp) {
        // 5 3 1 / 8 4 2 1
        int len = end - start;
        for (int gap = len / 2; gap >= 1; gap /= 2) {
            // insert-sort for sub-array: if gap == 1 then it's insertion-sort.
            sortByGap(array, start, end, cmp, gap);
        }
    }

    private void sortByGap(E[] array, int start, int end, Comparator<E> cmp, int gap) {
        for (int i = start + gap; i < end; i++) {
            E tmp = array[i];
            int j = i - gap;
            // j < i && array[j] > array[i], order is not meet
            for (; j >= start && cmp.compare(array[j], tmp) > 0; j -= gap) {
                array[j + gap] = array[j];
            }
            // now j < 0 || (array[j] < array[i] && j < i)
            array[j + gap] = tmp;
        }
    }

    public static void main(String[] args) {
        IMyArraySort<Integer> sort = new MyShellSort<>();
        MyAbstractArraySort.test(sort);
        MyAbstractArraySort.testRange(sort);
    }
}
