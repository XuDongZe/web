package com.xdz.dsa.graph;

import com.google.common.collect.Lists;
import com.xdz.dsa.Queue.IMyQueue;
import com.xdz.dsa.Queue.MyArrayCircleQueue;
import com.xdz.dsa.Queue.MyArrayQueue;
import com.xdz.dsa.list.IMyList;
import com.xdz.dsa.list.MyArrayList;
import com.xdz.dsa.stack.IMyStack;
import com.xdz.dsa.stack.MyArrayStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Description: adj graph implement<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/29 12:37<br/>
 * Version: 1.0<br/>
 */
public class MyAdjGraph implements IMyAdjGraph {

    /**
     * 结点数组
     */
    private IMyList<Vertex> vertexes;

    public MyAdjGraph() {
        this.vertexes = new MyArrayList<>(8);
    }

    public MyAdjGraph(IMyList<Edge> edges) {
        // max size is edge's count - 1.
        vertexes = new MyArrayList<>(edges.size());
        for (Edge e : edges) {
            addEdge(e);
        }
    }

    private void addEdge(Edge edge) {
        if (!edge.from.adjVertexes.contains(edge.to)) {
            edge.from.adjVertexes.addLast(edge.to);
            edge.from.outDegree++;
            edge.to.inDegree++;
        }
        if (!vertexes.contains(edge.from)) {
            vertexes.addLast(edge.from);
        }
        if (!vertexes.contains(edge.to)) {
            vertexes.addLast(edge.to);
        }
    }

    @Override
    public void topSort() {
        IMyList<String> result = topSortV2();
        System.out.println(result.string());
    }

    private IMyList<String> topSortV1() {
        IMyList<String> result = new MyArrayList<>(vertexes.size());
        for (int i = 0; i < vertexes.size(); i++) {
            Vertex v = findNewZeroInDegreeVertex();
            if (v == null) {
                throw new RuntimeException("found ring in graph");
            }
            v.topNo = i;
            for (Vertex adj : v.adjVertexes) {
                adj.inDegree--;
            }
            result.addLast(v.id);
        }
        return result;
    }

    private IMyList<String> topSortV2() {
        IMyList<String> result = new MyArrayList<>(vertexes.size());
        IMyQueue<Vertex> queue = new MyArrayQueue<>(vertexes.size());
        for (Vertex v : vertexes) {
            if (v.inDegree == 0) {
                queue.enqueue(v);
            }
        }

        int no = 0;
        while (!queue.isEmpty()) {
            Vertex v = queue.dequeue();
            v.topNo = no++;
            result.addLast(v.id);
            for (Vertex adj : v.adjVertexes) {
                if (--adj.inDegree == 0) {
                    queue.enqueue(adj);
                }
            }
        }
        if (no != vertexes.size()) {
            throw new RuntimeException("found ring in graph");
        }
        return result;
    }

    private Vertex findNewZeroInDegreeVertex() {
        for (Vertex v : vertexes) {
            if (v.topNo == 0 && v.inDegree == 0) {
                return v;
            }
        }
        return null;
    }

    // todo
    @Override
    public void shortedPath() {
        topSort();
        int[][] dp = new int[vertexes.size()][vertexes.size()];
        for (int i = 0; i < dp.length; i ++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
    }

    @Override
    public void dfs() {
        if (vertexes.isEmpty()) {
            return;
        }
        IMyList<String> result = new MyArrayList<>(vertexes.size());
//        dfsRecursive(vertexes.getFirst(), result);
        dfsIterator(vertexes.getFirst(), result);
        System.out.println(result.string());
    }

    private void dfsRecursive(Vertex v, IMyList<String> result) {
        if (v == null || v.visited) {
            return;
        }
        result.addLast(v.id);
        v.visited = true;
        for (Vertex next : v.adjVertexes) {
            dfsRecursive(next, result);
        }
    }

    private IMyList<String> dfsIterator(Vertex first, IMyList<String> result) {

        IMyStack<Vertex> stack = new MyArrayStack<>();
        result.addLast(first.id);
        stack.push(first);
        first.visited = true;

        while (!stack.isEmpty()) {
            Vertex v = stack.top();
            // find next
            Vertex next = null;
            for (Vertex tmp : v.adjVertexes) {
                if (!tmp.visited) {
                    next = tmp;
                    break;
                }
            }
            if (next == null) {
                stack.pop();
            } else {
                result.addLast(next.id);
                stack.push(next);
                next.visited = true;
            }
        }

        return result;
    }

    @Override
    public void bfs() {
        IMyList<String> result = new MyArrayList<>(vertexes.size());
        IMyQueue<Vertex> queue = new MyArrayCircleQueue<>(vertexes.size());
        queue.enqueue(vertexes.getFirst());
        while (!queue.isEmpty()) {
            Vertex v = queue.dequeue();
            if (v.visited) {
                continue;
            }
            // do-something
            result.addLast(v.id);
            v.visited = true;
            for (Vertex next : v.adjVertexes) {
                if (!next.visited) {
                    // if to decrease visited vertex and enqueue-dequeue op
                    queue.enqueue(next);
                }
            }
        }
        System.out.println(result.string());
    }

    public static void main(String[] args) {
        // init
        IMyList<Edge> edges = new MyArrayList<>(16);
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        Vertex v3 = new Vertex("v3");
        Vertex v4 = new Vertex("v4");
        Vertex v5 = new Vertex("v5");
        Vertex v6 = new Vertex("v6");
        Vertex v7 = new Vertex("v7");
        edges.addLast(new Edge(v1, v2));
        edges.addLast(new Edge(v1, v3));
        edges.addLast(new Edge(v1, v4));
        edges.addLast(new Edge(v2, v4));
        edges.addLast(new Edge(v2, v5));
        edges.addLast(new Edge(v3, v6));
        edges.addLast(new Edge(v4, v3));
        edges.addLast(new Edge(v4, v6));
        edges.addLast(new Edge(v5, v4));
        edges.addLast(new Edge(v5, v7));
        edges.addLast(new Edge(v7, v6));

        IMyAdjGraph graph = new MyAdjGraph(edges);
//        System.out.println("top-sort");
//        graph.topSort();
        System.out.println("dfs");
        graph.dfs();
//        System.out.println("bfs");
//        graph.bfs();
//        System.out.println("shorted-path");
    }

}
