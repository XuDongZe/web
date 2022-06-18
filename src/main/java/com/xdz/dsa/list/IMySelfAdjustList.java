package com.xdz.dsa.list;

import java.util.Comparator;

/**
 * Description: self-adjust-list<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/4 12:40<br/>
 * Version: 1.0<br/>
 */
public interface IMySelfAdjustList<E> {

    @SuppressWarnings("unchecked")
    default void addAdjust(E e) {
        addAdjust(e,true);
    }

    /**
     * @param asc true is ascending otherwise is descending
     */
    @SuppressWarnings("unchecked")
    default void addAdjust(E e, boolean asc) {
        if (asc) {
            addAdjust(e, (o1, o2) -> ((Comparable<E>) o1).compareTo(o2));
        } else {
            addAdjust(e, (o1, o2) -> ((Comparable<E>) o2).compareTo(o1));
        }
    }

    /**
     * 增加一个元素，自适应排序。按cmp升序
     */
    void addAdjust(E e, Comparator<E> cmp);
}
