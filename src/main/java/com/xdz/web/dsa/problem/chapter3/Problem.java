package com.xdz.web.dsa.problem.chapter3;

import com.xdz.web.dsa.Queue.IMyQueue;
import com.xdz.web.dsa.Queue.MyArrayCircleQueue;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/14 18:21<br/>
 * Version: 1.0<br/>
 */
public class Problem {
    /**
     * josephus. n for person's id: [1, n], m for pass times in each removed-loop.
     * <p>
     * game begin, all person circled with id's position. id 1 hold the very item.
     * <p>
     * game loop, one loop start from holder, then pass m times and each time
     * the item is passed to the next id. at the end of one loop, that's m passes
     * are all done. the current hold-person is removed.
     * <p>
     * find the last person remained.
     * <p>
     * we can use a circle-queue to mock: the holder is queue head.
     * <p>
     * game init: all persons enqueue with id.
     * <p>
     * game loop: one pass, head and tail step one: dequeue then enqueue.
     * <p>
     * one loop end: m passed done. the head will be removed: dequeue.
     * <p>
     * game end: queue's size() == 1
     * <p>
     * time: O(m + n). enqueue & dequeue is O(1), (m + n) times
     */
    public static int problem3_6_josephus(int m, int n) {
        if (m == 0) {
            return n;
        }

        IMyQueue<Integer> queue = new MyArrayCircleQueue<>(n);
        for (int id = 1; id <= n; id++) {
            queue.enqueue(id);
        }
        while (queue.size() > 1) {
            for (int i = 0; i < m; i++) {
                int head = queue.dequeue();
                queue.enqueue(head);
            }
            int id = queue.dequeue();
            System.out.print(id + ",");
        }
        return queue.dequeue();
    }


    // todo 3.23
    // todo 3.30
    // todo 3.31
    // todo 3.32
    // todo 3.35
    // todo 3.37


    public static void main(String[] args) {
        System.out.println(problem3_6_josephus(0, 5));
        System.out.println(problem3_6_josephus(1, 5));
    }
}
