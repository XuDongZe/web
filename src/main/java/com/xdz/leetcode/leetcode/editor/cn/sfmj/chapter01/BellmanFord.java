package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.Arrays;

/**
 * <pre>
 * 贝尔曼-福特 单源最短路径算法
 *
 * 可以处理负权路径的问题 可以检测负权环
 *
 * 思想：通过边的松弛
 * 假设有一条边{begin, end, weight}使得dis[begin] + weight < dis[end] 则更新dis
 * [end] = dis[begin] + weight
 *
 * 因此 通过枚举所有的边 对其进行松弛操作一次 就可以得到从源点s出发 经过1条边的点的最短距离
 * 遍历松弛2次 就可以得到经过2条边的点的最短距离
 *
 * 那么需要遍历多少次呢？因为假设没有环 因此从源点s出发 最远的点 与s之间的边的数量 为n-1。
 * 因此只需要枚举n-1次。
 *
 * 优化：
 * 1. 在中途枚举边时最短路径图就已经不变了 说明已经得到结果
 * 2. 队列优化：第一次枚举时 很多后面的点由于前面的点的距离是MAX 所以无法处理；
 * 因此可以使用BFS 按照队列处理：队列中的边 是全部已经链接到源点s的（非MAX）但是它不一定是最短距离
 * 松弛操作时 只能使用队列中已知距离的边做中转
 * 第2点就是SPFA算法的思想
 *
 * 如何发现负权边？
 * 如果没有负权边 那么n-1次松弛遍历操作后 最短路径图一定会固定下来
 * 那么只需要查看第n次松弛时 是否还有修改 如果有 那么存在负权边
 *
 * 复杂度分析
 * 1. 外层循环 n-1 固定 O(n)
 * 2. 内层循环 遍历所有边 O(e)
 * 因此为 O(n * e)
 *
 * 只要复杂度带了e 那么使用于 稀疏图。因为稠密图的e == O(n^2)
 *
 * 数据结构设计：
 * 通过算法可以知道 主要是对边进行遍历 因此使用 边集数组来表示
 *
 * 思考图的常见操作：
 * 1. u->v是否直连？权重多少？
 * 2. u的所有邻接点/边
 * 3. u的出度、入度
 *
 * 图的3种常见表示方法：
 * 1. 邻接矩阵 适用于稠密图
 *  1. 空间O(n ^ 2)
 *  2. g[u][v] == w O(1)
 *  3. u的edges 需要遍历判断 O(n)
 * 2. 邻接表 适用于稀疏图
 *  1. 空间O(e)
 *  2. g[u][v] == w ? O(u的出度) 需要遍历
 *  3. u的edges 直接获取 O(u的出度)
 * 3. 边集数组 适用于稀疏图 是邻接表的简单版本(不带u的划分 只有一个大数组即可 简单)
 *  1. 空间O(e)
 *  2. g[u][v] == w ? O(e) 需要遍历边集数组
 *  3. u的edges O(e)
 * </pre>
 */
public class BellmanFord {
    private static final int MAX = 0x3f3f3f3f;

    int n;
    int e;
    int[][] edges; // {from, to, w}

    public BellmanFord(int n, int[][] edges) {
        this.n = n;
        this.e = edges.length;
        this.edges = edges;
    }

    // 源点s
    public int[] bellmanFord(int s) {
        int[] dis = new int[n];
        Arrays.fill(dis, MAX);
        dis[s] = 0;

        // 松弛n-1次
        for (int i = 1; i < n; i++) {
            // 遍历所有边 松弛
            if (!trySongchi(dis)) {
                // 优化 提前结束 后续状态不会再改变了
                break;
            }
        }

        // 检测负权路径
        // 遍历所有边 松弛
        if (trySongchi(dis)) {
            return null;
        }
        return dis;
    }

    private boolean trySongchi(int[] dis) {
        boolean change = false;
        for (int j = 0; j < e; j++) {
            int u = edges[j][0], v = edges[j][1], w = edges[j][2];
            if (dis[v] > dis[u] + w) {
                dis[v] = dis[u] + w;
                // 负权路径肯定包含v 那么从v->v就是一条可求的负权路径
                change = true;
            }
        }
        return change;
    }

    public static void main(String[] args) {
        int n = 4;
//        int[][] edges = {
//                {0, 1, 9}, {0, 2, 3}, {1, 2, -7}, {2, 3, 5}
//        };
        int[][] edges = {{0, 1, 9}, {0, 2, 3}, {1, 2, -7}, {2, 3, 5}, {3, 1, 1}};

        BellmanFord bellmanFord = new BellmanFord(n, edges);
        int[] dis = bellmanFord.bellmanFord(0);
        if (dis == null) {
            System.out.println("负权路径");
        } else {
            System.out.println(Arrays.toString(dis));
        }


    }
}
