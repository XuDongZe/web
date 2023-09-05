package com.xdz.dsa.sort;

import java.util.Comparator;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/13 0:37<br/>
 * Version: 1.0<br/>
 * 
 * <pre>
 * like bubble-sort & selection-sort, use (n - 1) times sub-sort.
 * the core idea is, at each sub-sort, we pick the min/max element from the un-sorted part,
 * and put to the sorted-part's tail (in array, we using a swap(array, i, min_idx) to implement this)
 * 
 * we use a i which means the sorted-part's element count to split the ordered & un-ordered part:
 * ordered:     array[0, i)
 * un-ordered:  array[i, n)
 *
 * assume that we find the very element from un-ordered part, the idx is j, so we just put array[j] to array[i].
 * and consider that array[i] is still not sorted now. so do not replace array[i] directly -> we need to find
 * a position to place it -> and just now we place array[j] to a new position so array[j] is hole (un-used) now.
 *
 * so just swap(i, j).
 *
 * like bubble-sort, we can not break from inner-loop earlier before we iterate all we need to cmp.
 * and if i < j && array[i] == array[j], (we base i and find a new j), we do not swap.
 *
 * Array SelectionSort is not stability. consider this seq: [3B, 3A, 2, 1, 4]
 * 1. select min value: 1, swap with 4A => [1, 3A, 2, 3B, 4]
 * 2. select min value: 2, swap with 4B => [1, 2, 3A, 3B, 4]
 * now 3A,3B order is diff with original. so stability is not guaranteed.
 *
 * about stability here is an intuitive idea: if swap(i, j), and for pairs(i, i..j), and pairs(j, i..j), we
 * can't guarantee there stability.
 *
 * Summary:
 * Complex:
 * best = worst = avg = O(n ^ 2)
 * Stability:
 * false
 * </pre>
 */
public class MySelectionSort<E extends Comparable<E>> extends MyAbstractArraySort<E> {
    @Override
    protected void doSort(E[] array, int start, int end, Comparator<E> cmp) {
        for (int i = start; i < end; i++) {
            // now sorted-part is array[start, i)
            int k = i;
            for (int j = i + 1; j < end; j++) {
                if (less(array[j], array[k], cmp)) {
                    k = j;
                }
            }
            // now k is the min-value idx for comp's nature order.
            exch(array, i, k);
            // now array[i] is sorted and sorted-part is array[start, i]
            assert isSorted(array, start, i, cmp);
        }
        assert isSorted(array, start, end, cmp);
    }

    public static void main(String[] args) {
        IMyArraySort<Integer> sort = new MySelectionSort<>();
        test(sort);
        testRange(sort);
    }
}
