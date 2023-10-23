package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.bag.IMyBag;
import com.xdz.dsa.bag.MyBag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * strong connected components parse. using by di-graph.
 *
 * strong connected relation-ship between v and w only if there is a path from v to w
 * as well as a path from w to v. we say v..->..w and w..->..v
 *
 * strong connected components are a vertex-set and every element has strong connected
 * relation-ship with each other.
 *
 * simply there must has at least one cycle in the components. and the components will
 * become large by include another cycle. so its cycles with connected-point(a vertex belong to more than one cycle.)
 */
public class KosarajuSCC {
    private boolean[] marked;
    private int[] id; // id[v] = i; => the vertex v's sc id is i
    private int count; // the sc count. and id allocator.

    public KosarajuSCC(MyDigraph digraph) {
        marked = new boolean[digraph.V()];
        id = new int[digraph.V()];

        DepthFirstOrder order = new DepthFirstOrder(MyDigraph.reverse(digraph));

        for (Integer v : order.reversePost()) {
            if (!marked[v]) {
                dfs(digraph, v);
                count ++;
            }
        }
    }

    /**
     * travel start with vertex v
     */
    private void dfs(MyDigraph digraph, int v) {
        marked[v] = true;
        id[v] = count;

        for (Integer w : digraph.adj(v)) {
            if (!marked[w]) {
                dfs(digraph, w);
            }
        }
    }

    /**
     * is (v,w) has sc relation-ship.
     */
    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

    /**
     * num of scc
     */
    public int count() {
        return count;
    }

    /**
     * the id of the only strong connected component include vertex v.
     */
    public int id(int v) {
        return id[v];
    }


    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + "tinyDG.txt";
        MyDigraph graph = MyDigraph.create(new FileInputStream(filename));
        KosarajuSCC cc = new KosarajuSCC(graph);

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
