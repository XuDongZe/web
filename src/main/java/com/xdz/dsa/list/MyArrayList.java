package com.xdz.dsa.list;

import com.xdz.dsa.exception.MyArrayEmptyException;

import java.util.Iterator;

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
        checkAccess(idx);
        return (E) array[idx];
    }

    @Override
    public void set(int idx, E value) {
        checkAccess(idx);
        array[idx] = value;
    }

    @Override
    public void add(int index, E value) {
        checkInsert(index);
        ensureCapacity(size + 1);
        // [index,...] => [index+1,...]
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
        // [index+1,...] => [index,...]
        for (int i = index + 1; i < size; i++) {
            array[i - 1] = array[i];
        }
        // help gc
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
        return new MyArrayListIterator();
    }

    public Iterator<E> reverseIterator() {
        return new MyArrayListReverseIterator();
    }

    private void ensureCapacity(int newCapacity) {
        if (this.capacity >= newCapacity) {
            return;
        }

        newCapacity = Math.max(2 * capacity, newCapacity);

        Object[] newArray = new Object[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
        capacity = newCapacity;
    }

    private class MyArrayListIterator implements Iterator<E> {
        private int pos = 0;

        public MyArrayListIterator() {
        }

        @Override
        public boolean hasNext() {
            return pos < size();
        }

        @Override
        public E next() {
            return get(pos++);
        }
    }

    private class MyArrayListReverseIterator implements Iterator<E> {
        private int pos;

        public MyArrayListReverseIterator() {
            if (isEmpty()) {
                throw new MyArrayEmptyException();
            }
            this.pos = size() - 1;
        }

        @Override
        public boolean hasNext() {
            return pos > 0;
        }

        @Override
        public E next() {
            return get(pos--);
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
