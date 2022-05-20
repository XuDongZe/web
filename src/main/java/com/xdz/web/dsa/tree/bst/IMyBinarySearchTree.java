package com.xdz.web.dsa.tree.bst;

import com.xdz.web.dsa.list.IMyList;

public interface IMyBinarySearchTree<E extends Comparable<E>> {

    Node<E> find(E e);

    Node<E> findMin();

    Node<E> findMax();

    void insert(E e);

    void insertAll(IMyList<E> list);

    void remove(E e);

    boolean contains(E e);

    void clear();

    boolean isEmpty();

    class Node<E extends Comparable<E>> {
        Object element;
        Node<E> left;
        Node<E> right;

        public Node() {

        }

        public Node(Object element) {
            this.element = element;
        }

        public Node(Object element, Node<E> left, Node<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }

        public E getElement() {
            return (E) element;
        }
    }
}
