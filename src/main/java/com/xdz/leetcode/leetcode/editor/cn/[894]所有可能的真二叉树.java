
//leetcode submit region begin(Prohibit modification and deletion)

import java.util.ArrayList;
import java.util.List;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 * int val;
 * TreeNode left;
 * TreeNode right;
 * TreeNode() {}
 * TreeNode(int val) { this.val = val; }
 * TreeNode(int val, TreeNode left, TreeNode right) {
 * this.val = val;
 * this.left = left;
 * this.right = right;
 * }
 * }
 */

class Solution {
    public List<TreeNode> allPossibleFBT(int n) {
        return dfs(n);
    }

    private List<TreeNode> dfs(int n) {
        if (n == 1) {
            List<TreeNode> res = new ArrayList<>();
            res.add(new TreeNode(0));
            return res;
        }
        if (n % 2 == 0) {
            return new ArrayList<>();
        } else {
            List<TreeNode> res = new ArrayList<>();
            // 枚举左子树的节点数量
            for (int i = 1; i < n; i += 2) {
                List<TreeNode> left = dfs(i);
                List<TreeNode> right = dfs(n - 1 - i);
                for (TreeNode l : left) {
                    for (TreeNode r : right) {
                        res.add(new TreeNode(0, l, r));
                    }
                }
            }
            return res;
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
