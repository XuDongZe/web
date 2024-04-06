package com.xdz.leetcode.leetcode.editor.cn;

/**
给你一棵树，树上有 n 个节点，按从 0 到 n-1 编号。树以父节点数组的形式给出，其中 parent[i] 是节点 i 的父节点。树的根节点是编号为 0 的
节点。 

 树节点的第 k 个祖先节点是从该节点到根节点路径上的第 k 个节点。 

 实现 TreeAncestor 类： 

 
 TreeAncestor（int n， int[] parent） 对树和父数组中的节点数初始化对象。 
 getKthAncestor(int node, int k) 返回节点 node 的第 k 个祖先节点。如果不存在这样的祖先节点，返回 -1 。 
 

 

 示例 1： 

 

 
输入：
["TreeAncestor","getKthAncestor","getKthAncestor","getKthAncestor"]
[[7,[-1,0,0,1,1,2,2]],[3,1],[5,2],[6,3]]

输出：
[null,1,0,-1]

解释：
TreeAncestor treeAncestor = new TreeAncestor(7, [-1, 0, 0, 1, 1, 2, 2]);

treeAncestor.getKthAncestor(3, 1);  // 返回 1 ，它是 3 的父节点
treeAncestor.getKthAncestor(5, 2);  // 返回 0 ，它是 5 的祖父节点
treeAncestor.getKthAncestor(6, 3);  // 返回 -1 因为不存在满足要求的祖先节点
 

 

 提示： 

 
 1 <= k <= n <= 5 * 10⁴ 
 parent[0] == -1 表示编号为 0 的节点是根节点。 
 对于所有的 0 < i < n ，0 <= parent[i] < n 总成立 
 0 <= node < n 
 至多查询 5 * 10⁴ 次 
 

 Related Topics 树 深度优先搜索 广度优先搜索 设计 二分查找 动态规划 👍 249 👎 0

*/

//leetcode submit region begin(Prohibit modification and deletion)
class TreeAncestor {

    // pa[x][i]: 节点x的第2^i个祖先节点 不存在则为-1
    private int[][] pa;

    // 预处理处pa
    public TreeAncestor(int n, int[] parent) {
        pa = new int[n][32];
        for (int x = 0; x < n; x ++) {
            pa[x][0] = parent[x];
        }

        for (int i = 1; i < 32; i ++) {
            for (int x = 0; x < n; x ++) {
                int p = pa[x][i - 1];
                if (p < 0) {
                    pa[x][i] = -1;
                } else {
                    pa[x][i] = pa[p][i - 1];
                }
            }
        }
    }

    public int getKthAncestor(int node, int k) {
        int p = node;
        for (int i = 0; i < 32; i++) {
            if (((k >>> i) & 1) != 0) {
                p = pa[p][i];
                if (p == -1) {
                    return p;
                }
            }
        }
        return p;
    }
}

/**
 * Your TreeAncestor object will be instantiated and called as such:
 * TreeAncestor obj = new TreeAncestor(n, parent);
 * int param_1 = obj.getKthAncestor(node,k);
 */
//leetcode submit region end(Prohibit modification and deletion)