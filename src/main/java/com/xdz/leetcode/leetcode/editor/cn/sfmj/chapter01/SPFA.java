package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * <pre>
 * 队列优化的Bellman-Ford算法
 *
 * 队列：普通队列 先进先出即可
 * 存放的是 当前已经与源点s连接的边集 在bellman-ford的内层循环 对edges的松弛操作中
 * 显而易见的 对于edge: u->v w来说 如果u点从s点不可达(目前) 那么这次松弛操作无效
 * 所以需要一个集合标记 当前所有从s点可达(按照边)的点 这可以用bfs队列来做
 *
 * 算法：
 * 1. dis[s] = 0 queue.add(s)
 * 2. 从queue中pop()处一个点 这个点肯定是从s可达的
 * 3. 遍历s的出边 松弛它 将出边的终点v加入queue（注意 这里v可能会重复入队 但是不可能同时出现在队列中 todo）
 *
 * 数据结构设计：
 * 算法中要求知道s的出边 因此使用邻接表存储
 *
 * 算法复杂度分析：
 * 点s的出边数量为O(s的出度 上限为n) 这个计算将会重复k次(s 入队的次数)
 * 考虑k的上限 为 s的入度 因为只有通过其他点达到它 才可能会入队
 * 所以复杂度的上限为 O((出度 * 入度)) = O(n * n) 朴素为O(k * n), k < e
 *
 * 负权回路检测：
 * 一个点最多入队的次数 为 s的入度 上限为
 * </pre>
 */
public class SPFA {
    private static final int MAX = 0x3f3f3f3f;

    int n;
    Edge[] edges;

    private static class Edge {
        int v = -1;
        int w;
        Edge next;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
        }
    }

    public SPFA(int n, int[][] edgeArray) {
        this.n = n;
        int e = edgeArray.length;
        this.edges = new Edge[e];
        for (int i = 0; i < e; i++) {
            int u = edgeArray[i][0], v = edgeArray[i][1], w = edgeArray[i][2];
            addEdge(u, v, w);
        }
    }

    private void addEdge(int u, int v, int w) {
        Edge edge = new Edge(v, w);
        edge.next = edges[u];
        edges[u] = edge;
    }

    public int[] spfa(int s) {
        int[] dis = new int[n];
        Queue<Integer> queue = new LinkedList<>();
        boolean[] inQue = new boolean[n];
        int[] count = new int[n];

        Arrays.fill(dis, MAX);
        dis[s] = 0;
        queue.add(s);
        inQue[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            inQue[u] = false;
            if (count[u]++ >= n) {
                // 存在负权路径
                return null;
            }
            // 遍历u的邻接链表 松弛之
            for (Edge e = edges[u]; e != null; e = e.next) {
                int v = e.v, w = e.w;
                if (dis[v] > dis[u] + w) {
                    dis[v] = dis[u] + w;
                }
                if (!inQue[v]) { // 如果v已经在队列中 那么没有必要进入
                    queue.offer(v);
                    inQue[v] = true;
                }
            }
        }

        return dis;
    }

    public static void main(String[] args) {
        int n = 4;
//        int[][] edges = {
//                {0, 1, 9}, {0, 2, 3}, {1, 2, -7}, {2, 3, 5}
//        };
        int[][] edges = {{0, 1, 9}, {0, 2, 3}, {1, 2, -7}, {2, 3, 5}, {3, 1, 1}};

        SPFA spfa = new SPFA(n, edges);
        int[] dis = spfa.spfa(0);
//        int[] dis = new BellmanFord(n, edges).bellmanFord(0);
        if (dis == null) {
            System.out.println("负权路径");
        } else {
            System.out.println(Arrays.toString(dis));
        }
    }
}
