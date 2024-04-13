package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * <pre>
 * 拓扑排序 有向图
 *
 * 算法思想：点的入度的
 * 1. 找到入度为0的点
 * 2. 将他从图中删去 更新点的入度 从新得到的入度为0的点继续处理
 * 3. 直到 图的所有顶点都经过了拓扑排序：排序后的顶点集合==图的顶点
 *
 * 数据结构设计：
 * 1. 快速计算点的入度：di[]数组存储
 * 2. 删除该点从而更新其他点的入度 => 该点的邻接点 => 点 邻接集合
 * 3. 当前入度为0的点 需要依次处理 => 队列
 * </pre>
 */
public class TopoLogical {
    private static final int MAX = 0x3f3f3f3f;

    int n;
    List<Integer>[] adjVer; // adjVer[u] u节点的邻接点集合

    public TopoLogical(int n, int[][] g) {
        this.n = n;
        this.adjVer = new List[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (g[i][j] != MAX) {
                    addEdge(i, j);
                }
            }
        }
    }

    // add u->v
    public void addEdge(int u, int v) {
        List<Integer> vers = adjVer[u];
        if (vers == null) {
            vers = new ArrayList<>();
            adjVer[u] = vers;
        }
        vers.add(v);
    }

    public List<Integer> topological() {
        int[] di = new int[n];
        // 统计入度
        for (List<Integer> vers : adjVer) {
            if (vers != null) {
                for (Integer v : vers) {
                    di[v]++;
                }
            }
        }
        // 入度为0的点的队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i : di) {
            if (i == 0) {
                queue.offer(i);
            }
        }

        List<Integer> ans = Lists.newArrayList();
        while (!queue.isEmpty()) {
            Integer u = queue.poll();
            // 出队的顺序 就是入队的顺序 就是入度为0的发生的时机的顺序
            ans.add(u);

            // 删除这个点 修改它的邻接点集合入度
            if (adjVer[u] != null) {
                for (Integer v : adjVer[u]) {
                    di[v]--;
                    if (di[v] == 0) {
                        queue.offer(v);
                    }
                }
            }
        }
        return ans;
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

        TopoLogical obj = new TopoLogical(N, g);
        List<Integer> topological = obj.topological();
        System.out.println(Arrays.toString(topological.toArray(new Integer[]{})));
    }
}
