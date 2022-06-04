package com.xdz.web.dsa.Tuple;

/**
 * Description: 二元组<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/4 16:47<br/>
 * Version: 1.0<br/>
 */
public class TwoTuple<A, B> {
    public A first;
    public B second;

    public TwoTuple(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
