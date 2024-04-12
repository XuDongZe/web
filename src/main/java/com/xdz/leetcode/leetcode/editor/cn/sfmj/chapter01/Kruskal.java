package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import com.google.common.collect.Lists;
import org.checkerframework.checker.units.qual.K;
import org.python.modules.itertools.count;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 * 克鲁斯卡尔算法 最小生成树
 *
 * 算法：
 * 1. 将边 按照权重从小到大 排序
 * 2. 选择一条最短的边E: u->v 看看如果将它添加到mst中 是否会构成环 如果不会构成环 那么有：
 * 它定义了一个顶点的切分集合：{u和其他点} {v和其他点} 而E是一条横切边 这条边又是最小的边 所以它属于mst
 * 3. 当选择n-1条边之后 mst完成
 *
 * 算法易懂 但比较难实现：
 * 数据结构设计：
 * 1. 需要将边排序
 * 2. 需要知道将边E: u->v加入mst后 是否会构成环。推导一下：如果u->v已经连通 那么加入直连边E 一定会构成环。
 * 所以问题等价于u->v是否连通 => 并查集
 *
 * 复杂度分析：
 * 最小生成树n个顶点 n-1条边 每次选择一条边，每次选择时需要使用并查集判断u->v连通性 O(log n)
 * 因此为：n * log(n)
 * </pre>
 */
public class Kruskal {

    private static final int MAX = 0x3f3f3f3f;

    private static class Edge {
        int u;
        int v;
        int w;

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

    }

    public Edge[] kruskal(int n, Edge[] edges) {
        int idx = 0;
        Edge[] mst = new Edge[n - 1];
        int[] parent = new int[n];
        int[] count = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            count[i] = 1;
        }

        Arrays.sort(edges, Comparator.comparingInt(o -> o.w));
        for (Edge e : edges) {
            int u = e.u, v = e.v;
            if (find(u, parent, count) == find(v, parent, count)) {
                continue;
            }
            mst[idx++] = e;
            if (idx == mst.length) {
                break;
            }
            union(u, v, parent, count);
        }
        return mst;
    }

    private int find(int i, int[] parent, int[] count) {
        if (i == parent[i]) {
            return i;
        } else {
            int root = find(parent[i], parent, count);
            // 路径压缩
            parent[i] = root;
            count[root]++;
            return root;
        }
    }

    private void union(int a, int b, int[] parent, int[] count) {
        int ra = find(a, parent, count);
        int rb = find(b, parent, count);
        // 大鱼吃小鱼
        if (count[ra] > count[rb]) {
            parent[rb] = ra;
            count[ra] += count[rb];
            count[rb] = 0;
        } else {
            parent[ra] = rb;
            count[rb] += count[ra];
            count[ra] = 0;
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

        // 邻接矩阵 转成 边集数组
        List<Edge> edges = Lists.newArrayList();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (g[i][j] != MAX) {
                    edges.add(new Edge(i, j, g[i][j]));
                }
            }
        }

        Kruskal obj = new Kruskal();
        Edge[] mst = obj.kruskal(N, edges.toArray(new Edge[]{}));
        for (Edge edge : mst) {
            System.out.printf("%s -> %s, %s\n", edge.u, edge.v, edge.w);
        }
    }
}
