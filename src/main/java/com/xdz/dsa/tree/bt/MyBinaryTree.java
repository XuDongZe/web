package com.xdz.dsa.tree.bt;

import com.xdz.dsa.Queue.IMyQueue;
import com.xdz.dsa.Queue.MyArrayCircleQueue;
import com.xdz.dsa.holder.IntHolder;
import com.xdz.dsa.list.IMyList;
import com.xdz.dsa.list.MyArrayList;
import com.xdz.dsa.stack.IMyStack;
import com.xdz.dsa.stack.MyArrayStack;

/**
 * Description: binary tree link implement<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/22 16:19<br/>
 * Version: 1.0<br/>
 */
@SuppressWarnings("ALL")
public class MyBinaryTree<E extends Comparable<E>> implements IMyBinaryTree<E> {

    protected Node<E> root;

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public boolean isBst() {
        return isBst(root);
    }

    @Override
    public boolean isAvl() {
        return isAvlV2(root, new com.xdz.dsa.holder.IntHolder());
    }

    @Override
    public Node<E> find(E e) {
        return null;
    }

    @Override
    public void insert(E e) {
        // pre in post
    }

    @Override
    public void remove(E e) {

    }

    @Override
    public void clear() {
        clearRecursive(root);
    }

    @Override
    public boolean isEmpty() {
        return root != null;
    }

    @Override
    public void preOrder() {
        preOrderIterate(root);
    }

    @Override
    public void inOrder() {
        inOrderIterate(root);
    }

    @Override
    public void postOrder() {
        postOrderIterate(root);
    }

    @Override
    public void levelOrder() {
        levelOrderIterateWithLine(root);
    }

    @Override
    public int size() {
        return sizeRecursive(root);
    }

    @Override
    public int height() {
        return heightRecursize(root);
    }

    @Override
    public void preOrder(Node<E> root) {
        preOrderIterate2(root);
    }

    @Override
    public void inOrder(Node<E> root) {
        inOrderIterate(root);
    }

    @Override
    public void postOrder(Node<E> root) {
        postOrderIterate(root);
    }

    @Override
    public void levelOrder(Node<E> root) {
        levelOrderIterateWithLine(root);
    }

    @Override
    public int size(Node<E> root) {
        return sizeRecursive(root);
    }

    @Override
    public int height(Node<E> root) {
        return heightRecursize(root);
    }

