package com.xdz.dsa.sort;

import com.xdz.util.CollectionUtil;

import java.util.Comparator;

/**
 * Description: quick-sort<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/13 13:31<br/>
 * Version: 1.0<br/>
 * <pre>
 *
 * </pre>
 */
public class MyQuickSort<E extends Comparable<E>> extends MyAbstractArraySort<E> {

    private static final int CUTOFF = 4;

    @Override
    protected void doSort(E[] array, int start, int end, Comparator<E> cmp) {
        quickSort(array, start, end - 1, cmp);
    }

    /**
     * quick-sort array[start, end], by nature-order with cmp.
     */
    private void quickSort(E[] array, int start, int end, Comparator<E> cmp) {
        if (end - start + 1 >= CUTOFF) {
            E pivot = median3(array, start, end, cmp);
            int i = start, j = end - 1;
            for ( ; ; ) {
                // we do not check i & j. for i , j sentinel
                while (cmp.compare(array[++i], pivot) < 0) {}
                while (cmp.compare(array[--j], pivot) > 0) {}
                if (i < j) {
                    CollectionUtil.swap(array, i, j);
                } else {
                    break;
                }
            }
            // restore pivot to right position
            CollectionUtil.swap(array, i, end - 1);
            quickSort(array, start, i - 1, cmp);
            quickSort(array, i + 1, end, cmp);
        } else {
            ((IMyArraySort<E>)MyInsertionSort.INSTANCE).sort(array, start, end + 1, cmp);
        }
    }

    /**
     * todo analyze this
     * median array[start, end, mid], mid = (start + end) / 2
     * order these and place pivot to array[end - 1] as sentinel, place smaller than pivot one to array[left] as sentinel too.
     */
    private static <E extends Comparable<E>> E median3(E[] array, int start, int end, Comparator<E> cmp) {
        int mid = (start + end) / 2;
        if (cmp.compare(array[start], array[mid]) > 0) {
            CollectionUtil.swap(array, start, mid);
        }
        if (cmp.compare(array[start], array[end]) > 0) {
            CollectionUtil.swap(array, start, end);
        }
        if (cmp.compare(array[mid], array[end]) > 0) {
            CollectionUtil.swap(array, mid, end);
        }
        // now array[start] <= array[mid] <= array[end]
        E median = array[mid];
        // place pivot to array[end - 1]
        CollectionUtil.swap(array, mid, end - 1);
        return median;
    }

    public static void main(String[] args){
        IMyArraySort<Integer> sort = new MyQuickSort<>();
        test(sort);
        testRange(sort);

    }
}
