package com.xdz.web.dsa.sort;

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
        for (int i = start + 1; i <= end - 1; i++) {
            E tmp = array[i];
            // find the right position for tmp,
            // j < i && array[j] > array[i], order is not meet. copy
            int j = i - 1;
            for (; j >= start && cmp.compare(array[j], tmp) > 0; j--) {
                // array[j] step right 1, now array[j] is hole(un-used)
                array[j + 1] = array[j];
            }
            // now j = -1 || array[j] < tmp || array[j] == tmp. so tmp is array[j]'s successor.
            array[j + 1] = tmp;
        }
    }

    public static void main(String[] args) {
        IMyArraySort<Integer> sort = new MyInsertionSort<>();
        MyAbstractArraySort.test(sort);
        MyAbstractArraySort.testRange(sort);
    }
}
