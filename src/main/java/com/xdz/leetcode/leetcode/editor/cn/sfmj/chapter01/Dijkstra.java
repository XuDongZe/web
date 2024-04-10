package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.Arrays;

/**
 * <pre>
 * 迪杰斯特拉算法 解决单源最短路径问题
 *
 * 问题：从给定点出发（不失一般性 用节点1来替代）到达所有其他点的最短路径为？
 *
 * 思想：
 * 从给定点s到点u的路径会变短 只可能通过其他点v中转
 * 贪心
 *
 * 算法：
 * 定义dis[u]为给定点s到u的最短路径 初始化 dis[i] 1->u = INF
 * flag[u] = true 表示点u到s点的最短距离已经找到 初始化 flag[i] 1->u = false
 * 初始化 dis[s] = 0
 *
 * 1. 找到flag[i] == false集合中 dis[i]的最小值 记录u = i
 * 2. 标记u为true
 * 3. 使用u为中转节点 尝试将dis[v]变小 v为u的邻接点集合
 *
 * 复杂度分析：n个点 m条边 不失一般性 假设为连通图
 * 邻接矩阵存储
 * 1. 两层循环 外层每次标记一个点 所以为n
 * 2. 内层循环
 *  1. 每次从未被标记的集合中找到 dis[i]的最小值 n
 *  2. 内层循环 更新邻接点的距离 n (因为一个点的邻接点最多是n)
 * n * (n + n) = 2* n^2
 * 分析：复杂度n^2 与m无关 因此适合m较大 n较小的情况 稠密图(m == n^2)
 *
 * 优化
 * 1. 外层循环不变 仍然是贪心到所有n个点全部被标记位置 O(n)
 * 2. 内层循环
 *  1. 从未被标记的集合中找最值 可以用堆来优化 => pop一个最小值节点u O(log n)
 *  2. 更新u的邻接点的距离时 也需要调整堆 log(n) todo
 * 分析：复杂度 n log n + e* log n = (n + e)log n 使用于稀疏图：比如 e == n
 * 注意稠密图 e最大n^2级别
 *
 * 证明之：
 * 这是一个贪心的思想。需要证明dis[u]在算法结束后是最小路径值
 * 当dis[u]被标记为true之后 意味着dis[u]已经是最小值了 dis[u]就不会再被更新了
 * 反证：
 * 假设dis[u]还会变小 也就是有一个此时还没有被标记的中转点k 让s通过k中转到达u的距离 < dis[u]
 * 但由于没有负权路径 所以不可能
 *
 * 缺点：
 * 不可以处理存在负权路径的图
 * </pre>
 */
public class Dijkstra {
    private static final int MAX = 0x3f3f3f3f;

    private int n;
    private int s;
    private int[][] g;
    private int[] dis;

    public Dijkstra(int n, int[][] g) {
        this.n = n;
        this.g = g;
    }

    /**
     * 计算单源最短路径
     * dis[i]为i节点到s节点的最短路径
     */
    public int[] dijkstra(int s) {
        boolean[] flag = new boolean[n];
        int[] dis = new int[n];

        Arrays.fill(dis, MAX);
        dis[s] = 0;

        for (int i = 0; i < n; i++) { // 所有节点
            int v = -1, min = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!flag[j] && min > dis[j]) {
                    min = dis[j];
                    v = j;
                }
            }
            if (v == -1) {
                break;
            }

            flag[v] = true;
            for (int j = 0; j < n; j++) {
                if (g[v][j] != MAX && dis[j] > dis[v] + g[v][j]) {
                    dis[j] = dis[v] + g[v][j];
                }
            }
        }

        return dis;
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

        Dijkstra obj = new Dijkstra(N, g);
        int[] dis = obj.dijkstra(0);
        for (int d : dis) {
            System.out.println(d);
        }
    }
}
