package com.xdz.dsa.list;

import java.util.Iterator;
import java.util.Objects;

/**
 * Description: 列表的单链表实现<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/13 11:10<br/>
 * Version: 1.0<br/>
 * <p>
 * we use an extra-empty node, hold the list. we call it head node.
 * and we let the last node point to null, to indicate that list is at end.
 * <p>
 * the head node make all node is the same with each other, include first and last node:
 * they all have a logic prev node and a next node. and this will convenient us a lot:
 * <p>
 * assume that we remove one node, and it's prev node is p. if no head-node here, we need to consider that if p is null,
 * which occur on node is the first one, the code will be:
 * <pre>
 *     if (list.next == null) {
 *         list = node.next;
 *     } else if {
 *         p.next = node.next;
 *     }
 * </pre>
 * if we have a head node in list as first one, and as the list's point holder. just do that:
 * <pre>
 *     p.next = p.next.next
 * </pre>
 * because we know p is not null forever.
 * <p>
 * for idx location, head node is -1, and first node (head.next) is 0.
 * size is element count, so valid access idx is [0, size - 1]
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MySingleLinkedList<E> implements IMyList<E> {
    /**
     * we use it to hold the list
     */
    private Node<E> head;
    /**
     * we use it to hold the element count of list, so size() is O(1)
     * also the next pos to insert.
     * so add range check: [0, size]
     * get/set range check: [0, size)
     */
    private int size;

    public MySingleLinkedList() {
        head = new Node<>();
        size = 0;
    }

    @Override
    public E get(int idx) {
        Node<E> node = getNode(idx);
        return (E) node.element;
    }

    @Override
    public void set(int idx, E e) {
        Node<E> node = getNode(idx);
        node.element = e;
    }

    @Override
    public void add(int idx, E e) {
        insertAfter(getNode(idx - 1), e);
    }

    @Override
    public E remove(int idx) {
        return removeByPrev(getNode(idx - 1));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new MySingleLinkedListIterator(this.head);
    }

    /**
     * 在node节点前插入新节点.
     * 单向列表不推荐使用这种。
     */
    @Deprecated
    private void insertBefore(Node<E> node, E element) {
        Node<E> p = prev(node);
        if (p == null) {
            throw new RuntimeException("node is not in list");
        }
        insertAfter(p, element);
    }

    /**
     * idx == -1, head
     * idx == 0, head.next
     */
    private Node<E> getNode(int idx) {
        if (idx == -1) {
            return head;
        }

        if (idx < 0 || idx >= size) {
            throw new RuntimeException("get from list, index is illegal: " + idx);
        }

        int i = -1;
        Node<E> node = head;
        while (i < idx) {
            // loop for idx count
            node = node.next;
            i++;
        }
        // i: -1 => idx
        // node: head => node
        // so node is list[idx] if we define list[head] is -1.
        return node;
    }

    /**
     * prev.next = node
     */
    private E removeByPrev(Node<E> prev) {
        Node<E> node = prev.next;
        prev.next = prev.next.next;
        size--;
        return (E) node.element;
    }

    /**
     * 在node节点后插入新节点
     */
    private void insertAfter(Node<E> node, E element) {
        Node<E> newNode = new Node<>(element);
        newNode.next = node.next;
        node.next = newNode;
        size++;
    }

    /**
     * 删除node节点
     */
    @Deprecated
    private E remove(Node<E> node) {
        Node<E> p = prev(node);
        if (p == null) {
            throw new RuntimeException("node is not in list");
        }
        return removeByPrev(p);
    }

    private Node<E> prev(Node<E> node) {
        Node<E> p = head;
        while (p != null && p.next != node) {
            p = p.next;
        }
        // now p == null => node is not in list || p.next = node => p is node.prev
        return p;
    }

    private Node<E> next(Node<E> node) {
        return node == null ? null : node.next;
    }

    private static class MySingleLinkedListIterator<E> implements Iterator<E> {

        /**
         * 当前节点指针, 初始化为-1, 即head节点
         */
        private Node<E> p;

        public MySingleLinkedListIterator(Node<E> head) {
            if (head == null) {
                throw new RuntimeException("head pointer is null");
            }
            p = head;
        }

        @Override
        public boolean hasNext() {
            return p.next != null;
        }

        @Override
        public E next() {
            p = p.next;
            return (E) p.element;
        }
    }

    private static class Node<E> {
        Object element;
        private Node<E> next;

        public Node() {
            this.element = null;
            this.next = null;
        }

        public Node(E e) {
            this.element = e;
            next = null;
        }

        public Node(E e, Node<E> next) {
            this.element = e;
            this.next = next;
        }
    }

    public void exchange(int i, int j) {
        exchange(getNode(i), getNode(j));
    }

    private void exchange(Node<E> p, Node<E> q) {
        // p != head && q != head && p != q
        Node<E> pp = prev(p), pq = prev(q);
        pp.next = q;
        pq.next = p;
        Node<E> tmp = p.next;
        p.next = q.next;
        q.next = tmp;
    }

    public static void main(String[] args) {
        MySingleLinkedList<Integer> list = new MySingleLinkedList<>();
        // add(idx)
        // get
        // set
        // remove
        list.add(0, 1);
        list.add(0, 2);
        list.add(0, 3);
        list.exchange(list.getNode(0), list.getNode(1));
        Integer integer = list.get(0);
        integer = list.get(1);
        integer = list.get(2);
        list.set(0, 4);
        list.set(0, 5);
        list.remove(2);
        list.remove(1);
        list.remove(0);

        // insert
        list.addFirst(1);
        list.addFirst(2);
        list.addFirst(3);
        list.addLast(4);
        list.addLast(5);
        list.addLast(6);

        // remove
        list.removeFirst();
        list.removeFirst();
        list.removeLast();
        list.removeLast();

        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int e = it.next();
        }
    }
}
