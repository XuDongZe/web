package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * <pre>
 * 堆优化
 * 需要使用邻接表表示
 *
 * 复杂度分析
 * 1. 每次poll一个节点u 确定它的最短路径 一共poll n次 每次涉及堆的调整log n 因此为O(n * log n)
 * 2. 修改节点u相邻的点的dis 相邻的点的数量 是这个点的出度 而一张图中所有节点的出度就是边的数量 e
 * 每次修改 会将修改后的权值 对堆进行调整 每次的复杂度是O(log n)
 *
 * 因此整体复杂度为 O(n * log n) + O(e * log n) = O((e + n) * log n)
 *
 * 适用于 稀疏图。也就是边比较少。 如果e == n 则为n * log n复杂度
 * 比未优化的n^2级别的复杂度好很多。
 * 这主要是因为 稀疏图中 n^2对节点遍历 有很多无用的操作：
 * 因为邻接矩阵中u->v 大部分情况不存在边 是一个无效判断
 * </pre>
 */
public class DijkstraHeap {
    private static final int MAX = 0x3f3f3f3f;

    private int n;
    private Edge[] edges; // 邻接表 edges[i]表示节点i的邻接点

    private static class Edge {
        int v; // 终点节点
        int w; // 权值
        Edge next;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
        }
    }

    private static class Node {
        int u, w;

        public Node(int u, int w) {
            this.u = u;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return u == node.u;
        }

        @Override
        public int hashCode() {
            return Objects.hash(u);
        }
    }

    public DijkstraHeap(int n, int[][] g) {
        this.n = n;
        this.edges = new Edge[n]; // all null for init
        // 转成edges表示
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (g[i][j] != MAX) {
                    addEdge(i, j, g[i][j]);
                }
            }
        }
    }

    // 给定源点s dis[i]为距离s的最短路径
    public int[] dijkstra(int s) {
        int[] dis = new int[n];
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o.w));

        Arrays.fill(dis, MAX);
        dis[s] = 0;
        queue.add(new Node(s, 0));
        while (!queue.isEmpty()) {
            // 距离s最近的节点 弹出 此时dis[e.v]已经被确定 因为不会再有距离更短的中转点了
            Node e = queue.poll();
            // 修改e的相邻的点
            for (Edge ne = edges[e.u]; ne != null; ne = ne.next) {
                if (dis[ne.v] > dis[e.u] + ne.w) {
                    queue.remove(new Node(ne.v, dis[ne.v]));
                    dis[ne.v] = dis[e.u] + ne.w;
                    queue.add(new Node(ne.v, dis[ne.v]));
                }
            }
        }

        return dis;
    }

    // u->v 权重为w
    private void addEdge(int u, int v, int w) {
        // 头插法
        Edge edge = new Edge(v, w);
        edge.next = edges[u];
        edges[u] = edge;
    }

    public static void main(String[] args) {
        int N = 6;
        int[][] g = {
                {MAX, 1, 3, MAX, MAX, MAX},
                {MAX, MAX, 1, 4, 2, MAX},
                {MAX, MAX, MAX, 5, 5, MAX},
                {MAX, MAX, MAX, MAX, MAX, 3},
                {MAX, MAX, MAX, 1, MAX, 6},
                {MAX, MAX, MAX, MAX, MAX, MAX}
        };

        DijkstraHeap obj = new DijkstraHeap(N, g);
        int[] dis = obj.dijkstra(0);
        for (int d : dis) {
            System.out.println(d);
        }
    }
}
