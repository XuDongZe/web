package com.xdz.dsa.graph;

import com.xdz.dsa.Queue.IMyQueue;
import com.xdz.dsa.Queue.MyArrayCircleQueue;
import com.xdz.dsa.Queue.MyArrayQueue;
import com.xdz.dsa.heap.IMyHeap;
import com.xdz.dsa.heap.MyHeap;
import com.xdz.dsa.list.IMyList;
import com.xdz.dsa.list.MyArrayList;
import com.xdz.dsa.stack.IMyStack;
import com.xdz.dsa.stack.MyArrayStack;
import javafx.scene.shape.Arc;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;

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
        // 头插法
        edge.to.arc = edge.from.arc;
        edge.from.arc = new ArcNode(edge.to, edge.weight);
        edge.from.outDegree++;
        edge.to.inDegree++;
        if (!vertexes.contains(edge.from)) {
            vertexes.addLast(edge.from);
        }
        if (!vertexes.contains(edge.to)) {
            vertexes.addLast(edge.to);
        }
    }

    @Override
    public void topSort() {
        IMyList<String> result = topSortV1();
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
            for (Vertex adj : v) {
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
            for (Vertex adj : v) {
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
            for (Vertex next : v) {
                if (!next.visited) {
                    // if to decrease visited vertex and enqueue-dequeue op
                    queue.enqueue(next);
                }
            }
        }
        System.out.println(result.string());
    }

    // todo
    @Override
    public void shortedPath(Vertex v) {
        dijkstra(v);
    }

    //////////////////
    /////// inner func
    //////////////////

    private void dfsRecursive(Vertex v, IMyList<String> result) {
        if (v == null || v.visited) {
            return;
        }
        result.addLast(v.id);
        v.visited = true;
        for (Vertex next : v) {
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
            for (Vertex tmp : v) {
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

    /**
     * let graph split to 2 set:
     * first is P, include vertexes that shorted-path from v is known.
     * second is Q which stp(v, el) is un-known.
     * init: P = {v}
     * loop:
     * 1. choose next from v that p(v, next) is min.
     * 2. add next to P: P = {v, next}
     * 3. change p(v, t) = min(p(v, t) + p(v, next) + p(next, t)), for t in next.adjList.
     * for 1. we use a min-heap, which value is vertex and compareTo depends on p(v, t).
     * so min-heap init: v.adjList.
     * min-heap iterate: when we got a new p(v, t), we add the vertex and the p to min-heap.
     */
    private void dijkstra(Vertex v) {
        // malloc topNo
        topSort();

        // init weight[][]
        int[][] dp = new int[vertexes.size()][vertexes.size()];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = new int[vertexes.size()];
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }

        for (Vertex t : vertexes) {
            if (t.arc != null) {
                dp[t.topNo][t.arc.v.topNo] = t.arc.weight;
            }
            dp[t.topNo][t.topNo] = 0;
        }

        IMyHeap<ArcNode> minHeap = new MyHeap<>();
        minHeap.insert(v.arc);

        while (!minHeap.isEmpty()) {
            Vertex mid = minHeap.pop().v;
            // process all mid 's arc node: we now know mid so maybe dp[v][mid.arc.v] will be less. update it.
            for (Vertex t : v) {
                int tmp = dp[v.topNo][mid.topNo] + dp[mid.topNo][t.topNo];
                if (tmp < dp[v.topNo][t.topNo]) {
                    dp[v.topNo][t.topNo] = tmp;
                    minHeap.insert(new ArcNode(t, tmp));
                }
            }
        }

        for (Vertex t : vertexes) {
            System.out.printf("[%s, %s] = %s\n", v.id, t.id, (dp[v.topNo][t.topNo] == Integer.MAX_VALUE ? "INF" : dp[v.topNo][t.topNo]));
        }
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
        System.out.println("top-sort");
        graph.topSort();
//        System.out.println("dfs");
//        graph.dfs();
//        System.out.println("bfs");
//        graph.bfs();
//        System.out.println("shorted-path");
//        graph.shortedPath(v1);
    }

}
