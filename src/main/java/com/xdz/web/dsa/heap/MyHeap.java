package com.xdz.web.dsa.heap;

import java.util.Comparator;

/**
 * Description: priority-queue heap array implement<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/9 18:30<br/>
 * Version: 1.0<br/>
 *
 * <pre>
 * we use a binary-heap to implement priority-queue. the heap is a min-heap, means:
 * root.val < left.val && root.val < right.val
 *
 * for binary-heap, we can use a complete-binary-tree to implement, a complete-binary-tree is that:
 * each node has 2 full child node, unless it's the last-one-node's parent node.
 *
 * complete-binary-tree has beautiful and simple math feature:
 * if we give root node a virtual no 1, and root.left is 2 and root.right is 3. then:
 * if parent.no = i, then left no = 2 * i, right no = 2 * i + 1.
 * if child no = i, then parent no = i / 2.
 *
 * so we can use a simple none link-chain struct, just use an array, and storage node to the idx == node.no
 * that say, root node's value is stored at array[1], root.left.value is stored at array[2]...
 * the idx of element is the no of node in the mapping complete-binary-tree.
 *
 * if we insert or remove one element in heap, the heap's order feature maybe broken.
 * we just hold the core idea:
 *
 * 1. because the heap is a complete-binary-tree, so we can remove or insert in the tree's tail: that's
 * the next node space to use: we can not alloc a node in other space, because all node is full of child except the tail.
 *
 * 2. but we need to hold the order-feature of heap. so we must insert the element at a right position, but now we can just
 * make a new node space at last. so we must convert the op of tail-node to the right-position node.
 * also the remove operation, we can just remove the tail node to hold complete-binary-tree's feature, so we need
 * to convert the op of tail-node to the right position node.
 *
 * for insert(e)
 * 1. array[size + 1] is the next insert place, and array[size + 1] is un-used now, we call it hole.
 * 2. we percolate up from the current hole. we will hold two pointer: parent & child element when travel by percolate up.
 *   2.1 init hole = size + 1, parent = hole / 2;
 *   2.2 loop hole = parent (hole / 2)
 *   2.3 when loop do: find the first parent let array[parent = hole / 2] < e. so what will we do when it's false? that means
 *   what should we do if array[parent] > e ? now parent is not the final-parent we want, we iterate one step and find the new parent and check.
 *   that's:
 *      hole = parent;
 *      parent = hole / 2;
 *   and we need move hole-space to the hole idx pointer, that means let hole pointer space is un-used. so we do:
 *      array[hole] = array[parent]
 *   before hole & parent idx pointer iterate.
 *   2.4 finally we put array[hole] = e. so hole / 2 > e now. & let size = size + 1
 *
 * for deleteMin():
 * 1. array[1] is the min value.
 * 2. now array[1] is un-used(it will be deleted), so we move the tail node to hole(a right position).
 *   2.1 init hole = 1, child = hole * 2
 *   2.3 loop hole, when loop we do: find the first array[hole] let array[size] < array[minChildPos].
 *   so what should we do when array[size] > array[minChildPos]? now hole is not what we want, so iterate by:
 *      hole = minChildPos
 *      child = hole * 2;
 *   and also we need to hold that array[hole] is un-used. so:
 *      array[hole] = array[minChildPos]
 * 3. put array[size] to array[hole]
 *
 * we change root from array[1] to array[0].
 * </pre>
 */
@SuppressWarnings("unchecked")
public class MyHeap<E extends Comparable<E>> implements IMyHeap<E> {

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * array for data storage.
     */
    private Object[] array;
    /**
     * element amount
     */
    private int size;
    /**
     * element order cmp
     */
    private final Comparator<E> cmp;

    public MyHeap() {
        this(true);
    }

    public MyHeap(boolean minHeap) {
        this(new Object[DEFAULT_CAPACITY], 0, minHeap);
    }

    public MyHeap(Object[] array, int size, boolean minHeap) {
        this(array, size, minHeap ? Comparator.naturalOrder() : Comparator.reverseOrder());
    }

    public MyHeap(Object[] array, int size, Comparator<E> cmp) {
        this.array = array;
        this.size = size;
        this.cmp = cmp;
    }

