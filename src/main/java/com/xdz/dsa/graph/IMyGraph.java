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
     * adj string description of each vertex
     */
    default String __toString() {
        int V = V();
        String s = V + " vertexes, " + E() + " edges\n";
        for (int v = 0; v < V; v++) {
            // the adj(v) description
            s += v + ": ";
            for (Integer w : adj(v)) {
                s += w + " ";
            }
            s += "\n";
        }
        return s;
    }
}