    /**
     * set all node's left and right pointer to null. help gc.
     *
     * @param root
     */
    private void clearRecursive(Node<E> root) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            clearRecursive(root.left);
            root.left = null;
        }
        if (root.right != null) {
            clearRecursive(root.right);
            root.right = null;
        }
    }

    private boolean isBst(Node<E> root) {
        if (root == null) {
            return true;
        }
        if (root.left != null && root.left.element.compareTo(root.element) > 0) {
            return false;
        }
        if (root.right != null && root.right.element.compareTo(root.element) < 0) {
            return false;
        }
        return isBst(root.left) && isBst(root.right);
    }


    /**
     * O(n * log n). n for n nodes, log(n) for height(node) for each node.
     */
    private boolean isAvl(Node node) {
        if (node == null) {
            return true;
        }
        // test node
        if (Math.abs(height(node.left) - height(node.right)) > 1) {
            return false;
        }
        // test node.left && node.right
        return isAvl(node.left) && isAvl(node.right);
    }

    /**
     * O(n) just the same as postOrder.
     * do Math.abs && max each n nodes.
     */
    private boolean isAvlV2(Node node, com.xdz.dsa.holder.IntHolder heightHolder) {
        if (node == null) {
            heightHolder.value = -1;
            return true;
        }

        com.xdz.dsa.holder.IntHolder leftHolder = new com.xdz.dsa.holder.IntHolder();
        com.xdz.dsa.holder.IntHolder rightHolder = new com.xdz.dsa.holder.IntHolder();
        if (isAvlV2(node.left, leftHolder) && isAvlV2(node.right, rightHolder)) {
            if (Math.abs(leftHolder.value - rightHolder.value) <= 1) {
                heightHolder.value = Math.max(leftHolder.value, rightHolder.value) + 1;
                return true;
            }
        }
        return false;
    }

    public static <E extends Comparable<E>> MyBinaryTree<E> create(IMyList<E> list) {
        MyBinaryTree<E> tree = new MyBinaryTree<>();
        if (list != null) {
            tree.root = create(list, new IntHolder());
        }
        return tree;
    }

    private static <E extends Comparable<E>> Node<E> create(IMyList<E> list, IntHolder idxHolder) {
        if (idxHolder.value >= list.size()) {
            return null;
        }

        E e = list.get(idxHolder.value);
        if (e == null) {
            // 叶子节点需要显示的表示出来
            return null;
        }

        Node<E> node = new Node<E>(e);
        idxHolder.value++;
        node.left = create(list, idxHolder);
        idxHolder.value++;
        node.right = create(list, idxHolder);
        return node;
    }

    private void preOrderRecursive(Node<E> root) {
        if (root == null) {
            return;
        }
        System.out.println(root.element);
        preOrderRecursive(root.left);
        preOrderRecursive(root.right);
    }

    /**
     * <pre>
     * use user-define stack to mock param-stack when function call.
     * the core idea is function's param and local-var:
     * 1. need to save before another call
     * 2. and need to load after the another call return.
     *
     * in the preOrderRecursive func, another call is the func itself
     * and the data need to save and load is the param: root
     * </pre>
     *
     * @param root
     */
    private void preOrderIterate(Node<E> root) {
        IMyStack<Node> stack = new MyArrayStack<>();
        // root != null, current level func call is not end
        // !stack.isEmpty(), another level func call is not end
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                // visit root node
                System.out.println(root.element);
                // before recursive call, save func params
                stack.push(root);
                // visit left sub-tree
                root = root.left;
            } else {
                // after recursive call, load func params and use the loaded one.
                // left sub tree and root of stack's top has visited
                // back tracking to current root
                root = stack.pop();
                // visit root.right
                root = root.right;
                // no need to save and load any local params for now this is a tail-recursive
            }
        }
    }

    private void preOrderIterate2(Node<E> root) {
        IMyStack<Node<E>> stack = new MyArrayStack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            root = stack.pop();
            System.out.println(root.element);
            if (root.right != null) {
                stack.push(root.right);
            }
            if (root.left != null) {
                stack.push(root.left);
            }
        }
    }

    private void inOrderRecursive(Node<E> root) {
        if (root == null) {
            return;
        }
        inOrderRecursive(root.left);
        System.out.println(root.element);
        inOrderRecursive(root.right);
    }

    private void inOrderIterate(Node<E> root) {
        IMyStack<Node> stack = new MyArrayStack<>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                stack.push(root);
                root = root.left;
            } else {
                // now return from root.left sub-call
                root = stack.pop();
                System.out.println(root.element);
                root = root.right;
            }
        }
    }

    private void postOrderRecursive(Node<E> root) {
        if (root == null) {
            return;
        }
        postOrderRecursive(root.left);
        postOrderRecursive(root.right);
        System.out.println(root.element);
    }

    private void postOrderIterate(Node<E> root) {
        // first do preOrderIterate
        IMyStack<Node> stack = new MyArrayStack<>();
        IMyStack<E> result = new MyArrayStack<>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                // root
                stack.push(root);
                result.push((E) root.element);
                // right
                root = root.right;
            } else {
                root = stack.pop();
                // left
                root = root.left;
            }
        }
        // now result is preOrder, reverse it(pop iterater)
        while (!result.isEmpty()) {
            System.out.println(result.pop());
        }
    }

    private void levelOrderIterate(Node<E> root) {
        if (root == null) {
            return;
        }
        IMyQueue<Node<E>> queue = new MyArrayCircleQueue<>(sizeRecursive(root));
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            root = queue.dequeue();
            System.out.println(root.element);
            if (root.left != null) {
                queue.enqueue(root.left);
            }
            if (root.right != null) {
                queue.enqueue(root.right);
            }
        }
    }

    private void levelOrderIterateWithLine(Node<E> root) {
        IMyQueue<Node<E>> queue = new MyArrayCircleQueue<>(sizeRecursive(root));
        queue.enqueue(root);
        int cur = 1, next = 0;

        while (!queue.isEmpty()) {
            root = queue.dequeue();
            System.out.println(root.element);
            cur--;
            if (root.left != null) {
                queue.enqueue(root.left);
                next++;
            }
            if (root.right != null) {
                queue.enqueue(root.right);
                next++;
            }
            if (cur == 0) {
                System.out.println();
                cur = next;
                next = 0;
            }
        }
    }

    private int sizeRecursive(Node<E> root) {
        if (root == null) {
            return 0;
        }
        return sizeRecursive(root.left) + sizeRecursive(root.right) + 1;
    }

    private int heightRecursize(Node<E> root) {
        if (root == null) {
            return -1;
        }
        return Math.max(heightRecursize(root.left), heightRecursize(root.right)) + 1;
    }

    public static void main(String[] args) {
        IMyList<Integer> list = new MyArrayList<>(10);
        list.addLast(1);
        list.addLast(2);
        list.addLast(4);
        list.addLast(null);
        list.addLast(null);
        list.addLast(5);
        list.addLast(null);
        list.addLast(null);
        list.addLast(6);
        list.addLast(null);
        list.addLast(null);

        MyBinaryTree<Integer> binaryTree = MyBinaryTree.create(list);
        binaryTree.preOrderIterate(binaryTree.root);
        binaryTree.preOrderIterate2(binaryTree.root);
        binaryTree.preOrderRecursive(binaryTree.root);

        binaryTree.inOrderIterate(binaryTree.root);
        binaryTree.inOrderRecursive(binaryTree.root);

        binaryTree.postOrderIterate(binaryTree.root);
        binaryTree.postOrderRecursive(binaryTree.root);

        binaryTree.levelOrder();
        binaryTree.levelOrderIterate(binaryTree.root);

        System.out.println(binaryTree.size());

        System.out.println(binaryTree.height());
    }
}
