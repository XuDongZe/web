package com.xdz.dsa.sort;

import java.util.Comparator;

public interface IMyArraySort<E extends Comparable<E>> {

    /**
     * Rearranges the array[start..end) in ascending order, using a comparator.
     * @param start include
     * @param end exclude
     */
    void sort(E[] array, int start, int end, Comparator<E> cmp);

    /**
     * Rearranges the array[start..end) in ascending order, using the natural order.
     */
    default void sort(E[] array, int start, int end) {
        sort(array, start, end, Comparator.naturalOrder());
    }

    /**
     * Rearranges the array in ascending order, using a comparator
     */
    default void sort(E[] array, Comparator<E> cmp) {
        sort(array, 0, array.length, cmp);
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     */
    default void sort(E[] array) {
        sort(array, 0, array.length, Comparator.naturalOrder());
    }

    /**
     * Rearranges the array in descending order
     */
    default void reverseSort(E[] array) {
        sort(array, 0, array.length, Comparator.reverseOrder());
    }

    /**
     * Rearranges the array in descending order
     */
    default void reverseSort(E[] array, int start, int end) {
        sort(array, start, end, Comparator.reverseOrder());
    }

    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/
    /**
     * is a < b ?
     */
    default boolean less(E a, E b, Comparator<E> cmp) {
        return cmp.compare(a, b) < 0;
    }

    default boolean less(E a, E b) {
        return less(a, b, Comparator.naturalOrder());
    }

    /**
     * exchange array[i] and array[j]
     */
    default void exch(E[] array, int i, int j) {
        E swap = array[i];
        array[i] = array[j];
        array[j] = swap;
    }

    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/

    /**
     * check array[start..end) is sorted
     */
    default boolean isSorted(E[] array, int start, int end, Comparator<E> cmp) {
        for (int i = start + 1; i < end; i ++) {
            if (less(array[i], array[i - 1], cmp)) {
                return false;
            }
        }
        return true;
    }

    default boolean isSorted(E[] array, int start, int end) {
        return isSorted(array, start, end, Comparator.naturalOrder());
    }

    default boolean isSorted(E[] array) {
        return isSorted(array, 0, array.length, Comparator.naturalOrder());
    }
}
