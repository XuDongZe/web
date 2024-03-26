package com.xdz.leetcode.leetcode.editor.cn.pjs;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2024/3/26 22:29<br/>
 * Version: 1.0<br/>
 */
public class __dfs_2_1_4_1 {
    int a[];
    int k;

    /**
     * 从数组a中选出若干个数字 他们的和是k
     *
     * @return true 可以 false 不可以
     * <p>
     * 算法：
     * 对于数组a的每一项 选择加或者不加 当每一项都做出选择后 判断当前和sum是否==k
     * 状态数为2^n 因此复杂度是O(2^n)
     */
    public boolean solve(int a[], int k) {
        this.a = a;
        this.k = k;
        return dfs(0, 0);
    }

    /**
     * 当前和是sum 第i项是否选择进行分支
     */
    private boolean dfs(int sum, int i) {
        if (i == a.length) {
            return sum == k;
        }
        // 剩下的都不用选啦 提前结束
        if (sum == k) {
            return true;
        }
        // i < a.length 那么第i项可以选择或者不选择
        return dfs(sum + a[i], i + 1) || dfs(sum, i + 1);
    }


    public static void main(String[] args) {
        int[] a = {1,2,4,7};

        __dfs_2_1_4_1 obj = new __dfs_2_1_4_1();
        System.out.println(obj.solve(a, 5));
        System.out.println(obj.solve(a, 13));
        System.out.println(obj.solve(a, 15));
        System.out.println(obj.solve(a, 0));
        System.out.println(obj.solve(a, -1));
    }

}
