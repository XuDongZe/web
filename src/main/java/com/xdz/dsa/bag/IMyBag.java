package com.xdz.dsa.bag;

/**
 * bag adt. un-sorted collection. like a set.
 */
public interface IMyBag<T> extends Iterable<T> {
    /**
     * add an item to bag
     */
    void add(T item);

    /**
     * contains an item or not
     */
    boolean contains(T item);

    /**
     * bag is empty or not
     */
    boolean isEmpty();

    /**
     * num of element
     */
    int size();
}
