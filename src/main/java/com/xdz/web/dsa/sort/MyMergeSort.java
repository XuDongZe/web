package com.xdz.web.dsa.sort;

import java.util.Comparator;

/**
 * Description: merge-sort <br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 17:16<br/>
 * Version: 1.0<br/>
 * <p>
 *
 * <pre>
 * Summary:
 * Complex:
 * best = worst = avg = O(n * log(n))
 * Stability:
 * true.
 * </pre>
 */
public class MyMergeSort<E extends Comparable<E>> extends MyAbstractArraySort<E> {

    // todo when sort-part is small enough we maybe use an inner-sort to solve rather than continue recursive
    IMyArraySort<E> innerSort = new MyInsertionSort<>();
    private static final int INNER_SORT_MAX_COUNT = 4;

    @Override
    protected void doSort(E[] array, int start, int end, Comparator<E> cmp) {
        mergeSort(array, start, end - 1, cmp);
    }

    /**
     * sort array[start, end] in nature-order by cmp
     */
    private void mergeSort(E[] array, int start, int end, Comparator<E> cmp) {
        int len = end - start + 1;
        if (len <= 1) {
            // todo problem complex is small enough to use a simple O(n ^ 2)
//            innerSort.sort(array, start, end);
            return;
        }
        // sort by insertion-sort
        int mid = start + len / 2 - 1; // note, todo here must sub 1.
        mergeSort(array, start, mid, cmp);
        mergeSort(array, mid + 1, end, cmp);
        mergeByArrayCopy(array, start, mid, end, cmp);
    }

    /**
     * merge two sub-array in-order.
     * array[left, mid] is nature-order by cmp
     * array[mid + 1, right] is nature-order by cmp
     * <p>
     * note: if i < j && array[i] == array[j], then tmp[k] = array[i]. so it's stable.
     * <p>
     */
    private void mergeByArrayCopy(E[] array, int left, int mid, int right, Comparator<E> cmp) {
        Object[] tmp = new Object[right - left + 1];
        int i = left, j = mid + 1, k = 0;
        while (i <= mid && j <= right) {
            if (cmp.compare(array[i], array[j]) <= 0) {
                tmp[k++] = array[i++];
            } else {
                tmp[k++] = array[j++];
            }
        }
        if (i <= mid) {
            System.arraycopy(array, i, tmp, k, (mid - i + 1));
        }
        if (j <= right) {
            System.arraycopy(array, j, tmp, k, (right - j + 1));
        }
        System.arraycopy(tmp, 0, array, left, tmp.length);
    }

    /**
     * in-place merge algo
     * todo
     */
    private void mergeInPlace(E[] array, int left, int mid, int right, Comparator<E> cmp) {

    }

        public static void main(String[] args) {
        IMyArraySort<Integer> sort = new MyMergeSort<>();
        MyAbstractArraySort.test(sort);
        MyAbstractArraySort.testRange(sort);
    }
}
