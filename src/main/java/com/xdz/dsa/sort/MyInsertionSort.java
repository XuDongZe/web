package com.xdz.dsa.sort;

import java.util.Comparator;

/**
 * Description: insertion sort<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 13:38<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * let n = array.length.
 * insertion-sort use (n - 1) times sub-sort.
 * after p-th sub-sort, (p in range [1, n - 1]), array[0, p] is sorted:
 * array[0, 0] is sorted.
 * after p = 1, array[0, 1] is sorted
 * after p = 2, array[0, 2] is sorted.
 *
 * before p-th sub-sort, array[0, p - 1] is sorted. after p-th, array[0, p] i sorted.
 * so at p-th sub-sort, we do: place the array[p] to right position:
 * if ascending, find the first array[i] that array[i] < array[p].
 * if descending, find the first array[i] that array[i] > array[p].
 *
 * Complex:
 * 1. if array is sorted, then O(n). each sub-sort is O(1)
 * 2. if array is reverse-sorted, then O(n ^ 2). each sub-sort is O(n / 2)
 *
 * commonly, for compare-exchange sort algo, we can use reverse-pairs to analyze the complex.
 * we can easily prove the following two problems:
 * 1. for a random array(array.length == n), the avg(reverse-pairs num of array) = n * (n - 1) / 4.
 *   1.1 random array, we assume that full-array as complete-set, so all pairs is C(n, 2) (the concept of combinations)
 *   1.2 the pairs is nature otherwise reverse order. and is one to one mapping. so C(n, 2) / 2 = n * (n - 1) / 4
 * 2. in compare-exchange sort, each compare-exchange, we decr one reverse-order. and basic compare-exchange op is O(1).
 * so the complex of compare-exchange sort algo is O(n ^ 2).
 *
 * Stability:
 * a sort algo is stable, which means, if before sort:
 *   array[i] == array[j] && i < j,
 * then after sort, this is also established.
 *
 * for insertion-sort, we step right 1 if order(i, j) is not satisfy the param cmp-func.
 * so if we define array[i] == array[j], order(i, j) is satisfied. the algo is stable.
 *
 * summary:
 * Complex:
 *   best:  O(n)
 *   worst: O(n ^ 2)
 *   avg:   O(n ^ 2)
 * Stability:
 *   true
 * </pre>
 */
public class MyInsertionSort<E extends Comparable<E>> extends MyAbstractArraySort<E> {

    @SuppressWarnings("rawtypes")
    public static final IMyArraySort INSTANCE = new MyInsertionSort<>();

    @Override
    public void doSort(E[] array, int start, int end, Comparator<E> cmp) {
        // i-th sub-sort
//        for (int i = start + 1; i <= end - 1; i++) {
//            E tmp = array[i];
//            // find the right position for tmp,
//            // j < i && array[j] > array[i], order is not meet. copy
//            int j = i - 1;
//            for (; j >= start && cmp.compare(array[j], tmp) > 0; j--) {
//                // array[j] step right 1, now array[j] is hole(un-used)
//                array[j + 1] = array[j];
//            }
//            // now j = -1 || array[j] < tmp || array[j] == tmp. so tmp is array[j]'s successor.
//            array[j + 1] = tmp;
//        }

        __sort(array, start, end, cmp);
    }

    private void __sort(E[] array, int start, int end, Comparator<E> cmp) {
        for (int i = start; i < end; i++) {
            // array[start..i) is sorted
            // place array[i] place right-position in range[start..i)
            // bubble to find the right-position
            for (int j = i; j > 0 && less(array[j], array[j - 1], cmp); j--) {
                exch(array, j, j - 1);
            }
            // array[start..i] is sorted.
            assert isSorted(array, start, i, cmp);
        }
        assert isSorted(array, start, end, cmp);
    }

    /**
     * an optimized version of insertion sort (with half exchanges and a sentinel).
     */
    private void __sort_x(E[] array, int start, int end, Comparator<E> cmp) {
        // put the smallest element at [0], serve as sentinel
        // bubble a sentinel to [0]
        int exchanges = 0;
        for (int i = end - 1; i > start; i --) {
            if (less(array[i], array[i - 1], cmp)) {
                exch(array, i, i - 1);
                exchanges ++;
            }
        }
        // and check sorted already
        if (exchanges == 0) {
            return;
        }

        // insertion sort with half-exchange, now array[0] is the smallest
        for (int i = 2; i < end; i ++) {
            E v = array[i];
            int j = i;
            while (less(array[j], array[j - 1], cmp)) {
                array[j] = array[j - 1];
                j = j - 1;
            }
            array[j] = v;
        }
    }

    private void __sort_binary_search(E[] array, int start, int end, Comparator<E> cmp) {
        for (int i = start + 1; i < end; i ++) {
            // binary search to determine index j where to insert a[i]
            E v = array[i];
            int lo = 0, hi = i;
            while (lo < hi) {
                int mid = lo + (hi - lo) >> 2;
                if (less(v, array[mid], cmp)) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }

            // now lo >= hi
            // if lo > hi, then at prev step.
            // case 1: hi = mid, lo > hi, that's say lo(old) > mid(old), impossible
            // case 2: lo = mid + 1, lo > hi, that's say mid(old) + 1 > hi(old) => mid(old) >= hi(old),
            // at the least case: mid(old) == hi(old) => low(old) == hi(old), impossible
            // so lo > hi, impossible. so lo == hi.

            // now lo == hi. so index j is lo (or high). let's inset a[i] to index lo
            for (int j = i; j > lo; j --) {
                array[j] = array[j - 1];
            }
            array[lo] = v;

            assert isSorted(array, start, i, cmp);
        }
        assert isSorted(array, start, end, cmp);
    }

    public static void main(String[] args) {
        IMyArraySort<Integer> sort = new MyInsertionSort<>();
        test(sort);
        testRange(sort);
    }
}
