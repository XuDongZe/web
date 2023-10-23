package com.xdz.dsa.graph;

import com.xdz.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * find a graph has cycle or not
 */
public class Cycle {

    private boolean[] marked;
    private boolean hasCycle;

    public Cycle(MyGraph graph) {
        marked = new boolean[graph.V()];
        for (int s = 0; s < graph.V() && !hasCycle; s++) {
            if (!marked[s]) {
                dfs(graph, s, s);
            }
        }
    }

    /**
     * begin with s -> v, now pos at v and from s.
     * build a cc search to find if v can arrive w which is in the path of cc.
     */
    private void dfs(IMyGraph graph, int v, int u) {
        if (hasCycle) {
            return;
        }

        marked[v] = true;

        for (Integer w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w, v);
            } else {
                if (w != u) { // graph is not directed so w<->v is not cycle
                    hasCycle = true;
                    break;
                }
            }
        }
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        MyGraph graph = MyGraph.create(new FileInputStream(filename));

        Cycle cycle = new Cycle(graph);
        System.out.println(cycle.hasCycle());
    }
}
