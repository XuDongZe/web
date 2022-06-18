package com.xdz.dsa.tree.bst;

import com.xdz.dsa.tree.bt.IMyBinaryTree;

public interface IMyBinarySearchTree<E extends Comparable<E>> extends IMyBinaryTree<E> {

    Node<E> findMin();

    Node<E> findMax();
}