    @Override
    public void insert(E e) {
        ensureCapacityForInsert();
        array[size] = e;
        // adjust size-node
        percolateUp(size++);
    }

    @Override
    public E pop() {
        E top = top();
        array[0] = array[--size];
        // adjust 1-node
        percolateDown(0);
        // return old min value
        return top;
    }

    @Override
    public E top() {
        if (size == 0) {
            throw new RuntimeException("no element in heap");
        }
        return (E) array[0];
    }

    @Override
    public void clear() {
        array = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * <pre>
     * percolate up from i-node. that means i-node maybe not the right position for its value.
     * we need to find a new position to place i-node value.
     * we use element step-by-step copy to stay the struct of an array.
     * </pre>
     */
    private void percolateUp(int i) {
        E tmp = elementAt(i);
        // find e > array[parent] then set array[hole] = e. parent = parentIdx(hole).
        // hold array[hole] un-used
        int hole = i, parent;
        for (; (parent = parentIdx(hole)) >= 0; hole = parent) {
            // parent is larger
            if (cmp.compare(tmp, elementAt(parent)) < 0) {
                array[hole] = array[parent];
            } else {
                break;
            }
        }
        array[hole] = tmp;
    }

    /**
     * <pre>
     * from i node, start percolate. that means i node now maybe not place here. we need
     * to find a new position to place the i-node val.
     * we use element step-by-step copy to stay the struct of an array.
     * </pre>
     */
    private void percolateDown(int i) {
        // percolate down from array[i]
        E tmp = elementAt(i);
        int hole = i, child;
        for (; (child = leftChildIdx(hole)) < size; hole = child) {
            // find min childIdx
            if (child + 1 < size && cmp.compare(elementAt(child + 1), elementAt(child)) < 0) {
                child = child + 1;
            }
            // find tmp < array[child] then set array[hole] = tmp; hole = parentIdx(child).
            // hold array[hole] un-used
            // parent is larger in min-heap
            if (cmp.compare(tmp, elementAt(child)) > 0) {
                array[hole] = array[child];
            } else {
                break;
            }
        }
        array[hole] = tmp;
    }

    private void ensureCapacityForInsert() {
        if (size >= array.length) {
            int newCapacity = array.length * 2 + 1;
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    private int parentIdx(int idx) {
        return (idx + 1) / 2 - 1;
    }

    private int leftChildIdx(int idx) {
        return idx * 2 + 1;
    }

    private int rightChildIdx(int idx) {
        return idx * 2 + 2;
    }

    private E elementAt(int idx) {
        return (E) array[idx];
    }

    /**
     * <pre>
     * complex: O(n).
     * we do not use n times insert op. todo
     * </pre>
     */
    public static <E extends Comparable<E>> MyHeap<E> create(E[] array, boolean minHeap) {
        Object[] newArray = new Object[array.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        MyHeap<E> newHeap = new MyHeap<>(newArray, array.length, minHeap);
        // adjust each parent node. from bottom to top.
        for (int i = newHeap.size() / 2; i >= 0; i--) {
            newHeap.percolateDown(i);
        }
        return newHeap;
    }

    public static void main(String[] args) {
        System.out.println("minHeap");
        IMyHeap<Integer> minHeap = new MyHeap<>();
        for (int i = 1; i < 10; i++) {
            minHeap.insert(10 - i);
        }
        while (!minHeap.isEmpty()) {
            System.out.println(minHeap.pop());
        }

        System.out.println("maxHeap");
        IMyHeap<Integer> maxHeap = new MyHeap<>(false);
        for (int i = 1; i < 10; i++) {
            maxHeap.insert(10 - i);
        }
        while (!maxHeap.isEmpty()) {
            System.out.println(maxHeap.pop());
        }

        Integer[] array = new Integer[]{8, 4, 6, 1, 4, 7, 26, 45, 178, 12};
        MyHeap<Integer> heap = MyHeap.create(array, true);
        System.out.println("create minHeap");
        while (!heap.isEmpty()) {
            System.out.println(heap.pop());
        }
        heap = MyHeap.create(array, false);
        System.out.println("create maxHeap");
        while (!heap.isEmpty()) {
            System.out.println(heap.pop());
        }
    }
}
