package com.xdz.dsa.graph;

import com.xdz.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TwoColor {
    private boolean[] marked;
    private boolean[] color;
    private boolean canBeTwoColor;

    public TwoColor(IMyGraph graph) {
        marked = new boolean[graph.V()];
        color = new boolean[graph.V()];
        canBeTwoColor = true;

        for (int v = 0; v < graph.V() && canBeTwoColor; v++) {
            if (!marked[v]) {
                dfs(graph, v);
            }
        }
    }

    /**
     * mock paint color from v by dfs order, and judge two-color or not in cc path.
     */
    private void dfs(IMyGraph graph, int v) {
        if (!canBeTwoColor) {
            return;
        }

        marked[v] = true;

        for (Integer w : graph.adj(v)) {
            if (!marked[w]) {
                color[w] = !color[v]; // mock painting
                dfs(graph, w);
            } else {
                if (color[w] == color[v]) {
                    canBeTwoColor = false;
                    break;
                }
            }
        }
    }

    /**
     * can be binary partition or not
     */
    public boolean canBipartite() {
        return canBeTwoColor;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        IMyGraph graph = MyGraph.create(new FileInputStream(filename));

        TwoColor twoColor = new TwoColor(graph);
        System.out.println(twoColor.canBipartite());
    }
}
