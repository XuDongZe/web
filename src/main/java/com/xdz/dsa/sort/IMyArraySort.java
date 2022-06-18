package com.xdz.dsa.sort;

import java.util.Comparator;

public interface IMyArraySort<E extends Comparable<E>> {

    /**
     * sort array[start, end)
     * @param start include
     * @param end exclude
     */
    void sort(E[] array, int start, int end, Comparator<E> cmp);

    default void sort(E[] array, int start, int end) {
        sort(array, start, end, Comparator.naturalOrder());
    }

    /**
     * @param array the array to be sort
     * @param cmp customer comparator, can implement descending cmp
     */
    default void sort(E[] array, Comparator<E> cmp) {
        sort(array, 0, array.length, cmp);
    }

    /**
     * default sort order is ascending
     */
    default void sort(E[] array) {
        sort(array, 0, array.length, Comparator.naturalOrder());
    }

    /**
     * default sort order is descending
     */
    default void reverseSort(E[] array) {
        sort(array, Comparator.reverseOrder());
    }

    default void reverseSort(E[] array, int start, int end) {
        sort(array, start, end, Comparator.reverseOrder());
    }
}
