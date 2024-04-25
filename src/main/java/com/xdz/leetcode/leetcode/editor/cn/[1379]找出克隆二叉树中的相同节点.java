
//leetcode submit region begin(Prohibit modification and deletion)
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */



class Solution {
    TreeNode cloned;
    TreeNode original;
    TreeNode target;

    public final TreeNode getTargetCopy(final TreeNode original, final TreeNode cloned, final TreeNode target) {
        this.original = original;
        this.cloned = cloned;
        this.target = target;
        return dfs(cloned);
    }

    private TreeNode dfs(TreeNode root) {
        if (root == null) {
            return null;
        }
        if (root.val == target.val) {
            return root;
        }
        TreeNode result = dfs(root.left);
        if (result != null) {
            return result;
        }
        return dfs(root.right);
    }
}
//leetcode submit region end(Prohibit modification and deletion)
