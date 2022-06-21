package com.xdz.dsa.unionfindset;

public interface IMyUnionFindSet {
    /**
     * n -> {x}, n is an element of set-x
     * return x
     */
    int find(int n);

    /**
     * {root1} {root2} -> {root1, root2}
     * return the new union-ed set id
     */
    int union(int root1, int root2);
}
