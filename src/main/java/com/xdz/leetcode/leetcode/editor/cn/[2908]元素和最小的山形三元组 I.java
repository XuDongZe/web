package com.xdz.leetcode.leetcode.editor.cn;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution2908 {
    // 输入：nums = [8,6,1,5,3] 输出：9
    public int minimumSum(int[] nums) {
        final int MAX = Integer.MAX_VALUE / 2;
        // 预处理前缀最小值 prefixMin[i] == min[0, min]
        int n = nums.length;
        int[] prefixMin = new int[n];
        prefixMin[0] = nums[0];
        for (int i = 1; i < n; i++) {
            prefixMin[i] = Math.min(prefixMin[i - 1], nums[i]);
        }
        // 后缀最小值 suffixMin[i] = min[i, n-1]
        int[] suffixMin = new int[n];
        suffixMin[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            suffixMin[i] = Math.min(suffixMin[i + 1], nums[i]);
        }

        int min = MAX;
        // 枚举k
        for (int k = 1; k < n - 1; k++) {
            if (prefixMin[k - 1] < nums[k] && nums[k] > suffixMin[k + 1]) {
                min = Math.min((prefixMin[k - 1] + nums[k] + suffixMin[k + 1]), min);
            }
        }
        return min == MAX ? -1 : min;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
