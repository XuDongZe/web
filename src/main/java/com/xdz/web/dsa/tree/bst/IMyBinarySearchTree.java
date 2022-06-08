package com.xdz.web.dsa.tree.bst;

import com.xdz.web.dsa.list.IMyList;
import com.xdz.web.dsa.tree.bt.IMyBinaryTree;

public interface IMyBinarySearchTree<E extends Comparable<E>> extends IMyBinaryTree<E> {

    Node<E> findMin();

    Node<E> findMax();
}
