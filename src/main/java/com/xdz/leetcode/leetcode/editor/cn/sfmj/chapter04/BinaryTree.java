package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class BinaryTree {

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            val = x;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        preHelper(root, list);
        return list;
    }

    private static void preHelper(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        list.add(root.val);
        preHelper(root.left, list);
        preHelper(root.right, list);
    }

    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorderHelper(root, list);
        return list;
    }

    private static void inorderHelper(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        inorderHelper(root.left, list);
        list.add(root.val);
        inorderHelper(root.right, list);
    }

    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        postOrderHelper(root, list);
        return list;
    }

    private static void postOrderHelper(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        postOrderHelper(root.left, list);
        postOrderHelper(root.right, list);
        list.add(root.val);
    }

    public static List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            // 先入栈右节点 所有左节点先出栈
            TreeNode node = stack.pop();
            list.add(node.val);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }

        return list;
    }

    public static List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            // 沿着左子树 所有沿途节点 入栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            // 左子节点为空 返回到根节点 访问之
            cur = stack.pop();
            list.add(cur.val);
            // 右子节点 与 根节点同样的处理
            cur = cur.right;
        }

        return list;
    }

    public static List<Integer> postorderTraversal2(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        // 根 右 左
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            list.add(node.val);
            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }
        // 根 左 右
        Collections.reverse(list);

        return list;
    }

    public static List<Integer> postorderTraversal3(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null) {
            return list;
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode prev = null;
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            // 遍历左子树
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            cur = stack.pop();

            // 右子树为空 || 从右子树返回
            if (cur.right == null || prev == cur.right) {
                list.add(cur.val);
                prev = cur;
                // cur的访问结束
                cur = null;
            } else {
                // cur.right != null && prev != cur.right 从左子树返回 并且 右子树非空
                // cur 重新进组
                stack.push(cur);
                cur = cur.right;
            }
        }

        return list;
    }

}
