package com.xdz.dsa.graph;

import com.xdz.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * a graph's transitive-closure (传递闭包) is a data-struct that:
 * 1. a graph
 * 2. the vertex is the same as original-graph
 * 3. if v..->..w(from v to w has a path) in the original-graph, then there is a direct edge
 * v->w in the closure graph.
 * <p>
 * simply, closure graph has more edges than the original one. for example, if original is
 * a ring has v vertex and v edges. then closure-graph has v^2 edges.
 * <p>
 * close graph also like the path-compress union-find-set.
 * <p>
 * closure graph can implement by adj-array: array[v][w] == true suppose that there is an edge v->w.
 * <p>
 * closure graph api can also implement by dfs-reachable api: we use n dfs instance,
 * dfs[v].marked[w] is the value of v..->..w
 */
public class TransitiveClosure {

    private DepthFirstSearch[] all;

    public TransitiveClosure(MyDigraph digraph) {
        all = new DepthFirstSearch[digraph.V()];
        for (int v = 0; v < digraph.V(); v++) {
            // v is the dfs start point.
            // so all[v].marked[w] is v..->..w or not.
            // if we also need the path, we just change dfs to dfs-paths.
            all[v] = new DepthFirstSearch(digraph, v);
        }
    }

    /**
     * is v..->..w ?
     * that's say from v to w has a path.
     */
    public boolean reachable(int v, int w) {
        return all[v].marked(w);
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + "tinyDG.txt";
        MyDigraph graph = MyDigraph.create(new FileInputStream(filename));

        TransitiveClosure closure = new TransitiveClosure(graph);
        System.out.println(closure.reachable(0, 1));
        System.out.println(closure.reachable(1, 0));
        System.out.println(closure.reachable(6, 5));
        System.out.println(closure.reachable(9, 1));
        System.out.println(closure.reachable(1, 0));
    }
}
