package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

/**
 * <pre>
 * 前缀和
 *
 * 解决的问题：
 * 数组a[], 支持操作：查询区间和a[l, r] 数组a[]是静态的 其中的值不会修改
 *
 * 朴素解法：
 * 遍历[l,r] 累加 复杂度O(n)
 *
 * 前缀和解法：
 * 预处理原数组, 构造前缀和数组：s[i] = s[i - 1] + a[i]; s[0] = a[0]
 * sum(l, r) = s[r] - s[l - 1]
 * </pre>
 */
public class PrefixSum {
    int s[]; // 前缀和数组

    public PrefixSum(int[] a) {
        this.s = new int[a.length];
        s[0] = a[0];
        for (int i = 1; i < a.length; i++) {
            s[i] = s[i - 1] + a[i];
        }
    }

    public int sum(int l, int r) {
        if (l == 0) {
            return s[r];
        } else {
            return s[r] - s[l - 1];
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5};
        PrefixSum obj = new PrefixSum(nums);
        System.out.println(obj.sum(0, nums.length - 1));
        System.out.println(obj.sum(1, 3));
        System.out.println(obj.sum(2, 2));
    }
}
