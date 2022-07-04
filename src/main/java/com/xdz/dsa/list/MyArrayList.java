package com.xdz.dsa.list;

import java.util.Iterator;
import java.util.Objects;

/**
 * Description: implement a list using array.
 * 1. using an array, hold this array's reference, capacity and size.
 * 2. dynamic increment
 * 3. random access by index: get set
 * 4. basic func: size, isEmpty
 * 5. add remove by index
 * 6. iterator
 * Author: dongze.xu<br/>
 * Date: 2022/5/9 20:43<br/>
 * Version: 1.0<br/>
 */
public class MyArrayList<E> implements IMyList<E> {
    private Object[] array;
    private int capacity;
    private int size;

    public MyArrayList(int capacity) {
        array = new Object[capacity];
        this.capacity = capacity;
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int idx) {
        return (E) array[idx];
    }

    @Override
    public void set(int idx, E value) {
        array[idx] = value;
    }

    @Override
    public void add(int index, E value) {
        ensureCapacity(size + 1);
        for (int i = size - 1; i >= index; i--) {
            array[i + 1] = array[i];
        }
        array[index] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        E e = (E) array[index];
        for (int i = index + 1; i < size; i++) {
            array[i - 1] = array[i];
        }
        array[size - 1] = null;
        size--;
        return e;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyArrayListIterator(this);
    }

    public Iterator<E> reverseIterator() {
        return new MySingleLinkedListReverseIterator<>(this);
    }

    private void ensureCapacity(int newCapacity) {
        if (this.capacity >= newCapacity) {
            return;
        }

        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    private class MyArrayListIterator implements Iterator<E> {

        private int pos = 0;
        private MyArrayList<E> list;

        public MyArrayListIterator(MyArrayList<E> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return pos < size();
        }

        @Override
        public E next() {
            return list.get(pos++);
        }
    }

    private static class MySingleLinkedListReverseIterator<E> implements Iterator<E> {

        private int pos;
        private MyArrayList<E> list;

        public MySingleLinkedListReverseIterator(MyArrayList<E> list) {
            this.list = list;
            this.pos = list.size - 1;
        }

        @Override
        public boolean hasNext() {
            return pos > 0;
        }

        @Override
        public E next() {
            return list.get(pos--);
        }
    }

    public static void main(String[] args) {
        MyArrayList<Integer> list = new MyArrayList<>(3);
        list.add(0, 1);
        list.add(0, 2);
        list.add(0, 3);
        list.set(0, 4);
        int i = list.get(0);
        list.remove(0);
        list.remove(1);
        list.remove(0);

        list.add(0, 1);
        list.add(0, 2);
        list.add(0, 3);
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int e = it.next();
        }

        list.add(0, 1);
        list.add(0, 2);
        list.add(0, 3);
        Iterator<Integer> reverseIt = list.reverseIterator();
        while (reverseIt.hasNext()) {
            int e = reverseIt.next();
        }
    }
}
