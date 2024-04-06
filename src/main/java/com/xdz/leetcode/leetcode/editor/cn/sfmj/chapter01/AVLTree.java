package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * avl tree
 */
public class AVLTree {

    TreeNode root;

    private static class TreeNode {
        private int val;
        private TreeNode left;
        private TreeNode right;
        private int cnt;
        /**
         * 节点的高度
         * 叶子节点的高度为1 非叶子节点的高度为 max(left.height, right.height) + 1
         */
        private int height;

        public TreeNode(int val) {
            this.val = val;
            this.cnt = 1;
            this.height = 1;
        }
    }

    public TreeNode search(int val) {
        return __search(root, val);
    }

    /**
     * 在根节点为 node 的树中 搜索val
     */
    private TreeNode __search(TreeNode node, int val) {
        if (node == null) {
            return null;
        }
        if (val == node.val) {
            return node;
        } else if (val < node.val) {
            return __search(node.left, val);
        } else {
            return __search(node.right, val);
        }
    }

    public void insert(int val) {
        root = __insert(root, val);
    }

    private TreeNode __insert(TreeNode node, int val) {
        if (node == null) {
            node = new TreeNode(val);
        } else if (val == node.val) {
            node.cnt++;
        } else if (val < node.val) {
            node.left = __insert(node.left, val);
        } else {
            node.right = __insert(node.right, val);
        }
        // 回溯
        // 这是理解AVL Tree的关键 balance操作是在回溯的时候调用的 也就是说
        // 会自底向上地 从叶节点 一直到刚开始传进来的node节点 全都调整一遍
        // 其实只需要调整第底下 失衡的节点即可 后续上面的一定不会再调整了
        return __balance(node);
    }

    public void delete(int val) {
        root = __delete(root, val);
    }

    /**
     * 在根节点node的树中 删除val
     */
    private TreeNode __delete(TreeNode node, int val) {
        if (node == null) {
            return null;
        }
        if (val == node.val) {
            node.cnt--;
            if (node.cnt > 0) {
                return node;
            }
            // do delete node
            if (node.left == null && node.right == null) {
                // 叶子节点直接删除
                return null;
            } else if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                // 两个子节点都不是null
                // find post-order node
                TreeNode cur = node.right;
                while (cur.left != null) {
                    cur = cur.left;
                }
                // copy post-node to node 二叉搜索顺序 未被破坏
                node.val = cur.val;
                node.cnt = cur.cnt;
                // 删除post node
                node.right = __delete(node.right, cur.val);
            }
        } else if (val < node.val) {
            node.left = __delete(node.left, val);
        } else {
            node.right = __delete(node.right, val);
        }

        return __balance(node);
    }


    /**
     * 对 根节点为node 的树 在node处的失衡 进行平衡调整
     */
    private TreeNode __balance(TreeNode node) {
        if (node == null) {
            return null;
        }

        if (__height(node.left) - __height(node.right) > 1) { // L侧失衡 隐含了node.left != null
            // 进一步判断是LL or LR todo 分析这里的==
            if (__height(node.left.left) >= __height(node.left.right)) {
                // LL
                node = __balanceLL(node);
            } else {
                // LR
                node = __balanceLR(node);
            }
        } else if (__height(node.right) - __height(node.left) > 1) { // 镜像 R侧失衡
            // 进一步判断是RL or RR
            if (__height(node.right.right) >= __height(node.right.left)) {
                // RR
                node = __balanceRR(node);
            } else {
                // RL
                node = __balanceRL(node);
            }
        }

        // 这里还需要重新计算height吗 => 有必要 比如root==null时 插入第一个节点
        __resetHeight(node);
        return node;
    }

    /**
     * 在node处 发生不平衡 LL型
     * 右旋
     * 返回右旋操作后 新的根节点
     */
    private TreeNode __balanceLL(TreeNode node) {
        TreeNode left = node.left;
        node.left = left.right;
        left.right = node;
        // left和node高度发生变化了
        __resetHeight(node);
        __resetHeight(left);
        return left;
    }

    /**
     * 在node处 发生不平衡 RR型 LL的镜像
     * 左旋
     */
    private TreeNode __balanceRR(TreeNode node) {
        TreeNode right = node.right;
        node.right = right.left;
        right.left = node;

        __resetHeight(node);
        __resetHeight(right);
        return right;
    }

    /**
     * 在node处 发生不平衡 LR型
     */
    private TreeNode __balanceLR(TreeNode node) {
        // 在node处构造LL型
        node.left = __balanceRR(node.left);
        return __balanceLL(node);
    }

    private TreeNode __balanceRL(TreeNode node) {
        // 构造RR型
        node.right = __balanceLL(node.right);
        return __balanceRR(node);
    }


    private int __height(TreeNode node) {
        return node == null ? 0 : node.height;
    }

    private void __resetHeight(TreeNode node) {
        node.height = Math.max(__height(node.left), __height(node.right)) + 1;
    }


    @Override
    public String toString() {
        List<TreeNode> list = new ArrayList<>();
        inOrder(list, root);
        return Arrays.toString(
                list.stream().mapToInt(node -> node.val).toArray()
        );
    }

    /**
     * 以node为根节点的树的 顺序列表 中序遍历
     */
    private void inOrder(List<TreeNode> list, TreeNode node) {
        if (node == null) {
            return;
        }
        inOrder(list, node.left);
        list.add(node);
        inOrder(list, node.right);
    }

    public static void main(String[] args) {
//        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
//        AVLTree obj = new AVLTree();
//        for (int num : nums) {
//            obj.insert(num);
//        }
//        TreeNode node = obj.search(1);
//        System.out.println(node.val);
//        obj.delete(1);
//        System.out.println(obj);
//        obj.delete(7);
//        System.out.println(obj);
//        obj.delete(8);
//        System.out.println(obj);

        int[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19};
        AVLTree obj = new AVLTree();
        for (int num : nums) {
            obj.insert(num);
        }
        int[] deletes = {3, 7, 8, 10, 11, 13, 17, 9};
        for (int n : deletes) {
            obj.delete(n);
        }
        System.out.println(obj);
    }
}
