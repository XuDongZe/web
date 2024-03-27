package com.xdz.leetcode.leetcode.editor.cn;

import java.util.Arrays;
import java.util.Comparator;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {

    private static final int MOD = 1000000007;

    public int countWays(int[][] ranges) {
        Arrays.sort(ranges, Comparator.comparingInt(a -> a[0]));
        int end = ranges[0][1];
        int res = 2;
        for (int i = 1; i < ranges.length; i ++) {
            // 无法合并
            if (ranges[i][0] > end) {
                // 统计区间数量
                res = res * 2 % MOD;
                // 新区间
                end = ranges[i][1];
            } else {
                // 合并
                end = Math.max(end, ranges[i][1]);
            }
        }
        return res;
    }
}


//leetcode submit region end(Prohibit modification and deletion)
