package com.xdz.web.dsa.tree.bt;

import com.xdz.web.dsa.Queue.IMyQueue;
import com.xdz.web.dsa.Queue.MyArrayCircleQueue;
import com.xdz.web.dsa.list.IMyList;
import com.xdz.web.dsa.list.MyArrayList;
import com.xdz.web.dsa.stack.IMyStack;
import com.xdz.web.dsa.stack.MyArrayStack;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;
import org.slf4j.helpers.NOPLoggerFactory;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/22 16:19<br/>
 * Version: 1.0<br/>
 */
@SuppressWarnings("ALL")
public class MyBinaryTree<E extends Comparable<E>> {

    private Node<E> root;

    public void preOrder() {
        preOrderIterate(root);
    }

    public void inOrder() {
        inOrderIterate(root);
    }

    public void postOrder() {
        postOrderIterate(root);
    }

    public void levelOrder() {
        levelOrderIterateWithLine(root);
    }

    public int size() {
        return sizeRecursive(root);
    }

    public int height() {
        return heightRecursize(root);
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
        idxHolder.value ++;
        node.left = create(list, idxHolder);
        idxHolder.value ++;
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

    public static class Node<E> {
        Object element;
        Node left;
        Node right;

        public Node(Object element) {
            this.element = element;
        }

        public Node(Object element, Node left, Node right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
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
        binaryTree.levelOrderIterateWithLine(binaryTree.root);

        int size = binaryTree.size();

        int height = binaryTree.height();
    }
}
