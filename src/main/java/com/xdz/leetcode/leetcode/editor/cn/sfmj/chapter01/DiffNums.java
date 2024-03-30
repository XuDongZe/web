package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.Arrays;

/**
 * <pre>
 * 差分数组
 *
 * 给定一个数组nums 对区间[a,b]中的每个元素+3 频繁的进行区间增加操作 但是很少对nums[i]进行读取
 *
 * 区间增加操作 一般使用循环 在原数组上直接进行
 * for (int i = a; i <= b; i ++) {
 *     sum[i] += k;
 * }
 *
 * 我们可以使用差分数组来描述原数组: 原数组相邻元素之间的差值
 * d[i] = nums[i] - nums[i-1]
 * d[0] = nums[0]
 *
 * 则原数组为差分数组的前缀和
 * nums[0] = d[0]
 * nums[1] - nums[0] = d[1] => nums[1] = d[1] + nums[0] = d[1] + d[0]
 * nums[2] = d[2] + nums[1] = d[2] + d[1] + nums[0] = d[2] + d[1] + d[0]
 * nums[i] = sum(d[0:i])
 *
 * 对区间[a,b] +k:
 * d[a] += k => [a, +INF) +k
 * d[b + 1] -= k => [b+1, +INF) -k
 * </pre>
 */
public class DiffNums {
    private int[] diff; // 差分数组

    public DiffNums(int[] nums) {
        this.diff = new int[nums.length];
        // 构造差分数组
        diff[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            diff[i] = nums[i] - nums[i - 1];
        }
    }

    // [a, b] 区间每个数 +k
    public void increment(int a, int b, int k) {
        diff[a] += k;
        if (b + 1 < diff.length) {
            diff[b + 1] -= k;
        }
        // 当b + 1 >= diff.length时 说明b之后没有区间了 不需要处理
    }

    // 返回原数组
    public int[] result() {
        int[] nums = new int[diff.length];
        nums[0] = diff[0];
        for (int i = 1; i < nums.length; i++) {
            nums[i] = nums[i - 1] + diff[i];
        }
        return nums;
    }

    public static void main(String[] args) {
        int[] nums = {8, 4, 6, 7, 9, 3, 1, 5};
        DiffNums obj = new DiffNums(nums);
        obj.increment(1, 5, 3);
        System.out.println(Arrays.toString(obj.result()));
    }
}
