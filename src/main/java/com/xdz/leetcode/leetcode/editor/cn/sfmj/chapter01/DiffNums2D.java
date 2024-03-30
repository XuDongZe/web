package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * <pre>
 * 二维差分
 *
 * 差分数组和前缀和数组 可以用一个数组来表示
 *
 * 二维数组a[][] 在左上角(x1,y1)到右下角(x2,y2)的平面上 所有的点格都+k
 *
 * 可以使用差分数组来表示二维数组 操作完毕后再转为前缀和数组即可
 *
 * 前缀和 和 差分 互逆运算 一体两面 是一个数组的两种表示方法
 * 差分更便于表示区间操作
 *
 * 定义a[i][j]原数组为d[i][j]差分数组的前缀和 也就是a[i][j]是以(0,0)为左上角 (i,j)为右下角的区域中
 * 所有d[x][y]的和
 *
 * 可以推得:
 * a[i][j] = a[i][j-1] + a[i-1][j] - a[i-1][j-1] + d[i][j] (将a转化为d的前缀和表示 容易证明)
 * 所以有:
 * d[i][j] = a[i][j] + a[i-1][j-1] - a[i][j-1] - a[i-1][j]
 *
 * 当d[i][j]+=k时 a[i][j]是第一个受影响的前缀和 注意到a[x][y]会被其左边和上边的元素影响 因此由a[i][j]+=k
 * 会导致a[i][j]右边、下边的元素都+=k
 *
 * 结论:
 * d[i][j] += k 意味着以(i,j)为左上角的区域中 a[x][y]+=k
 * 这就实现了 区间操作
 *
 * https://blog.csdn.net/Sommer001/article/details/121019319
 * </pre>
 */
public class DiffNums2D {
    boolean debug;
    private int[][] d; // 差分数组 (对于前缀和数组来说是差分数组)
    private int[][] origin; // 原数组 (对于差分数组来说是前缀和数组)

    public DiffNums2D(int[][] a) {
        this.origin = a;
        int m = a.length; // 行
        int n = a[0].length; // 列
        d = new int[m][n]; // d[i][j] = 0
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                __add(i, j, i, j, a[i][j]);
            }
        }
    }

    private void __add(int x1, int y1, int x2, int y2, int k) {
        d[x1][y1] += k; // (x1,y1)为左上角的区域 a[i][j]+=k
        if (x2 + 1 < d.length) {
            d[x2 + 1][y1] -= k;
        }
        if (y2 + 1 < d[0].length) {
            d[x1][y2 + 1] -= k;
        }
        if (x2 + 1 < d.length && y2 + 1 < d[0].length) {
            d[x2 + 1][y2 + 1] += k;
        }
    }

    /**
     * 对于原数组 从左上角(x1,y1)到右下角(x2,y2) 都+k
     */
    public void add(int x1, int y1, int x2, int y2, int k) {
        __add(x1, y1, x2, y2, k);

        if (debug) {
            bruteForceAdd(x1, y1, x2, y2, k);
            if (!compare()) {
                throw new RuntimeException("error: not equal with bruteForceAdd");
            }
        }
    }

    /**
     * 返回原数组(d[][]的前缀和数组)
     */
    public int[][] result() {
        int[][] a = new int[d.length][d[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                // 计算每一个a[i][j]
                // 递推关系是从左上 到 右下
                int left = j - 1 < 0 ? 0 : a[i][j - 1];
                int top = i - 1 < 0 ? 0 : a[i - 1][j];
                int lt = (i - 1 < 0 || j - 1 < 0) ? 0 : a[i - 1][j - 1];
                a[i][j] = left + top - lt + d[i][j];
            }
        }
        return a;
    }

    public void bruteForceAdd(int x1, int y1, int x2, int y2, int k) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                origin[i][j] += k;
            }
        }
    }

    private boolean compare() {
        int[][] a = result();
        if (a.length != origin.length || a[0].length != origin[0].length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] != origin[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * inputStream 格式
     * m n m为行数 n为列数
     * 接下来m行 每行n个数字 表示a[i][j]的值
     */
    public static void main(String[] args) throws FileNotFoundException {
        // 数据预处理
        FileInputStream fis = new FileInputStream("src/main/java/com/xdz/leetcode/leetcode/editor/cn/sfmj/chapter01/diffNums2d.txt");
        Scanner scanner = new Scanner(fis);
        int m = scanner.nextInt(), n = scanner.nextInt();
        int[][] origin = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                origin[i][j] = scanner.nextInt();
            }
        }

        DiffNums2D diffNums2D = new DiffNums2D(origin);
        diffNums2D.debug = true;
        diffNums2D.add(0, 0, m - 1, n - 1, 1);
    }
}
