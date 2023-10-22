package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.bag.IMyBag;
import com.xdz.dsa.bag.MyBag;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;

/**
 * Description: adj graph implement<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/29 12:37<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * we use an array of list: array index is vertex, and array element is a list of adj-vertexes of v.
 * we call it adj-list array.
 * </pre>
 */
public class MyGraph implements IMyGraph {
    private int V; // num of vertex
    private int E; // num of edges
    private IMyBag<Integer>[] adj; // adj-list array

    private MyGraph() {
    }

    /**
     * create a new graph, with no edges.
     */
    public static MyGraph create(int V) {
        MyGraph graph = new MyGraph();
        graph.V = V;
        graph.E = 0;
        graph.adj = (IMyBag<Integer>[]) new MyBag[V];
        for (int v = 0; v < V; v++) {
            graph.adj[v] = new MyBag<>();
        }
        return graph;
    }

    /**
     * create a new graph, using an is.
     * format is:
     * // vertex-num edge-num
     * v e
     * // edges
     * v1 w1
     * v2 w2
     * ...
     */
    @SneakyThrows
    public static MyGraph create(InputStream is) {
        Scanner scanner = new Scanner(is);
        int V = scanner.nextInt();
        int E = scanner.nextInt();
        MyGraph graph = create(V);
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            graph.addEdge(v, w);
        }
        return graph;
    }

    @Override
    public int V() {
        return V;
    }

    @Override
    public int E() {
        return E;
    }

    @Override
    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E ++;
    }

    @Override
    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    /**
     * the degree of v
     * un-ordered graph, degree(v) is the num of edge which link with vertex v.
     */
    public int degree(int v) {
        int degree = 0;
        for (Integer w : adj(v)) {
            degree ++;
        }
        return degree;
    }

    @Override
    public String toString() {
        return __toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        MyGraph graph = MyGraph.create(new FileInputStream(filename));
        System.out.println(graph);
    }
}
