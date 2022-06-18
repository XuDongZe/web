package com.xdz.dsa.heap;

public interface IMyHeap<E extends Comparable<E>> {
    void insert(E e);

    E pop();

    E top();

    void clear();

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }
}
