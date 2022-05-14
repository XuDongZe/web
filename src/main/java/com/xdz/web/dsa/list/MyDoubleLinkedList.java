package com.xdz.web.dsa.list;

import java.util.Iterator;

/**
 * Description: list的双向链表实现<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/14 0:04<br/>
 * Version: 1.0<br/>
 * <p>
 * each node has one prev and one next pointer, that is more like a list-seq than single-linked-list.
 * <p>
 * like single-linked-list, we use a head pointer to holder the list, and a tail pointer to convenient us
 * with tail-operations: removeLast insertLast, which is used a lot in doubled-queue.
 */
@SuppressWarnings("ALL")
public class MyDoubleLinkedList<E> implements IMyList<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public MyDoubleLinkedList() {
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    @Override
    public E get(int idx) {
        return (E) getNode(idx).element;
    }

    @Override
    public void set(int idx, E e) {
        Node<E> node = getNode(idx);
        node.element = e;
    }

    /**
     * 在位置idx处增加新元素e
     * 实质上是在位置idx指示的旧节点 前 插入新节点，新节点值为e
     */
    @Override
    public void add(int idx, E e) {
        insertBefore(getNode(idx), e);
    }

    @Override
    public E remove(int idx) {
        return remove(getNode(idx));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyDoubleLinkedListIterator<>(this);
    }

    public void insertFirst(E e) {
        insertAfter(head, e);
    }

    public void insertLast(E e) {
        insertBefore(tail, e);
    }

    public E removeFirst() {
        return remove(head.next);
    }

    public E removeLast() {
        return remove(tail.prev);
    }

    /**
     * idx valid range is [-1, size]
     * @param idx -1 for head and size for tail
     * @return
     */
    private Node<E> getNode(int idx) {
        if (idx < -1 || idx > size) {
            throw new RuntimeException("idx is not valid");
        }

        int tailIdx = size - 1 - idx;
        if (tailIdx < idx) {
            return getNodeFromTail(tailIdx);
        } else {
            return getNodeFromHead(idx);
        }
    }

    private Node<E> getNodeFromTail(int idx) {
        int i = -1;
        Node<E> p = tail;
        while (i < idx && p != null) {
            p = p.prev;
            i ++;
        }
        return p;
    }

    private Node<E> getNodeFromHead(int idx) {
        int i = -1;
        Node<E> p = head;
        while (i < idx && p != null) {
            p = p.next;
            i ++;
        }
        return p;
    }

    public Node<E> prev(Node<E> node) {
        return node.prev;
    }

    public Node<E> next(Node<E> node) {
        return node.next;
    }

    public void insertBefore(Node<E> node, E e) {
        Node<E> newNode = new Node<>(e);
        newNode.next = node;
        newNode.prev = node.prev;
        node.prev.next = newNode;
        node.prev = newNode;
        size ++;
    }

    public void insertAfter(Node<E> node, E e) {
        insertBefore(node.next, e);
    }

    public E remove(Node<E> node) {
        E e = (E) node.element;
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size --;
        return e;
    }

    private static class Node<E> {
        private Object element;
        private Node<E> prev;
        private Node<E> next;

        public Node() {
            element = prev = next = null;
        }

        public Node(E e) {
            element = e;
        }
    }

    private static class MyDoubleLinkedListIterator<E> implements Iterator<E> {

        private Node<E> p;
        private MyDoubleLinkedList<E> list;

        public MyDoubleLinkedListIterator(MyDoubleLinkedList<E> list) {
            p = list.head;
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return p.next != list.tail;
        }

        @Override
        public E next() {
            p = p.next;
            return (E) p.element;
        }
    }

    public void exchange(int i, int j) {
        exchange(getNode(i), getNode(j));
    }

    private void exchange(Node<E> p, Node<E> q) {
        Node<E> pp = prev(p);
        Node<E> pq = prev(q);
        Node<E> np = next(p);
        Node<E> nq = next(q);
        pp.next = q;
        q.prev = pp;
        pq.next = p;
        p.prev = pq;

        np.prev = q;
        q.next = np;
        nq.prev = p;
        p.next = nq;
    }

    public boolean contains(E e) {
        Node<E> p = head.next;
        while (p != tail) {
            if ((e == null && p.element == null) || e.equals(p.element)) {
                return true;
            }
            p = p.next;
        }
        return false;
    }

    public static void main(String[] args){
        MyDoubleLinkedList<Integer> list = new MyDoubleLinkedList<>();
        // add(idx)
        // get
        // set
        // remove
        list.add(0, 1);
        list.add(0, 2);
        list.add(0, 3);
        list.exchange(list.getNode(0), list.getNode(2));
        boolean contains = list.contains(1);
        contains = list.contains(-1);
        Integer integer = list.get(0);
        integer = list.get(1);
        integer = list.get(2);
        list.set(0, 4);
        list.set(0, 5);
        list.remove(2);
        list.remove(1);
        list.remove(0);

        // insert
        list.insertFirst(1);
        list.insertFirst(2);
        list.insertFirst(3);
        list.insertLast(4);
        list.insertLast(5);
        list.insertLast(6);

        // remove
        list.removeFirst();
        list.removeFirst();
        list.removeFirst();
        list.removeLast();
        list.removeLast();
        list.removeLast();

        // inner func
        list.insertLast(1);
        Node node = list.getNode(0);
        list.insertAfter(node, 2);
        list.insertBefore(node, 3);
        list.remove(node);

        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int e = it.next();
        }
    }
}
