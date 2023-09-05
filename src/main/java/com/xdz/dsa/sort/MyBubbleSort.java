package com.xdz.dsa.sort;

import com.xdz.util.CollectionUtil;

import java.util.Comparator;

/**
 * Description: bubble sort<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 23:49<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * like insertion-sort, bubble sort use (n - 1) times sub-sort.
 * after p-th sub-sort, array[n - p , n) is the max-p items. (if nature-order)
 * at p+1-th sub-sort, we swap array(i, j) if (i, j) is not meet nature-order. i, j is range: [0, n - p)
 * so after p+1-th, array[n - p - 1] position is meet nature-order.
 *
 * Summary:
 *
 * Complex:
 * best = worst = avg = O(n ^ 2).
 * is worse than insertion-sort. for is not use sorted-part effectively:
 * in insertion-sort if we find the first pair meet comp's nature order, then inner-loop is end.
 * but in bubble-sort, we must ite all array[0, n - p) at p-th sub-sort. we have no chance to break earlier in inner-loop.
 *
 * Stability:
 * true.
 * if i < j and array[i] == array[j], we base i and find a j, then do not swap.
 * so array[j] is also behind array[i] after sub-sort.
 * </pre>
 */
public class MyBubbleSort<E extends Comparable<E>> extends MyAbstractArraySort<E> {
    @Override
    protected void doSort(E[] array, int start, int end, Comparator<E> cmp) {
        for (int i = start; i < end; i++) {
            // array[start..i) is sorted
            for (int j = end - 1; j > i; j--) {
                if (less(array[j], array[j - 1], cmp)) {
                    exch(array, j, j - 1);
                }
            }
            // array[start..i] is sorted
            assert isSorted(array, start, i, cmp);
        }
        assert isSorted(array, start, end, cmp);
    }

    public static void main(String[] args) {
        IMyArraySort<Integer> sort = new MyBubbleSort<>();
        test(sort);
        testRange(sort);
    }
}
