package com.xdz.dsa.Queue;

import com.xdz.dsa.exception.MyArrayEmptyException;
import com.xdz.dsa.exception.MyArrayFullException;
import org.bouncycastle.util.Iterable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Description: 数组实现的队列<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/12 20:53<br/>
 * Version: 1.0<br/>
 */
public class MyArrayQueue<E> implements IMyQueue<E> {
    private Object[] data;
    private int capacity;

    private int head;
    private int tail;

    public MyArrayQueue(int capacity) {
        data = new Object[capacity];
        this.capacity = capacity;
        head = tail = 0;
    }

    @Override
    public void enqueue(E e) {
        if (isFull()) {
            throw new MyArrayFullException();
        }
        if (tail == capacity) {
            // 队列有空间，但是tail满了。数据搬迁到数组起始位置
            for (int i = head; i < tail; i++) {
                data[i - 1] = data[i];
            }
            tail -= head;
            head = 0;
        }
        data[tail++] = e;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new MyArrayEmptyException();
        }
        E e = (E) data[head];
        data[head] = null; // help gcs
        head++;
        return e;
    }

    @Override
    public int size() {
        return tail - head;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isFull() {
        return size() >= capacity;
    }

    @Override
    public String toString() {
        return __toString();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new MyArrayQueueIterator<>();
    }

    private class MyArrayQueueIterator<E> implements Iterator<E> {
        private int h;
        private int t;

        public MyArrayQueueIterator() {
            this.h = head;
            this.t = tail;
        }

        @Override
        public boolean hasNext() {
            return t > h;
        }

        @Override
        public E next() {
            return (E) data[h++];
        }
    }

    public static void main(String[] args) {
        MyArrayQueue<Integer> queue = new MyArrayQueue<>(3);
        IMyQueue.test(queue);

        System.out.println(queue);
    }
}
