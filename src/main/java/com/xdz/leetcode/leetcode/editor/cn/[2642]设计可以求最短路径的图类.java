package com.xdz.leetcode.leetcode.editor.cn;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//leetcode submit region begin(Prohibit modification and deletion)
class Graph {
    // n个顶点
    int n;
    // 邻接矩阵
    int[][] matrix;
    // 最短路径
    // shortedPath[i] == 从i节点出发的
    int[][] dis;

    public Graph(int n, int[][] edges) {
        this.n = n;
        this.matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], Integer.MAX_VALUE);
            matrix[i][i] = 0;
        }

        dis = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dis[i], Integer.MAX_VALUE);
            dis[i][i] = 0;
        }

        for (int[] edge : edges) {
            addEdge(edge);
        }
    }


    public void addEdge(int[] edge) {
        // from -> to == weight
        int from = edge[0];
        int to = edge[1];
        int weight = edge[2];
        matrix[from][to] = weight;
    }

    public int shortestPath(int node1, int node2) {
        dijkstra(node1);
        return dis[node1][node2] == Integer.MAX_VALUE ? - 1 : dis[node1][node2];
    }

    private static class QueueNode {
        int v;
        int w;
    }

    // 寻找单源路径最小值
    private void dijkstra(int node) {
        // visited nodes
        Set<Integer> visited = new HashSet<>();
        dis[node][node] = 0;

        while (visited.size() < n) {
            // 找到距离node最近的 未被访问过的顶点
            int u = -1;
            int minDis = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!visited.contains(i) && dis[node][i] < minDis) {
                    minDis = dis[node][i];
                    u = i;
                }
            }

            if (u == -1) {
                // 目前 没有一个点可以从node访问到了 比如这个图并不是一个连通图 就可能在visited元素数量 < n时在这里退出
                break;
            }

            // 找到顶点v 他的最小距离就是minDis
            visited.add(u);
            // 看看u的加入, 是否使u的邻接点v 到node的距离 变小
            for (int v = 0; v < n; v++) {
                if (matrix[u][v] != Integer.MAX_VALUE) {
                    dis[node][v] = Math.min(dis[node][v], dis[node][u] + matrix[u][v]);
                }
            }
        }
    }

    public static void main(String[] args) {
        Graph obj = new Graph(4, new int[][]{{0, 2, 5}, {0, 1, 2}, {1, 2, 1}, {3, 0, 3}});
        System.out.println(obj.shortestPath(3, 2));
        System.out.println(obj.shortestPath(0, 3));
        obj.addEdge(new int[]{1, 3, 4});
        System.out.println(obj.shortestPath(0, 3));
    }
}

/**
 * Your Graph object will be instantiated and called as such:
 * Graph obj = new Graph(n, edges);
 * obj.addEdge(edge);
 * int param_2 = obj.shortestPath(node1,node2);
 */
//leetcode submit region end(Prohibit modification and deletion)
