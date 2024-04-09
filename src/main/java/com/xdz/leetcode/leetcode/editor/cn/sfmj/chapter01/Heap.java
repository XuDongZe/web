package com.xdz.leetcode.leetcode.editor.cn.sfmj.chapter01;

import java.util.Arrays;

/**
 * <pre>
 * 优先队列 堆
 *
 * 有序数组：查询、修改O(1) 删除、增加O(n)
 * 有没有什么办法优化呢？让增删改查的复杂度都可以接受
 *
 * 答案是使用树状结构
 * log n复杂度删除、增加元素
 * log n复杂度查询、修改元素
 *
 * 并且由于是有序的 可以快速的查找最值(最大值or最小值)
 *
 * 堆的基本结构是 树状的 有序 数组
 * 在增加、删除、修改元素后 通过一个调整操作 保持有序
 *
 * 堆的基本操作有：
 * 1. 快速获取堆顶元素：最值 O(1)
 * 2. 插入一个元素到堆中
 * 3. 从堆中删除一个元素
 *
 * 我们在示例中默认堆中元素是不重复的
 *
 * 节点编号采用 从1开始 则 1 2 3
 * parent = child / 2
 * left-child = 2 * p, right-child = 2 * p + 1;
 *
 * 节点编号采用 从0开始 则 0 1 2
 * parent = (child - 1) / 2 if (child >= 1)
 * left-child = 2 * p + 1, right-child = 2 * p + 2
 *
 * 构造小顶堆
 * </pre>
 */
public class Heap {
    private int[] data;
    private int size; // 堆中元素的数量

    public Heap(int cap) {
        this.data = new int[cap];
        size = 0;
    }

    /**
     * 插入一个元素到堆
     */
    public void add(int v) {
        if (size >= data.length) {
            data = Arrays.copyOf(data, (data.length) << 1);
        }
        data[size] = v;
//        up(size);
        __dfsUp(size);
        size ++;
    }

    /**
     * 获取堆顶元素
     */
    public int top() {
        return data[0];
    }

    /**
     * 删除堆顶元素
     */
    public void remove() {
        // 移花接木
        data[0] = data[size - 1];
//        down(0);
        __dfsDown(0);
        size --;
    }

    /**
     * i节点处不符合堆的规范 从i节点向上调整
     */
    private void up(int i) {
        int p = (i - 1) / 2;
        while (p > 0 && data[i] < data[p]) {
            swap(i, p);
            i = p;
            p = i / 2;
        }
    }

    private void __dfsUp(int i) {
        int p = (i - 1) / 2;
        if (p > 0 && data[i] < data[p]) {
            swap(i, p);
            __dfsUp(p);
        }
    }

    /**
     * 节点i破坏了堆的性质 所以从节点i开始往下调整恢复
     */
    private void down(int i) {
        // [i]太大 调整i到合适的位置
        // 结束条件: 子节点没有了 或者 比子节点都小
        int l = i * 2 + 1, r = i * 2 + 2;
        while (l < size) {
            int minIdx = l;
            if (r < size && data[r] < data[l]) minIdx = r;
            if (data[i] <= data[minIdx]) {
                break;
            } else {
                swap(i, minIdx);
                i = minIdx;
                l = i * 2 + 1;
                r = l + 1;
            }
        }
    }

    private void __dfsDown(int i) {
        int l = i * 2 + 1, r = i * 2 + 2;
        if (l >= size) {
            return;
        }
        int minIdx = l;
        if (r < size && data[r] < data[l]) minIdx = r;
        if (data[i] <= data[minIdx]) {
            return;
        }
        swap(i, minIdx);
        __dfsDown(minIdx);
    }

    private void swap(int i, int j) {
        int t = data[i];
        data[i] = data[j];
        data[j] = t;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        Heap heap = new Heap(nums.length / 2);
        for (int n : nums) {
            heap.add(n);
        }
        while (heap.size > 0) {
            int top = heap.top();
            System.out.println(top);
            heap.remove();
        }
    }
}
