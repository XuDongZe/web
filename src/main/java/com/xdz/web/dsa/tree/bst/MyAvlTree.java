package com.xdz.web.dsa.tree.bst;

import com.xdz.web.dsa.holder.IntHolder;
import com.xdz.web.dsa.list.IMyList;
import com.xdz.web.dsa.list.MyArrayList;

/**
 * Description: avl-tree. bst + self-balancing: left-rotate/right-rotate<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/4 18:04<br/>
 * Version: 1.0<br/>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MyAvlTree<E extends Comparable<E>> extends MyBinarySearchTree<E> {

    @Override
    public void insert(E e) {
        super.insert(e);
        // adjust
        root = adjustNode(root);
    }

    @Override
    public void remove(E e) {
        removeRecursive(root, e);
    }

    @Override
    protected Node<E> removeRecursive(Node<E> root, E e) {
        root = super.removeRecursive(root, e);
        root = adjustNode(root);
        return root;
    }

    private Node<E> adjustNode(Node<E> root) {
        int leftHeight = height(root.left), rightHeight = height(root.right);
        if (leftHeight - rightHeight > 1) {
            // root.left != null
            Node left = root.left;
            if (height(left.right) > height(left.left)) {
                root.left = leftRotate(left);
            }
            // now height(left.left) > height(left.right)
            root = rightRotate(root);
        } else if (rightHeight - leftHeight > 1) {
            Node right = root.right;
            if (height(right.left) > height(right.right)) {
                root.right = rightRotate(right);
            }
            // now height(right.right) > height(right.left)
            root = leftRotate(root);
        }
        return root;
    }

    /**<pre>
     * left-rotate op for node, it will occur while height(node.right) - height(node.left) > 1
     * now, the left subtree is lower than the right one. so assume that:
     * we catch the right subtree's root node, and bring it up, let it replace `node`. we will see that:
     * 1. the right subtree's root will be the new root of tree. the old root(the `node`) will be newNode's left.
     * so right subtree's height will decr one(for root be bring up) and left subtree's height will incr one(for old root node be down)
     * so abs(height(node.left) - height(node.right)) decr 2.
     * so we got:
     *   1. for init, after each add node, abs(height(node.left) - height(node.right)) <= 1.
     *   2. if we adjust this every move, we will guarantee that node tree is avl.
     * 2. what node changed?
     *   1. root.right, be the new root.
     *      1. right, not change. hold.
     *      2. left, new root's left is the old root. changed.
     *          change to root.
     *   2. root, be the new root's left-tree root.
     *      1. right, old root's right will be the new root. changed.
     *          change to root.right.left
     *          2. left, not change. hold.
     * </pre>
     */
    private Node leftRotate(Node node) {
        // now node != null && height(node.right) - height(node.left) > 1
        // node.right != null but node.left maybe null
        Node right = node.right; // not null
        node.right = right.left;
        right.left = node;
        // adjust root pointer
        return right;
    }

    private Node rightRotate(Node node) {
        // now height(node.left) - height(node.right) > 1, so bring node.left up and down root to right.
        // node != null && node.left != null, node.right maybe null
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        return left;
    }

    public static void main(String[] args) {
        IMyList<Integer> list = new MyArrayList<>(6);
        // test left-rotate
        list.addAll(new Integer[]{3, 2, 5, 4, 6, 7});
        MyAvlTree<Integer> tree = new MyAvlTree<>();
        tree.insertAll(list);
        System.out.println("after left rotate, isAvl: " + tree.isAvl());

        // test right rotate
        tree.clear();
        IMyList<Integer> list1 = new MyArrayList<>(6);
        list1.addAll(new Integer[]{6, 4, 7, 3, 5, 2});
        tree.insertAll(list1);
        System.out.println("after right rotate, isAvl: " + tree.isAvl());

        // test double rotate
        tree.clear();
        IMyList<Integer> list2 = new MyArrayList<>(6);
        list2.addAll(new Integer[]{6, 3, 7, 2, 4, 5});
        tree.insertAll(list2);
        System.out.println("after double rotate, isAvl: " + tree.isAvl());
    }
}
