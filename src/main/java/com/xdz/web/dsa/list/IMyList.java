package com.xdz.web.dsa.list;

/**
 * Description: 列表ADT<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/13 15:21<br/>
 * Version: 1.0<br/>
 */
public interface IMyList<E> extends Iterable<E>, IMyDeque<E> {
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

    /**
     * list's element count
     */
    int size();

    /**
     * list is empty: no element in list. or size() == 0
     */
    boolean isEmpty();

    /**
     * 是否包含指定元素
     */
    boolean contains(E e);

    @Override
    default E getFirst() {
        return get(0);
    }

    @Override
    default E getLast() {
        return get(size() - 1);
    }
}
