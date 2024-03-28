package com.xdz.leetcode.leetcode.editor.cn.gragh;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * <pre>
 * 计算图G中 任意两个点u->v的最短距离
 *
 * 算法思想
 * 要想使 u->v 的距离变小 唯一的办法是找到一个中转点k 使u->v的距离 > u->k->v的距离
 * 因此想要找到u->v的最短距离 需要枚举所有的节点k(除u v之外的) 如果u->v > u->k->v 那么更新u->v的距离
 * 当所有的节点k枚举完后 u->v的最短距离就确定了
 *
 * 证明
 * 假设 当所有的节点k枚举完后 u->v的距离仍然会继续变短
 * 假设u->v的距离会变短 那么一定使u通过中转的节点k到达了v 但是所有的k都已经枚举完了 所以这个k一定是已经被枚举过的值
 * 所以u->v的距离无法变得更短了
 *
 * 负权边
 * 如果a->b是一条负权边 而其他都是正权边 那么a点通过b做中转点 到达其他点 一定小于a直接到其他点。
 * floyed算法在执行过程中 处理 a=>v时 也会枚举到b点做中转点 此时一定会更新a=>v == a=>b=>v
 * 因此可以处理
 *
 * 单源
 * floyed是对于整个图的静态分析 不能处理动态加入的图 (动态加入的顶点、边)
 * 需要将所有u->v的距离进行整体迭代
 *
 * 时间复杂度
 * O(n^3) = 枚举k * 枚举u->v
 *
 * 暴力枚举
 *
 * </pre>
 */
public class Floyed {

    final static double MAX = 0x3f3f3f3f;
    int n; // n个顶点 从1到n
    double[][] dis; // 最短距离邻接矩阵  dis[i][j] = d: 表示i->j间的最短距离是d

    public Floyed(int n, double[][] dis) {
        this.n = n;
        this.dis = dis;

        floyed();
    }

    /**
     * 核心代码 5行
     * 简单粗暴 暴力枚举
     */
    private void floyed() {
        // 枚举中转点k
        for (int k = 0; k < n; k++)
            // 枚举u->v
            for (int u = 0; u < n; u++)
                for (int v = 0; v < n; v++) {
                    // 如果能通过中转点k 使得u->v的距离变短
                    if (dis[u][v] > dis[u][k] + dis[k][v]) {
                        dis[u][v] = dis[u][k] + dis[k][v];
                    }
                }
    }

    public double shortedPath(int u, int v) {
        return dis[u][v];
    }

    /**
     * inputStream 格式
     * n n个点 约定点的id从1到n
     * 接下来n行 为每个点的坐标 x y
     * m m条连线 无向边
     * 接下来m行 为每个连线连接的点 u v
     * k k个询问
     * 接下来k行 为给定求解的最短路径的参数 u v
     */
    public static void main(String[] args) throws IOException {
        int n, m, k; // n个点 m个连线 k个询问
        int[][] p; // n个点的坐标 p[i][0] == point[i].x, p[i][1] == point[i].y
        double[][] matrix; // 邻接矩阵表示

        // 数据预处理
        FileInputStream fis = new FileInputStream("src/main/java/com/xdz/leetcode/leetcode/editor/cn/gragh/floyed.txt");
        Scanner scanner = new Scanner(fis);
        // n个点
        n = scanner.nextInt();
        p = new int[n][2];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt(), y = scanner.nextInt();
            p[i][0] = x;
            p[i][1] = y;
        }
        // 初始化matrix
        matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], Floyed.MAX);
            matrix[i][i] = 0;
        }
        // m条边
        m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt() - 1; // -1转化为程序0下标
            int v = scanner.nextInt() - 1;
            double distance = Math.sqrt(Math.pow(p[u][0] - p[v][0], 2) + Math.pow(p[u][1] - p[v][1], 2));
            matrix[u][v] = matrix[v][u] = distance;
        }

        Floyed floyed = new Floyed(n, matrix);
        // k个询问
        k = scanner.nextInt();
        for (int i = 0; i < k; i++) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            System.out.println(floyed.shortedPath(u, v));
        }
    }
}
