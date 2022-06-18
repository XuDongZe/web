package com.xdz.dsa.stack;

public interface IMyStack<E> {
    /**
     * push one element to stack top
     */
    void push(E e);

    /**
     * remove stack top and return the removed one
     * @return
     */
    E pop();

    /**
     * get the element of stack top. read-only
     */
    E top();

    /**
     * @return the element nums
     */
    int size();

    /**
     * @return true if size() == 0.
     * stack init is empty
     */
    boolean isEmpty();
}
