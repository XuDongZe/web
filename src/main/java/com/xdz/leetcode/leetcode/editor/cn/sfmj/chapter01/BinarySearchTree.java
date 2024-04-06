package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 二叉搜索树
 */
public class BinarySearchTree {
    TreeNode root;

    private static class TreeNode {
        private int val;
        private TreeNode left;
        private TreeNode right;
        private int cnt;

        public TreeNode(int val) {
            this.val = val;
            this.cnt = 1;
        }
    }

    public TreeNode search(int val) {
        return __search(root, val);
    }

    /**
     * 在根节点为node的树中 搜索值val
     *
     * @return null || node.val == val
     */
    private TreeNode __search(TreeNode node, int val) {
        if (node == null) {
            // 没找到
            return null;
        } else if (val == node.val) {
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

    /**
     * 将val插入到 node为根节点的树中
     */
    private TreeNode __insert(TreeNode node, int val) {
        if (node == null) {
            // 找到val的位置了
            return new TreeNode(val);
        } else if (val == node.val) {
            node.cnt++;
        } else if (val < node.val) {
            node.left = __insert(node.left, val);
        } else {
            node.right = __insert(node.right, val);
        }
        return node;
    }

    public void delete(int val) {
        root = __delete(root, val);
    }

    /**
     * 在根节点为node的树中 删除val节点
     */
    private TreeNode __delete(TreeNode node, int val) {
        if (node == null) {
            return null;
        }
        if (val == node.val) {
            // do delete
            node.cnt--;
            if (node.cnt > 0) {
                return node;
            }
            // delete this node
            if (node.left == null && node.right == null) {
                // 叶子节点
                return null;
            } else {
                if (node.left == null) {
                    return node.right;
                } else if (node.right == null) {
                    return node.left;
                } else {
                    // 左右两个节点都存在
                    // 方案一 保留左分支：node的右子节点 变为node的前驱节点的右子节点
//                    {
//                        TreeNode cur = node.left;
//                        while (cur.right != null) {
//                            cur = cur.right;
//                        }
//                        cur.right = node.right;
//                        // 返回 node前驱节点所在的子树：左子节点
//                        return node.left;
//                    }
                    // 方案二 保留右分支：node的左子节点 变为node的后继节点的左子节点
//                    {
//                        TreeNode cur = node.right;
//                        while (cur.left != null) {
//                            cur = cur.left;
//                        }
//                        cur.left = node.left;
//                        return node.right;
//                    }

                    // 方案三：以上方案将一个分支放到另一个分支的叶节点上 树高度会越来越大
                    // 可以移形换影 删除叶节点 保持高度不变甚至减小
                    {
                        TreeNode pre = __preNode(node);
                        assert pre != null;
                        // 因为使用的是前驱、后继节点 所以val的赋值前后 顺序关系没有被破坏
                        node.val = pre.val;
                        node.cnt = pre.cnt;
                        // 在node的左子树中 删除这个前缀
                        pre.cnt = 1;
                        // 因为node的前缀pre的right一定是null 所以不会死循环
                        node.left = __delete(node.left, pre.val);
                        return node;
                    }
                }
            }
        } else if (val < node.val) {
            // val在node的左子树中
            node.left = __delete(node.left, val);
        } else {
            node.right = __delete(node.right, val);
        }
        return node;
    }

    /**
     * node的前驱节点: 左子树中的最大值
     */
    private TreeNode __preNode(TreeNode node) {
        TreeNode cur = node.left;
        if (cur == null) {
            // 左子树为空 没有前驱节点
            return null;
        }
        while (cur.right != null) {
            cur = cur.right;
        }
        // now cur.right == null
        return cur;
    }

    /**
     * node的后继节点 右子树中最左节点
     */
    private TreeNode __postNode(TreeNode node) {
        TreeNode cur = node.right;
        if (cur == null) {
            return null;
        }
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur;
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
        int[] nums = {8, 4, 9, 2, 6, 1, 3, 5, 7};
        BinarySearchTree obj = new BinarySearchTree();
        for (int num : nums) {
            obj.insert(num);
        }
        TreeNode node = obj.search(1);
        System.out.println(node.val);
        obj.delete(1);
        System.out.println(obj);
        obj.delete(7);
        System.out.println(obj);
        obj.delete(8);
        System.out.println(obj);
    }
}
