package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

/**
 * <pre>
 *
 *
 * 树状数组：TreeArray、Binary Indexed Tree
 *
 * 解决的问题：
 * 一个数组a[], 支持两种操作：动态地修改a[i] 以及 查询区间和sum[l,r]
 *
 * 则我们把区间[1,x]划分为若干个小区间 对于修改操作引起的变化 局限在小区间内 然后把小区间综合起来就能得到原[1,x]区间
 * 为了达到log级别的复杂度 我们将对x的二进制分解结果 来进行小区间的划分
 *
 * 整数的二进制分解：前缀和小区间的划分：[l, r]小区间的长度即位lowbit(r)
 *  * x = 2^i1 + 2^i2 + ... 2^im
 * 则区间[1, x]被划分为：
 *  长度为i1的小区间: [1, 2^i1] 这个区间的区间和为 c[2^i1]
 *  长度为i2的小区间: [2^i1+1, 2^i1+2^i2] 这个区间的区间和为 c[2^i1+2^i2] 也就是c[r]
 *  ...
 *  长度为im的小区间: [2^i1+2^i2+...+2^(im-1)+1, 2^i1+2^i2+...+2^(im-1)+2^im]
 *
 * 比如7=4+2+1 被划分为小区间为 [1,4] [5,6] [7,7] 这些小区间对应的区间和 分别为 c[4] c[6] c[7]
 * 比如4=4 被划分为小区间为 [1,4]
 *
 * 观察可以得到：小区间的区间和c[i]
 * 1. 对应的a[l, r]为 r=i, l=i-lowbit(i) + 1
 * 2. 区间长度为 lowbit(i)
 * 3. 父节点管辖的区间长度为i节点的2倍: [i-lowbit(i)+1, i] + [i+1, i+lowbit(i)] 所以父节点为i+lowbit(i) 子节点为i-lowbit(i)
 *
 *
 * 两个基本操作
 * update 单点更新 给a[i] += k。需要级联更新c[i]到父节点 O(log n)
 * query  区间和查询 [1, i] = 所有二进制分解的小区间的区间和 之和  级联查找c[i]的子节点 O(log n)
 *
 *
 * 有点并查集的意思；有点一致性哈希、jdk7HashMap分区降低并发的想法
 *
 * https://www.bilibili.com/video/BV1xq4y1i7et/?spm_id_from=333.999.0.0
 * </pre>
 */
public class TreeArray {

    int[] c; // binary indexed tree 下标从1开始

    public TreeArray(int[] a) {
        c = new int[a.length + 1];
        // init复杂度 O(log n) 一般也够用了
//        for (int i = 0; i < a.length; i++) {
//            add(i, a[i]);
//        }
        // init复杂度 O(n) 考虑c[i]对父节点的贡献
        for (int i = 1; i <= a.length; i ++) {
            c[i] += a[i - 1];
            int pi = i + lowbit(i);
            if (pi <= a.length) {
                c[pi] += c[i];
            }
        }
    }

    /**
     * a[idx] += k
     */
    public void add(int idx, int k) {
        for (int i = idx + 1; i < c.length; i += lowbit(i)) {
            c[i] += k;
        }
    }

    /**
     * 查询a[l,r]的区间和
     */
    public int query(int l, int r) {
        if (l == 0) {
            return prefixSum(r);
        }
        return prefixSum(r) - prefixSum(l - 1);
    }

    /**
     * 查询a[1, r]的区间和
     */
    private int prefixSum(int r) {
        int ans = 0;
        for (int i = r + 1; i >= 1; i -= lowbit(i)) {
            ans += c[i];
        }
        return ans;
    }

    // 二进制分解
    private int lowbit(int x) {
        return x & (-x);
    }


    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        TreeArray obj = new TreeArray(nums);
        System.out.println(obj.query(0, nums.length - 1));
        obj.add(0, 1);
        System.out.println(obj.query(0, 0));
        System.out.println(obj.query(0, 1));
        System.out.println(obj.query(0, nums.length - 1));
        obj.add(1, -1);
        System.out.println(obj.query(0, nums.length - 1));
    }
}
