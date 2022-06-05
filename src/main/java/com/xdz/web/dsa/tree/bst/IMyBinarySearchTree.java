package com.xdz.web.dsa.tree.bst;

import com.xdz.web.dsa.list.IMyList;

public interface IMyBinarySearchTree<E extends Comparable<E>> {

    Node<E> find(E e);

    Node<E> findMin();

    Node<E> findMax();

    void insert(E e);

    default void insertAll(IMyList<E> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (E e : list) {
            insert(e);
        }
    }

    void remove(E e);

    default boolean contains(E e) {
        return find(e) != null;
    }

    void clear();

    boolean isEmpty();

    class Node<E extends Comparable<E>> {
        E element;
        Node<E> left;
        Node<E> right;

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

        public E getElement() {
            return (E) element;
        }
    }
}
