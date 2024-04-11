package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * <pre>
 * 最小生成树 prim算法 不能处理带有负权路径的 基于贪心思想
 *
 * 图是 无向的 连通的（当然不连通的话 可以再加一层循环获得多颗MST 为了简便讨论强连通图）
 * 没有负权路径
 *
 * 算法：
 * 定义横切边集合：使用在MST中的节点集合 划分
 * 不失一般性的 可以初始化 mst-v-set = {0} 横切边集合trans-edges = 0的邻接表
 *
 * 每次 从横切边集合中 选择最小的边e 他就是MST的边
 *
 * e的end点v的邻接链表 加入横切边集合
 *
 * 数据结构：
 * 横切边集合 支持选择最小边 显然使用priority queue
 * MST 顶点集合 boolean select[] 标记
 * 图表示 邻接链表 便于取u的邻接边集合
 *
 * 复杂度分析：
 * 队列中入队的次数是e 显然每条边都会入队一次
 *  确定一条边时需要调整堆 O(log e) 堆中的元素最大数量不超过e
 * 因此为O(e * log e)
 *
 * 空间复杂度：
 * select[]标记 O(n)
 * queue e条边 O(e)
 * 因此为O(n + e)
 *
 * 证明之：由于没有负权边 显然u的邻接表中权重最小的边 就是连接u到其他点最小的边 因为通过其他点中转会更大
 * 因此这个边就是将u连接到MST中的边
 * </pre>
 */
public class Prim {
    private static final int MAX = 0x3f3f3f3f;

    int n;
    Edge[] edges;

    private static class Edge {
        int u;
        int v;
        int w;
        Edge next;

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    public Prim(int n, int[][] g) {
        this.n = n;
        this.edges = new Edge[n];
        for (int u = 0; u < n; u ++) {
            for (int v = 0; v < n; v ++) {
                if (g[u][v] != MAX) {
                    addEdge(u, v, g[u][v]);
                }
            }
        }
    }

    private void addEdge(int u, int v, int w) {
        Edge edge = new Edge(u, v, w);
        edge.next = edges[u];
        edges[u] = edge;
    }

    // 返回边集数组表示的图
    public Edge[] prim() {
        Edge[] mst = new Edge[n - 1];
        // mst的顶点集合 快速确定u是否在mst中
        boolean[] select = new boolean[n];
        // 横切边集合 优先队列 便于查找最小横切边
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o.w));

        int idx = 0;
        visit(select, queue, 0);
        while (!queue.isEmpty()) {
            Edge e = queue.poll();
            int u = e.u, v = e.v;
            // 失效的横切边
            if (select[u] && select[v]) continue;
            mst[idx++] = e;

            // 使用没有在mst中的节点
            visit(select, queue, select[u] ? v : u);
        }
        return mst;
    }

    private void visit(boolean[] select, PriorityQueue<Edge> queue, int u) {
        select[u] = true;
        for (Edge e = edges[u]; e != null; e = e.next) {
            queue.offer(e);
        }
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

        for (int i = 0; i < N; i ++) {
            for (int j = 0; j < N; j ++) {
                if (g[i][j] != MAX) {
                    g[j][i] = g[i][j];
                } else if (g[j][i] != MAX) {
                    g[i][j] = g[j][i];
                }
            }
        }

        Prim prim = new Prim(N, g);
        Edge[] edges = prim.prim();

        int sum = 0;
        for (Edge edge : edges) {
            sum += edge.w;
            System.out.printf("%d->%d, %d%n", edge.u, edge.v, edge.w);
        }
        System.out.println("total: " + sum);
    }
}
