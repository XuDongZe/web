package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.bag.IMyBag;
import com.xdz.dsa.bag.MyBag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * 连通分量 connect-counter
 * 连通分量可以将图的所有顶点切分为等价关系。
 */
public class CC {

    private boolean[] marked;
    private int[] id; // id[v] = i: the id of vertex v is i.
    private int count; // the num of max-sub-connected. can be used as id-allocate

    public CC(MyGraph graph) {
        int V = graph.V();
        marked = new boolean[V];
        id = new int[V];
        count = 0;

        for (int v = 0; v < V; v++) {
            if (!marked[v]) {
                dfs(graph, v);
                count ++;
            }
        }
    }

    /**
     * start with vertex v, allocate all connected-vertex id as count.
     */
    private void dfs(IMyGraph graph, int v) {
        marked[v] = true;
        id[v] = count;

        for (Integer w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }
    }

    /**
     * is vertex v and w is connected ?
     */
    public boolean connected(int v, int w) {
        return id[v] == id[w];
    }

    /**
     * the num of max-sub-connected graph in G
     */
    public int count() {
        return count;
    }

    /**
     * the uniq id of max-sub-connected graph include vertex v.
     */
    public int id(int v) {
        return id[v];
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        MyGraph graph = MyGraph.create(new FileInputStream(filename));
        CC cc = new CC(graph);

        int M = cc.count();
        System.out.println(M + " components");

        IMyBag<Integer>[] components =(IMyBag<Integer>[]) new MyBag[M];
        for (int i = 0; i < M; i ++) {
            components[i] = new MyBag();
        }
        for (int v = 0; v < graph.V(); v ++) {
            components[cc.id(v)].add(v);
        }

        for (int i = 0; i < M; i++) {
            System.out.printf("components[%s]: ", i);
            for (Integer v : components[i]) {
                System.out.printf(v + " ");
            }
            System.out.println();
        }
    }
}
