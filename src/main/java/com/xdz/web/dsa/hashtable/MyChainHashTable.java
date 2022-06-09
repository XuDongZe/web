package com.xdz.web.dsa.hashtable;

import com.xdz.web.dsa.list.MyDoubleLinkedList;
import com.xdz.web.util.MathUtil;

import java.util.Iterator;

/**
 * Description: chain hash table for collision<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/8 23:41<br/>
 * Version: 1.0<br/>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MyChainHashTable<E> implements IMyHashTable<E> {

    /**
     * a large-enough prime number
     */
    private static final int DEFAULT_TABLE_CAPACITY = 101;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    /**
     * array for list storage
     * we use list with head, maybe we can store head node in array, so save one node space for each list. and save capacity * node space total.
     */
    private MyDoubleLinkedList<E>[] array;
    /**
     * element max count
     */
    private int capacity;
    /**
     * the load factor param
     */
    private double loadFactor;
    /**
     * element count
     */
    private int elementCount;

    public MyChainHashTable() {
        this(DEFAULT_TABLE_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyChainHashTable(int capacity, double loadFactor) {
        this.capacity = MathUtil.nextPrime(capacity);
        array = new MyDoubleLinkedList[this.capacity];
        // init all list in constructor, or maybe you want use lazy-init.
        // we use this to avoid null-judge, so easy the algo-implement and focus on the core idea of hash table.
        // we can easily convert to lazy-init model by modify the insert, remove, contains and constructor func.
        for (int i = 0; i < this.capacity; i++) {
            // init empty list
            array[i] = new MyDoubleLinkedList<>();
        }
        this.loadFactor = loadFactor;
    }

    @Override
    public boolean contains(E e) {
        MyDoubleLinkedList<E> list = array[hash(e)];
        Iterator<E> it = list.iterator();
        while (it.hasNext()) {
            E el = it.next();
            if (same(e, el)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean insert(E e) {
        if (contains(e)) {
            // already exist
            return false;
        }
        array[hash(e)].addFirst(e);
        elementCount++;
        // there has too many elements for current table capacity
        if ((int) (capacity * loadFactor) <= elementCount) {
            rehash();
        }
        return true;
    }

    @Override
    public boolean remove(E e) {
        MyDoubleLinkedList<E> list = array[hash(e)];
        Iterator<E> it = list.iterator();
        while (it.hasNext()) {
            E el = it.next();
            if (same(e, el)) {
                // do remove
                it.remove();
                elementCount--;
                return true;
            }
        }
        // already remove
        return false;
    }

    private void rehash() {
        MyChainHashTable<E> newTable = new MyChainHashTable<E>(capacity * 2, loadFactor);
        // insert all. O(elementCount) and use extra space for data-move.
        for (MyDoubleLinkedList<E> list : this.array) {
            for (E e : list) {
                newTable.insert(e);
            }
        }
        this.array = newTable.array;
        this.capacity = newTable.capacity;
        this.elementCount = newTable.elementCount;
        this.loadFactor = newTable.loadFactor;
    }

    private int hash(E e) {
        int hash = e.hashCode();
        hash = hash % capacity;
        if (hash < 0) {
            hash += capacity;
        }
        return hash;
    }

    public static void main(String[] args) {
        IMyHashTable<Integer> table = new MyChainHashTable<>(11, 0.75);
        for (int i = 0; i < 20; i ++) {
            table.insert(i);
        }
        boolean result = table.contains(1);
        result = table.contains(31);
        result = table.insert(11);
        result = table.insert(31);
        result = table.contains(31);
        result = table.remove(31);
        result = table.remove(31);
        result = table.contains(31);
    }
}
