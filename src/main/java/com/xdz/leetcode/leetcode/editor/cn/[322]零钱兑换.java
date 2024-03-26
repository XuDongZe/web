package com.xdz.leetcode.leetcode.editor.cn;

import java.util.Arrays;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution322 {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);

        dp[0] = 0;
        for (int i = 1; i <= amount; i ++) {
            int min = dp[i];
            for (int j = 0; j < coins.length; j ++) {
                if (coins[j] <= i) {
                    min = Math.min(min, dp[i - coins[j]] + 1);
                }
            }
            dp[i] = min;
        }

        return dp[amount] > amount ? -1 : dp[amount];
    }
}
//leetcode submit region end(Prohibit modification and deletion)
