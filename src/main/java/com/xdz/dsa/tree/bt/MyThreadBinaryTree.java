package com.xdz.dsa.tree.bt;

/**
 * Description: thread binary tree<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/8 16:07<br/>
 * Version: 1.0<br/>
 */
public class MyThreadBinaryTree<E extends Comparable<E>> extends MyBinaryTree<E> {

    private Node<E> root;
    private Node<E> previous = null;
    private int linearStatus;
    private static final int LINEA_STATUS_NONE = 0;
    private static final int LINEA_STATUS_PRE = 1;
    private static final int LINEA_STATUS_IN = 2;
    private static final int LINEA_STATUS_POST = 3;

    public void preOrderThread() {
        preOrderThread(root);
        linearStatus = LINEA_STATUS_PRE;
        previous = null;
    }

    public void inOrderThread() {
        inOrderThread(root, null);
        linearStatus = LINEA_STATUS_IN;
        previous = null;
    }

    public void postOrderThread() {
        postOrderThread(root, null);
        linearStatus = LINEA_STATUS_POST;
        previous = null;
    }

    /**
     * just handle node and previous's relationship.
     * node.left == previous
     * and node.right == right
     */
    private void preOrderThread(Node<E> node) {
        if (node == null) {
            return;
        }
        doLinearLink(node);
        if (node.leftTag == Node.TAG_NODE) {
            preOrderThread((Node<E>) node.left);
        }
        if (node.rightTag == Node.TAG_NODE) {
            preOrderThread((Node<E>) node.right);
        }
    }

    private void inOrderThread(Node<E> node, Node<E> previous) {
        if (node == null) {
            return;
        }
        inOrderThread((Node<E>) node.left, node);
        doLinearLink(node);
        inOrderThread((Node<E>) node.right, node);
    }

    private void postOrderThread(Node<E> node, Node<E> previous) {
        if (node == null) {
            return;
        }
        postOrderThread((Node<E>) node.left, node);
        postOrderThread((Node<E>) node.right, node);
        doLinearLink(node);
    }

    private void doLinearLink(Node<E> node) {
        // if node.left == null then set prev(node) == previous
        if (node.left == null) {
            node.left = previous;
            node.leftTag = Node.TAG_LINEAR;
        }
        // if previous.right == null then set next(previous) = node
        if (previous != null && previous.right == null) {
            previous.right = node;
            previous.rightTag = Node.TAG_LINEAR;
        }
        previous = node;
    }

//    private void preOrderLinear(Node<E> root) {
//        if (root == null) {
//            return;
//        }
//
//        while (root != null) {
//            while (root.leftTag == Node.TAG_NODE) {
//                System.out.println(root.element);
//                root = root.left;
//            }
//            while (root.rightTag == Node.TAG_LINEAR) {
//                System.out.println(root.element);
//                root = root.right;
//            }
//            // 右子树
//            root = root.right;
//        }
//    }

    public static class Node<E> extends IMyBinaryTree.Node<E> {
        static final int TAG_LINEAR = 1;
        static final int TAG_NODE = 0;
        // leftTag & rightTag for linear.
        int leftTag;
        int rightTag;
    }
}
