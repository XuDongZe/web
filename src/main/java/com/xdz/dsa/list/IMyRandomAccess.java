package com.xdz.dsa.list;

public interface IMyRandomAccess<E> {
    /**
     * set by idx
     */
    E get(int idx);

    /**
     * get by idx
     */
    void set(int idx, E e);

    /**
     * add by idx
     */
    void add(int idx, E e);

    /**
     * remove by idx
     */
    E remove(int idx);
}
