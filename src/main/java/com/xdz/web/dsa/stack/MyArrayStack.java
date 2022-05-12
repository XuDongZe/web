package com.xdz.web.dsa.stack;

import com.xdz.web.dsa.exception.MyArrayEmptyException;

import java.util.Arrays;

/**
 * Description: 数组栈<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/5/10 17:48<br/>
 * Version: 1.0<br/>
 */
public class MyArrayStack<E> implements IMyStack<E> {
    /**
     * storage of elements
     */
    private Object[] data;
    /**
     * the capacity of stack
     */
    private int capacity;
    /**
     * the element nums of stack.
     * size is the pointer to insert
     * size - 1 is the pointer to last inserted-element, or that is stack's top
     */
    private int size;

    private static final int DEFAULT_CAPACITY = 8;
    /**
     * if the capacity is less than this, double capacity
     * else add
     */
    private static final int BASIC_CAPACITY = 1024 * 1024;

    public MyArrayStack() {
        data = new Object[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        size = 0;
    }

    @Override
    public void push(E e) {
        if (size >= capacity) {
            growth(size * 2);
        }
        data[size] = e;
        size ++;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new MyArrayEmptyException();
        }
        Object o = data[size - 1];
        // remove the last one
        data[size - 1] = null; // help gc
        size --;
        return (E) o;
    }

    @Override
    public E top() {
        if (isEmpty()) {
            throw new MyArrayEmptyException();
        }
        return (E) data[size - 1];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void growth(int newCapacity) {
        if (capacity >= newCapacity) {
            return;
        }
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
        capacity = newCapacity;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }

    public static void main(String[] args){
        MyArrayStack<Integer> stack = new MyArrayStack<>();
        stack.push(1);
        System.out.println(stack);
        stack.push(2);
        System.out.println(stack);
        stack.push(3);
        System.out.println(stack);
        // now stack is [1,2,3] and top is 3
        assert stack.top() == 3;
        assert stack.pop() == 3; assert stack.size() == 2;
        System.out.println(stack);

        assert stack.pop() == 2; assert stack.size() == 1;
        System.out.println(stack);

        assert stack.pop() == 1; assert stack.size() == 0;
        System.out.println(stack);

        assert stack.isEmpty();
    }
}
