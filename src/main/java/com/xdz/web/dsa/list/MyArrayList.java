package com.xdz.web.dsa.list;

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
public class MyArrayList<T> implements Iterable<T> {
    private Object[] array;
    private int capacity;
    private int size;

    public MyArrayList(int capacity) {
        array = new Object[capacity];
        this.capacity = capacity;
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public T get(int idx) {
        return (T) array[idx];
    }

    public void set(int idx, T value) {
        array[idx] = value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private void ensureCapacity(int newCapacity) {
        if (this.capacity >= newCapacity) {
            return;
        }

        Object[] newArray = new Object[newCapacity];
        for (int i = 0; i < size; i ++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void add(int index, T value) {
        ensureCapacity(size + 1);
        for (int i = size - 1; i >= index; i --) {
            array[i + 1] = array[i];
        }
        array[index] = value;
        size ++;
    }

    public void remove(int index) {
        for (int i = index + 1; i < size; i ++) {
            array[i - 1] = array[i];
        }
        size --;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyArrayListIterator(this);
    }

    private class MyArrayListIterator implements Iterator<T> {

        private int pos = 0;
        private MyArrayList<T> list;

        public MyArrayListIterator(MyArrayList<T> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return pos < size();
        }

        @Override
        public T next() {
            return list.get(pos++);
        }

        @Override
        public void remove() {
            list.remove(pos--);
        }
    }
}
