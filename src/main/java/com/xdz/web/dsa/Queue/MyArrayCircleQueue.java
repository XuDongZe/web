package com.xdz.web.dsa.Queue;

import com.xdz.web.dsa.exception.MyArrayEmptyException;
import com.xdz.web.dsa.exception.MyArrayFullException;

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

    public MyArrayCircleQueue(int capacity) {
        this.capacity = capacity;
        data = new Object[capacity + 1];
        head = tail = 0;
    }

    @Override
    public void enqueue(E e) {
        if (isFull()) {
            throw new MyArrayFullException();
        }
        data[tail] = e;
        // or capacity + 1: the actual tail pointer moved length at one circle
        tail = (tail + 1) % data.length;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new MyArrayEmptyException();
        }
        E e = (E) data[head];
        data[head] = null; // help gc
        head = (head + 1) % data.length;
        return e;
    }

    @Override
    public int size() {
        if (head < tail) {
            return tail - head;
        } else {
            return tail - head + data.length;
        }
    }

    @Override
    public boolean isEmpty() {
        return tail == head;
    }

    @Override
    public boolean isFull() {
        return (tail + 1) % data.length == head;
    }

    public static void main(String[] args) {
        IMyQueue<Integer> queue = new MyArrayCircleQueue<>(3);
        IMyQueue.test(queue);
    }
}
