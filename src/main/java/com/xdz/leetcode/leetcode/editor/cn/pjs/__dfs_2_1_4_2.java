package com.xdz.leetcode.leetcode.editor.cn.pjs;

/**
 * Description: Lake Counting POJ No.2386<br/>
 * Author: dongze.xu<br/>
 * Date: 2024/3/26 22:49<br/>
 * Version: 1.0<br/>
 */
public class __dfs_2_1_4_2 {
    char a[][];
    boolean visited[][];
    int r[][];

    /**
     * a是一个N*M的二维数组 a[i][j] == '.'表示没有积水 'W'表示积水
     *
     * 计算一共有多少个连通的水洼 水洼是八个方向连通的
     */
    public int solve(char a[][]) {
        this.a = a;
        visited = new boolean[a.length][a[0].length];
        r = new int[a.length][a[0].length];
        for (int i = 0; i < a.length; i ++) {
            for (int j = 0; j < a[0].length; j ++) {
                r[i][j] = -1;
            }
        }

        int res = 0;
        for (int i = 0; i < a.length; i ++) {
            for (int j = 0; j < a[0].length; j ++) {
                if (!visited[i][j] && a[i][j] == 'W') {
                    int root = i * a[0].length + j;
                    dfs(i, j, root);
                    res++;
                }
            }
        }
        return res;
    }

    /**
     * 从a[i][j]开始 遍历最大连通图
     */
    void dfs(int i, int j, int root) {
        r[i][j] = root;
        visited[i][j] = true;

        // 遍历8个方向
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // 跳过自己
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int nx = i + dx, ny = j + dy;
                if (nx >= 0 && nx < a.length && ny >= 0 && ny < a[0].length) {
                    // 如果下一个位置也是水洼 并且没有访问过
                    if (!visited[nx][ny] && a[nx][ny] == 'W') {
                        dfs(nx, ny, root);
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        String[] str = new String[] {
                "W........WW.",
                ".WWW.....WWW",
                "....WW...WW.",
                ".........WW.",
                ".........W..",
                "..W......W..",
                ".W.W.....WW.",
                "W.W.W.....W.",
                ".W.W......W.",
                "..W.......W."
        };
        char[][] a = new char[str.length][str[0].length()];
        for (int i = 0; i < a.length; i ++) {
            a[i] = str[i].toCharArray();
        }

        __dfs_2_1_4_2 obj = new __dfs_2_1_4_2();
        System.out.println(obj.solve(a)); // 3
    }
}
