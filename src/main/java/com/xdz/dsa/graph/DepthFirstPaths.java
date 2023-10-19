package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.stack.IMyStack;
import com.xdz.dsa.stack.MyArrayStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * find a path: s -> v for target s and any other vertex in a graph
 */
public class DepthFirstPaths {
    private boolean[] marked;
    private int[] edgeTo; // edgeTo[w] = v: v->w is the sub-path from s->w. v is the parent node in path's tree.
    private final int s; // the target source vertex

    public DepthFirstPaths(IMyGraph G, int s) {
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        dfs(G, s);
    }

    /**
     * now we at vertex v in G
     */
    private void dfs(IMyGraph G, int v) {
        marked[v] = true;
        G.adj(v).forEach((w) -> {
            if (!marked[w]) {
                // remember this path
                edgeTo[w] = v;
                dfs(G, w);
            }
        });
    }

    /**
     * is there a path from s to v
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * the path from s to v, begin with s and end with v
     * if v == s. then the path has just one vertex v.
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }

        IMyStack<Integer> path = new MyArrayStack<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    /**
     * args[0] for filename to create G
     * args[1] is the target source
     * <p>
     * show the path to any vertex
     */
    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        IMyGraph graph = MyGraph.create(new FileInputStream(filename));
        int s = Integer.parseInt(args[1]);

        DepthFirstPaths paths = new DepthFirstPaths(graph, s);
        // iterate vertex
        for (int v = 0; v < graph.V(); v++) {
            System.out.printf("%s to %s: ", s, v);
            if (paths.hasPathTo(v)) {
                paths.pathTo(v).forEach((x) -> {
                    if (x == s) {
                        System.out.printf(x + "");
                    } else {
                        System.out.printf("-" + x);
                    }
                });
            } else {
                System.out.printf("not connected");
            }
            System.out.println();
        }
    }
}
