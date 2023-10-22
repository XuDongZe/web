package com.xdz.dsa.Queue;

import java.util.Iterator;

public interface IMyQueue<E> extends Iterable<Integer> {
    /**
     * push one element to queue, which means add the element to the queue's tail
     * then the tail step one.
     * @param e the pushed element
     */
    void enqueue(E e);

    /**
     * remove the head element.
     * then head moved to next queued one.
     * @return the removed head element.
     */
    E dequeue();

    /**
     * @return the queue's element count
     */
    int size();

    /**
     * @return size() == 0
     */
    boolean isEmpty();

    /**
     * @return if the queue is not limited just return false.
     */
    boolean isFull();

    default String __toString() {
        String res = "[";
        Iterator<Integer> it = iterator();
        while (it.hasNext()) {
            res += (it.next() + ",");
        }
        if (res.charAt(res.length() - 1) == ',') {
            res = res.substring(0, res.length() - 1);
        }
        res += "]";
        return res;
    }

    static void test(IMyQueue<Integer> queue) {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue());
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
    }
}
