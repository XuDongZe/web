package com.xdz.leetcode.leetcode.editor.cn;

import java.util.Arrays;

/**
 * 一场比赛中共有 n 支队伍，按从 0 到 n - 1 编号。
 * <p>
 * 给你一个下标从 0 开始、大小为 n * n 的二维布尔矩阵 grid 。对于满足 0 <= i, j <= n - 1 且 i != j 的所有 i, j
 * ：如果 grid[i][j] == 1，那么 i 队比 j 队 强 ；否则，j 队比 i 队 强 。
 * <p>
 * 在这场比赛中，如果不存在某支强于 a 队的队伍，则认为 a 队将会是 冠军 。
 * <p>
 * 返回这场比赛中将会成为冠军的队伍。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：grid = [[0,1],[0,0]]
 * 输出：0
 * 解释：比赛中有两支队伍。
 * grid[0][1] == 1 表示 0 队比 1 队强。所以 0 队是冠军。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：grid = [[0,0,1],[1,0,1],[0,0,0]]
 * 输出：1
 * 解释：比赛中有三支队伍。
 * grid[1][0] == 1 表示 1 队比 0 队强。
 * grid[1][2] == 1 表示 1 队比 2 队强。
 * 所以 1 队是冠军。
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * n == grid.length
 * n == grid[i].length
 * 2 <= n <= 100
 * grid[i][j] 的值为 0 或 1
 * <p>
 * 对于所有 i， grid[i][i] 等于 0.
 * 对于满足 i != j 的所有 i, j ，grid[i][j] != grid[j][i] 均成立
 * 生成的输入满足：如果 a 队比 b 队强，b 队比 c 队强，那么 a 队比 c 队强
 * <p>
 * <p>
 * Related Topics 数组 矩阵 👍 30 👎 0
 */

//leetcode submit region begin(Prohibit modification and deletion)
class Solution2923 {
    public int findChampion(int[][] grid) {
        boolean[] fail = new boolean[grid.length];
        for (int i = 0; i < grid.length; i++) {
            if (fail[i]) {
                continue;
            }
            for (int j = 0; j < grid.length; j++) {
                if (i != j) {
                    if (grid[i][j] == 1) {
                        fail[j] = true;
                    } else {
                        fail[i] = true;
                    }
                }
            }
            if (!fail[i]) {
                return i;
            }
        }
        return -1;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
