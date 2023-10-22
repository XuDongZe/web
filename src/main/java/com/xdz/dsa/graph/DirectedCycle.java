package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.stack.IMyStack;
import com.xdz.dsa.stack.MyArrayStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * directed-graph cycle check.
 * we will find out is there has a cycle in di-graph, and if has,
 * we use a path(from one vertex in cycle, then start travel try to return-itself.
 */
public class DirectedCycle {
    private boolean[] marked;
    private boolean[] onStack; // 递归调用栈上的所有顶点
    private int edgeTo[]; // try to log the cycle path.
    private IMyStack<Integer> cycle; // start from one vertex and return to itself. one cycle

    public DirectedCycle(MyDigraph digraph) {
        marked = new boolean[digraph.V()];
        onStack = new boolean[digraph.V()];
        edgeTo = new int[digraph.V()];
        cycle = null;

        for (int v = 0; v < digraph.V() && !hasCycle(); v++) {
            if (!marked[v]) {
                dfs(digraph, v);
            }
        }
    }

    private void dfs(MyDigraph digraph, int v) {
        marked[v] = true;
        onStack[v] = true;
        for (Integer w : digraph.adj(v)) {
            if (hasCycle()) { // cut
                return;
            }

            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(digraph, w);
            } else {
                if (onStack[w]) {
                    // check a cycle. v->w is the last edge to finish the cycle.
                    // log the cycle in stack: w-> .... ->v->w. start from w and end with w.
                    IMyStack<Integer> path = new MyArrayStack<>();
                    for (int x = v; x != w; x = edgeTo[x]) {
                        path.push(x);
                    }
                    path.push(w);
                    path.push(v);
                    cycle = path;
                }
            }
        }
        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        MyDigraph graph = MyDigraph.create(new FileInputStream(filename));

        DirectedCycle cycle = new DirectedCycle(graph);
        if (cycle.hasCycle()) {
            for (Integer v : cycle.cycle()) {
                System.out.print(v + "->");
            }
            System.out.println();
        } else {
            System.out.println("no cycle");
        }
    }
}
