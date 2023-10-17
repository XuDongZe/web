package com.xdz.dsa.graph;

import java.io.InputStream;

/**
 * Description: graph adt<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/28 13:40<br/>
 * Version: 1.0<br/>
 */
public interface IMyGraph {
    /**
     * num of vertex
     */
    int V();

    /**
     * num of edges
     */
    int E();

    /**
     * add an edge, from v to w.
     */
    void addEdge(int v, int w);

    /**
     * vertex v adj: [? | v -> ?]
     */
    Iterable<Integer> adj(int v);

    /**
     * the degree of v
     * un-ordered graph, degree(v) is the num of edge which link with vertex v.
     */
    default int degree(int v) {
        int degree = 0;
        for (Integer w : adj(v)) {
            degree ++;
        }
        return degree;
    }
}
