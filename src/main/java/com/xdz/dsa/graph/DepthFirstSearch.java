package com.xdz.dsa.graph;

import com.xdz.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DepthFirstSearch {
    private boolean[] marked;
    private int count;

    /**
     * @param s search source vertex in G
     */
    public DepthFirstSearch(IMyGraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    private void dfs(IMyGraph G, int s) {
        marked[s] = true;
        count ++;
        for (Integer w : G.adj(s)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

    /**
     * is w connected with s
     */
    public boolean marked(int w) {
        return marked[w];
    }

    /**
     * the num of vertex which is connected with s (include s itself)
     */
    public int count() {
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "\\algo\\" + args[0];
        MyGraph graph = MyGraph.create(new FileInputStream(filename));

        // max-connected set
        boolean[] marked = new boolean[graph.V()];
        for (int v = 0; v < graph.V(); v++) {
            if (!marked[v]) {
                DepthFirstSearch search = new DepthFirstSearch(graph, v);
                System.out.print(v + " | ");
                for (int w = 0; w < graph.V(); w++) {
                    if (search.marked(w)) {
                        System.out.print(w + " ");
                        marked[w] = true;
                    }
                }
                System.out.println();
            }
        }
    }
}
