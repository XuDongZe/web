package com.xdz.dsa.graph;

import com.google.common.collect.Lists;
import com.xdz.Constants;
import com.xdz.dsa.Queue.IMyQueue;
import com.xdz.dsa.Queue.MyArrayQueue;
import com.xdz.dsa.heap.IMyHeap;
import com.xdz.dsa.heap.MyHeap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * lazy prim implement of minimum spanning tree. no-direction graph.
 * <p>
 * we will find a edge of mst each step. at first the tree only has 1 vertex(any one),
 * then we add edges to it. mst's edge is the smallest of a transverse edge's set(any one).
 * <p>
 * prim define a transverse use a simple but useful idea: there are two vertex set, one is the vertex of mst, another is
 * the component. so we get a transverse: the edge's one vertex in smt, the other in component.
 * <p>
 * we split the vertex to two vertex set. A, B.
 * first A = {0}, transverse set is from A to B => 0-0's adj vertex, find the smallest edge E.
 * E's other vertex is {1}.
 * now A = {0, 1}. transverse set need grow: all edges define by the new vertex 1 and its adj.
 * now maybe some situation: a and b are both in mst => edge is expired, just remove it.
 * 2. a in mst and b not, can use.
 * <p>
 * we loop this, add V-1 edges to mst and got A = full vertex. done.
 * <p>
 * data struct:
 * 1. vertex v is in mst? marked[v] == boolean.
 * 2. mst: list of Edge.
 * 3. current transverse set: priority queue of Edge, to find the smallest edge at O(1)
 */
public class LazyPrimMST {
    private boolean marked[];
    private IMyQueue<Edge> mst; // the edges of mst. V-1
    private IMyHeap<Edge> pq; // min priority queue of transverse
    private double weight;

    public LazyPrimMST(MyEdgeWeightedGraph graph) {
        marked = new boolean[graph.V()];
        mst = new MyArrayQueue<>(graph.V() - 1);
        ArrayList<Edge> list = Lists.newArrayList(graph.edges());
        pq = MyHeap.create(list.toArray(new Edge[0]), Comparator.naturalOrder());

        visit(graph, 0);
        while (!pq.isEmpty()) {
            Edge e = pq.pop();
            int v = e.either(), w = e.other(v);
            if (marked[v] && marked[w]) continue;
            // we find a cross-edge, and it is pop from pq, it's smallest
            mst.enqueue(e);
            if (!marked[v]) visit(graph, v);
            if (!marked[w]) visit(graph, w);
        }
    }

    private void visit(MyEdgeWeightedGraph graph, int v) {
        marked[v] = true;
        for (Edge e : graph.adj(v)) {
            if (!marked[e.other(v)]) {
                pq.insert(e);
                weight += e.weight();
            }
        }
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return weight;
    }


    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + "tinyEWG.txt";
        MyEdgeWeightedGraph graph = MyEdgeWeightedGraph.create(new FileInputStream(filename));
        LazyPrimMST lazyPrimMST = new LazyPrimMST(graph);

        System.out.println("总权重: \t" + lazyPrimMST.weight());
        for (Edge edge : lazyPrimMST.edges()) {
            System.out.println(edge);
        }
    }
}
