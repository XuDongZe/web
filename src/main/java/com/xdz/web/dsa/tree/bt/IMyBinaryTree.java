package com.xdz.web.dsa.tree.bt;

import com.xdz.web.dsa.list.IMyList;

/**
 * Description: binary tree ADT<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/8 15:53<br/>
 * Version: 1.0<br/>
 */
public interface IMyBinaryTree<E extends Comparable<E>> {

    Node<E> root();

    boolean isBst();

    boolean isAvl();

    Node<E> find(E e);

    void insert(E e);

    void remove(E e);

    void clear();

    boolean isEmpty();

    void preOrder();

    void inOrder();

    void postOrder();

    void levelOrder();

    int size();

    int height();

    void preOrder(Node<E> root);

    void inOrder(Node<E> root);

    void postOrder(Node<E> root);

    void levelOrder(Node<E> root);

    int size(Node<E> root);

    int height(Node<E> root);

    default void insertAll(IMyList<E> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (E e : list) {
            insert(e);
        }
    }

    default void insertAll(E[] list) {
        if (list == null || list.length <= 0) {
            return;
        }
        for (E e : list) {
            insert(e);
        }
    }

    default boolean contains(E e) {
        return find(e) != null;
    }

    class Node<E> {
        public E element;
        public Node<E> left;
        public Node<E> right;

        public Node() {

        }

        public Node(E element) {
            this.element = element;
        }

        public Node(E element, Node<E> left, Node<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }
}
