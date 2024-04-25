import java.util.Arrays;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {

    public int combinationSum4(int[] nums, int target) {
        Arrays.sort(nums);
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int i = 1; i <= target; i++) {
            int sum = 0;
            for (int num : nums) {
                if (i - num >= 0) {
                    sum += dp[i - num];
                } else {
                    break;
                }
            }
            dp[i] = sum;
        }
        return dp[target];
    }
}
//leetcode submit region end(Prohibit modification and deletion)
