package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.bag.IMyBag;
import com.xdz.dsa.bag.MyBag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * direction graph adt
 */
public class MyDigraph implements IMyGraph {

    private int V;
    private int E;
    private IMyBag<Integer>[] adj; // adj[v] is all adj-vertex which start with v.

    /**
     * create a new graph with v vertex, no edges.
     */
    public static MyDigraph create(int V) {
        MyDigraph graph = new MyDigraph();
        graph.V = V;
        graph.E = 0;
        graph.adj = new IMyBag[V];
        for (int i = 0; i < V; i++) {
            graph.adj[i] = new MyBag<>();
        }
        return graph;
    }

    /**
     * create a new graph from stream.
     * the stream format:
     * V E
     * v1->v2
     * v2->v3
     * ...
     */
    public static MyDigraph create(InputStream is) {
        Scanner scanner = new Scanner(is);
        int V = scanner.nextInt();
        int E = scanner.nextInt();
        MyDigraph graph = create(V);
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            graph.addEdge(v, w);
        }
        return graph;
    }

    /**
     * the reverse-graph. copy one from graph but all edges are turn back.
     * use this we can find all edges endpoints which end with target v.
     */
    public static MyDigraph reverse(MyDigraph graph) {
        MyDigraph reverse = new MyDigraph();
        for (int v = 0; v < graph.V(); v++) {
            for (Integer w : graph.adj(v)) {
                // v->w turn to w->v, then add to reverse
                reverse.addEdge(w, v);
            }
        }
        return reverse;
    }

    /**
     * num of vertex
     */
    @Override
    public int V() {
        return V;
    }

    /**
     * num of edges
     */
    @Override
    public int E() {
        return E;
    }

    /**
     * add an edge v->w
     */
    @Override
    public void addEdge(int v, int w) {
        adj[v].add(w);
        E++;
    }

    /**
     * all edges endpoint vertex which start with v
     */
    @Override
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    @Override
    public String toString() {
        return __toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        MyDigraph graph = MyDigraph.create(new FileInputStream(filename));
        System.out.println(graph);
    }
}
