package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.Queue.IMyQueue;
import com.xdz.dsa.Queue.MyArrayCircleQueue;
import com.xdz.dsa.Queue.MyArrayQueue;
import com.xdz.dsa.stack.IMyStack;
import com.xdz.dsa.stack.MyArrayStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * find shorted-path from target vertex s and any other vertex v.
 * using bfs
 */
public class BreadthFirstPaths {
    private int s;
    private boolean[] marked;
    private int[] edgeTo; // edgeTo[w] = v : v -> w

    public BreadthFirstPaths(IMyGraph graph, int s) {
        this.s = s;
        this.marked = new boolean[graph.V()];
        this.edgeTo = new int[graph.V()];

        bfs(graph, s);
    }

    /**
     * from vertex s to start bfs
     */
    private void bfs(IMyGraph graph, int s) {
        IMyQueue<Integer> queue = new MyArrayCircleQueue<>(graph.V());
        // mark source and enqueue
        marked[s] = true;
        queue.enqueue(s);
        // enqueue by path-len ordered
        while (!queue.isEmpty()) {
            Integer v = queue.dequeue();
            for (Integer w : graph.adj(v)) {
                if (!marked[w]) {
                    // v->w is shorted for queue's order
                    edgeTo[w] = v;
                    marked[w] = true; // marked for path to w is find.
                    queue.enqueue(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * the path to v from s
     */
    public Iterable<Integer> pathTo(int v) {
        IMyStack<Integer> path = new MyArrayStack<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        IMyGraph graph = MyGraph.create(new FileInputStream(filename));
        int s = Integer.parseInt(args[1]);

        BreadthFirstPaths paths = new BreadthFirstPaths(graph, s);
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
