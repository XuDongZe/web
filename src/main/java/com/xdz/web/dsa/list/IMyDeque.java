package com.xdz.web.dsa.list;

public interface IMyDeque<E> {
    /**
     * 在队头插入指定元素
     */
    void addFirst(E e);

    /**
     * 在队尾插入指定元素
     */
    void addLast(E e);

    /**
     * 删除队头元素
     */
    E removeFirst();

    /**
     * 删除队尾元素
     */
    E removeLast();

    /**
     * 获取第一个元素
     */
    E getFirst();

    /**
     * 获取最后一个元素
     */
    E getLast();
}
