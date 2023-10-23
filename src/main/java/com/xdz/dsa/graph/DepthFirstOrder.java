package com.xdz.dsa.graph;

import com.xdz.Constants;
import com.xdz.dsa.Queue.IMyQueue;
import com.xdz.dsa.Queue.MyArrayCircleQueue;
import com.xdz.dsa.stack.IMyStack;
import com.xdz.dsa.stack.MyArrayStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * travel a graph by dfs.
 * support pre() post() reversePost() order.
 */
public class DepthFirstOrder {

    private boolean[] marked;
    private IMyQueue<Integer> pre; // the pre order of all vertex.
    private IMyQueue<Integer> post;
    private IMyStack<Integer> reversePost;

    public DepthFirstOrder(IMyGraph graph) {
        marked = new boolean[graph.V()];
        pre = new MyArrayCircleQueue<>(graph.V());
        post = new MyArrayCircleQueue<>(graph.V());
        reversePost = new MyArrayStack<>();

        for (int v = 0; v < graph.V(); v++) {
            if (!marked[v]) {
                dfs(graph, v);
            }
        }
    }

    private void dfs(IMyGraph graph, int v) {
        pre.enqueue(v);

        marked[v] = true;
        for (Integer w : graph.adj(v)) {
            if (!marked[w]) {
                dfs(graph, w);
            }
        }

        post.enqueue(v);
        reversePost.push(v);
    }

    public Iterable<Integer> pre() {
        return pre;
    }

    public Iterable<Integer> post() {
        return post;
    }

    public Iterable<Integer> reversePost() {
        return reversePost;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filename = Constants.absoluteResourcePath + "algo\\" + args[0];
        IMyGraph graph = MyDigraph.create(new FileInputStream(filename));

        DepthFirstOrder order = new DepthFirstOrder(graph);
        System.out.println(order.pre());
        System.out.println(order.post());
        System.out.println(order.reversePost());
    }
}
