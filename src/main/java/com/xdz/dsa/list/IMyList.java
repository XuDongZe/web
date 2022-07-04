package com.xdz.dsa.list;

import java.util.Iterator;

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
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 是否包含指定元素
     */
    default boolean contains(E e) {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E next = it.next();
            if ((e == null && next == null) || (e != null && e.equals(next))) {
                return true;
            }
        }
        return false;
    }

    default void addAll(E[] array) {
        if (array == null || array.length <= 0) {
            return;
        }
        for (E e : array) {
            addLast(e);
        }
    }

    /**
     * 在队头插入指定元素
     */
    @Override
    default void addFirst(E e) {
        add(0 ,e);
    }

    /**
     * 在队尾插入指定元素
     */
    @Override
    default void addLast(E e) {
        add(size(), e);
    }

    /**
     * 删除队头元素
     */
    @Override
    default E removeFirst() {
        return remove(0);
    }

    /**
     * 删除队尾元素
     */
    @Override
    default E removeLast() {
        return remove(size() - 1);
    }

    @Override
    default E getFirst() {
        return get(0);
    }

    @Override
    default E getLast() {
        return get(size() - 1);
    }

    default String string() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (E e : this) {
            builder.append(e).append(", ");
        }
        if (builder.charAt(builder.length() - 1) == ' ') {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]");
        return builder.toString();
    }
}
