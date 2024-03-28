package com.xdz.leetcode.leetcode.editor.cn;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution1997 {
    /**
     * 第一次访问第i个房间，说明[0, i-1]的房间都已经访问过了。
     * 那么到达i房间后
     * 如果next[i] == i 那么2天后达到i+1房间
     * 否则
     * next[i] < i 需要回退到next[i]房间 此时需要消耗1步
     * 回退之后 next[i]访问次数为奇数 需要再次回退，然后再回复到i房间。 也就是从next[i] => i
     *
     *
     * 我们定义 f[i] 表示第一次访问第 i 号房间的日期编号，那么答案就是 f[n−1]。
     * 则
     * f[0] = 0
     * for i: 1 => n
     * f[i] =
     * 	f[i-1] + (2) if next[i-1] == i-1
     * 	f[i-1] + (1 + (f[i-1] - f[next[i-1]]) + 1) else
     * ans = f[n - 1]
     *
     * 我们考虑第一次到达第 i−1 号房间的日期编号，记为 f[i−1]，此时需要花一天的时间回退到第 nextVisit[i−1] 号房间，为什么是回退呢？因为题目限制了 0≤nextVisit[i]≤i。
     *
     * 回退之后，此时第 nextVisit[i−1] 号房间的访问为奇数次，而第 [nextVisit[i−1]+1,..i−1] 号房间均被访问偶数次，那么这时候我们从第 nextVisit[i−1] 号房间再次走到第 i−1 号房间，就需要花费 f[i−1]−f[nextVisit[i−1]] 天的时间，然后再花费一天的时间到达第 i 号房间，因此 f[i]=f[i−1]+1+f[i−1]−f[nextVisit[i−1]]+1
     *
     * 最后返回 f[n−1] 即可。
     */
    public int firstDayBeenInAllRooms(int[] nextVisit) {
        final int mod = (int) 1e9 + 7;
        int n = nextVisit.length;
        long[] f = new long[n + 1];
        f[0] = 0;
        for (int i = 1; i < n; i ++) {
            if (nextVisit[i - 1] == i - 1) {
                f[i] = (f[i - 1] + 2 + mod) % mod;
            } else {
                f[i] = (f[i - 1] + 1 + (f[i - 1] - f[nextVisit[i - 1]]) + 1 + mod) % mod;
            }
//            f[i] = (f[i - 1] + 1 + (f[i - 1] - f[nextVisit[i - 1]]) + 1 + mod) % mod;
        }
        return (int) f[n - 1];
    }
}
//leetcode submit region end(Prohibit modification and deletion)
