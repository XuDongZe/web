package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * dfs和bfs模板
 */
public class GraphGeneralSearch {

    private int n; // n个顶点
    private int[][] g; // 邻接矩阵表示

    public GraphGeneralSearch(int n, int[][] g) {
        this.n = n;
        this.g = g;
    }

    public List<List<Integer>> dfs() {
        List<List<Integer>> ans = Lists.newArrayList();
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            List<Integer> list = Lists.newArrayList();
            if (!visited[i]) {
                __dfs(i, visited, list);
                ans.add(list);
            }
        }
        return ans;
    }

    private void __dfs(int u, boolean[] visited, List<Integer> ans) {
        ans.add(u);
        for (int v = 0; v < n; v ++) {
            if (!visited[v] && g[u][v] == 1) {
                visited[v] = true;
                __dfs(v, visited, ans);
            }
        }
    }

    public List<Integer> bfs() {
        return __bfs(0);
    }

    public List<Integer> __bfs(int u) {
        List<Integer> ans = Lists.newArrayList();
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(u);
        visited[u] = true; // 进队列就访问过了 而不是出队列 否则可能重复进队列

        while (!queue.isEmpty()) {
            Integer t = queue.poll();
            // 出队的顺序就是入队的顺序 也就是访问的顺序
            ans.add(t);
            for (int v = 0; v < n; v ++) {
                if (!visited[v] && g[t][v] == 1) {
                    queue.add(v);
                    visited[v] = true;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int N = 5;
        int[][] g = {
                {0, 1, 1, 0, 0},
                {0, 0, 0, 1, 1},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0}
        };

        GraphGeneralSearch obj = new GraphGeneralSearch(N, g);
        System.out.println(Arrays.toString(obj.bfs().toArray()));
        System.out.println(Arrays.toString(obj.dfs().get(0).toArray()));
    }
}
