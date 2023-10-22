package com.xdz.dsa.stack;

import java.util.Iterator;

public interface IMyStack<E> extends Iterable<E> {
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


    default String __toString() {
        String res = "[";
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            res += (it.next() + ",");
        }
        if (res.charAt(res.length() - 1) == ',') {
            res = res.substring(0, res.length() - 1);
        }
        res += "]";
        return res;
    }
}
