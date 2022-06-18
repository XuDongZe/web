package com.xdz.dsa.Queue;

public interface IMyQueue<E> {
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

    static void test(IMyQueue<Integer> queue) {
         queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        System.out.println(queue.dequeue());
        queue.enqueue(4);
        System.out.println(queue.dequeue());
        queue.enqueue(5);
        System.out.println(queue.dequeue());
        queue.enqueue(6);
    }
}
