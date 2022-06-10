package com.xdz.web.dsa.heap;

import jnr.ffi.annotations.In;

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
     * root val is min
     */
    private boolean minHeap;

    public MyHeap() {
        this(true);
    }

    public MyHeap(boolean minHeap) {
        this.minHeap = minHeap;
        clear();
    }

    @Override
    public void insert(E e) {
        ensureCapacity();
        // hole percolate up
        int hole = ++size, parent;
        // find e > array[parent] then set array[hole] = e. parent = parentIdx(hole).
        // hold array[hole] un-used
        for (; hole > 1; hole = parent) {
            boolean parentLarger = e.compareTo(elementAt(parent = parentIdx(hole))) < 0;
            if ((minHeap && parentLarger) || (!minHeap && !parentLarger)) {
                array[hole] = array[parent];
            } else {
                break;
            }
        }
        array[hole] = e;
    }

    @Override
    public E pop() {
        E top = top();
        E tmp = (E) (array[1] = (E) array[size--]);
        // percolate down from array[1]
        int hole = 1, child;
        for (; (child = leftChildIdx(hole)) <= size; hole = child) {
            // find min childIdx
            if (child < size) {
                boolean rightLarger = elementAt(child + 1).compareTo(elementAt(child)) > 0;
                if ((minHeap && !rightLarger) || (!minHeap && rightLarger)) {
                    child = child + 1;
                }
            }
            // find tmp < array[child] then set array[hole] = tmp; hole = parentIdx(child).
            // hold array[hole] un-used
            boolean childLarger = tmp.compareTo(elementAt(child)) < 0;
            if ((minHeap && !childLarger) || (!minHeap && childLarger)) {
                array[hole] = array[child];
            } else {
                break;
            }
        }
        array[hole] = tmp;
        // return old min value
        return top;
    }

    @Override
    public E top() {
        if (size == 0) {
            throw new RuntimeException("no element in heap");
        }
        return (E) array[1];
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

    private void ensureCapacity() {
        // array[0] is not used. so space-size = size + 1
        if (size + 1 >= array.length) {
            int newCapacity = array.length * 2 + 1;
            Object[] newArray = new Object[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    private int parentIdx(int idx) {
        return idx / 2;
    }

    private int leftChildIdx(int idx) {
        return idx * 2;
    }

    private int rightChildIdx(int idx) {
        return idx * 2 + 1;
    }

    private E elementAt(int idx) {
        return (E) array[idx];
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
    }
}
