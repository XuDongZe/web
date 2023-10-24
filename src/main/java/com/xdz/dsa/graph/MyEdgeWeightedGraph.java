package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.bag.IMyBag;
import com.xdz.dsa.bag.MyBag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * weighted-edge un-directed graph.
 *
 * we use edge's adj list to describe it.
 * adj[v] is a list, element of the list is the adj-edges of vertex v.
 *
 * this is like MyGraph. just use Edge replace Integer(vertex) in adj list.
 */
public class MyEdgeWeightedGraph {
    private int V; // num of vertex
    private int E; // num of E
    private IMyBag<Edge>[] adj;

    /**
     * create a graph. with V vertex and no edges.
     */
    public static MyEdgeWeightedGraph create(int V) {
        MyEdgeWeightedGraph graph = new MyEdgeWeightedGraph();
        graph.V = V;
        graph.adj = new MyBag[V];
        for (int v = 0; v < V; v++) {
            graph.adj[v] = new MyBag<>();
        }
        return graph;
    }

    /**
     * create from an input-stream. the input format is:
     * V E
     * v1 w1 weight1
     * v2 w2 weight2
     * ...
     */
    public static MyEdgeWeightedGraph create(InputStream is) {
        Scanner scanner = new Scanner(is);
        int V = scanner.nextInt();
        MyEdgeWeightedGraph graph = create(V);
        int E = scanner.nextInt();
        for (int i = 0; i < E; i++) {
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            double weight = scanner.nextDouble();
            graph.addEdge(new Edge(v, w, weight));
        }
        return graph;
    }

    /**
     * num of vertex
     */
    public int V() {
        return V;
    }

    /**
     * num of edges
     */
    public int E() {
        return E;
    }

    /**
     * add an edge to graph. we do this:
     * add e to the adj-list of e.v
     * add e to the adj-list of e.w
     */
    public void addEdge(Edge e) {
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    /**
     * the v's adj edge list.
     */
    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

    /**
     * all edges of the graph. we add the edges of each vertex to a bag to save it.
     *
     * a description of EWG: all edge set
     */
    public Iterable<Edge> edges() {
        IMyBag<Edge> bag = new MyBag<>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj[v]) {
                if (e.other(v) > v) {
                    // remove repeat e.
                    bag.add(e);
                }
            }
        }
        return bag;
    }

    @Override
    public String toString() {
        StringBuilder builder =new StringBuilder();
        builder.append(String.format("%d vertexes, %d edges\n", V, E));
        Iterable<Edge> edges = edges();
        for (Edge edge : edges) {
            builder.append(edge).append("\n");
        }
        return builder.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + "tinyEWG.txt";
        MyEdgeWeightedGraph graph = MyEdgeWeightedGraph.create(new FileInputStream(filename));
        System.out.println(graph);
    }
}
