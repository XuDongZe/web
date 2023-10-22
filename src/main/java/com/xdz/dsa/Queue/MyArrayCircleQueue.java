package com.xdz.dsa.Queue;

import com.xdz.dsa.exception.MyArrayEmptyException;
import com.xdz.dsa.exception.MyArrayFullException;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Description: 数组实现的循环队列<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/12 20:54<br/>
 * Version: 1.0<br/>
 * <p>
 * we use a data array which the last one space is not used. for this:
 * head == tail == 0 is the init status, so queue empty is head == tail.
 * <p>
 * when enqueue, we do: data[tail ++] = e:
 * <li> first we put element e to index tail, so tail is the pointer to enqueued-element.</li>
 * <li> second let tail to step one to next enqueued-el. but on the condition that the next-one space is not used.</li>
 * if queue is only one empty space left, then the condition is not fit.
 * <p>
 * assume that we enqueue in a queue which just has one empty space. after the enqueue op,
 * tail will step one then it will be the same with head: and that's an empty queue!
 * <p>
 * so we left one space and not use it. it will be a sentinel: when tail point to the only space, we now know queue is full.
 * the only space is what? that is the prev space of head, so we got:
 * <li>full:    (tail + 1) % capacity == head</li>
 * <li>empty:   tail == head</li>
 */
public class MyArrayCircleQueue<E> implements IMyQueue<E> {

    private Object[] data;
    private int capacity;
    private int head;
    private int tail;
    private int size;

    public MyArrayCircleQueue(int capacity) {
        this.capacity = capacity;
        data = new Object[capacity];
        head = tail = 0;
        size = 0;
    }

    @Override
    public void enqueue(E e) {
        if (isFull()) {
            throw new MyArrayFullException();
        }
        data[tail] = e;
        // or capacity + 1: the actual tail pointer moved length at one circle
        tail = (tail + 1) % capacity;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new MyArrayEmptyException();
        }
        E e = (E) data[head];
        data[head] = null; // help gc
        head = (head + 1) % capacity;
        size --;
        return e;
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
    public boolean isFull() {
        return size() == capacity;
    }

    @Override
    public String toString() {
        return __toString();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new MyArrayCircleQueueIterator<>();
    }

    private class MyArrayCircleQueueIterator<E> implements Iterator<E> {

        private int h;
        private int t;
        private int cap;
        private int sz;

        private MyArrayCircleQueueIterator() {
            this.h = head;
            this.t = tail;
            this.cap = capacity;
            this.sz = size;
        }

        @Override
        public boolean hasNext() {
            return sz > 0;
        }

        @Override
        public E next() {
            E e = (E) data[h];
            h = (h + 1) % cap;
            sz --;
            return e;
        }
    }

    public static void main(String[] args) {
        IMyQueue<Integer> queue = new MyArrayCircleQueue<>(3);
        IMyQueue.test(queue);

        System.out.println(queue);
    }
}
