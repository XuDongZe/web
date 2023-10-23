package com.xdz.dsa.graph;

import com.xdz.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * detect whether a digraph is topo or not.
 * and if topo, give a topological-travel
 */
public class Topological {
    private Iterable<Integer> order; // one of topological-order

    public Topological(MyDigraph digraph) {
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (!cycle.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(digraph);
            /**
             * topological-order is on order of this:
             * v->w then v is finished ahead of w.
             *
             * in dfs, we have: v->w, so in pre order, v is ahead of w;
             * in post order, dfs(w) is finished ahead of dfs(v), so w->v
             * (there is no circle so we first finish dfs(w) then return to try to finish dfs(v))
             * so in reverse-post-order, we say v->w.
             *
             * so if we has a reverse-post-order v->w, we can say there is an edge v->w in the original-digraph.
             * and this is what we want.
             */
            order = dfs.reversePost();
        }
    }

    /**
     * is directed-no-ring graph or not. 有向无环图。
     */
    public boolean isDAG() {
        return order != null;
    }

    public Iterable<Integer> order() {
        return order;
    }


    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + "tinyDG.txt"; // not DAG
        filename = Constants.absoluteResourcePath + "algo\\" + "tinyOrder.txt"; // DAG
        MyDigraph graph = MyDigraph.create(new FileInputStream(filename));

        Topological topological = new Topological(graph);
        if (topological.isDAG()) {
            for (Integer v : topological.order()) {
                System.out.print(v + "->");
            }
            System.out.println();
        } else {
            System.out.println("not DAG");
        }
    }
}
