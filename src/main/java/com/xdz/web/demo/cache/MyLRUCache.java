package com.xdz.web.demo.cache;

/**
 * Description:
 * 线程安全: 读写锁<br/>
 * LRU过期.
 * Author: dongze.xu<br/>
 * Date: 2021/12/10 14:49<br/>
 * Version: 1.0<br/>
 */

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

/**
 * LRU: Least Recently Used. 最近最少使用.
 * 元素的访问(读或写) 会使元素变为最近使用的元素.
 */
public class MyLRUCache extends LinkedHashMap {
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private int maxSize;
    public MyLRUCache(int maxSize) {
        /**
         * accessOrder true for LRU
         */
        super(maxSize, 1.0f, true);
        this.maxSize = maxSize;
    }

    @Override
    public Object get(Object key) {
        readLock.lock();
        try {
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Object put(Object key, Object value) {
        writeLock.lock();
        try {
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxSize;
    }

    public static void main(String[] args){
        MyLRUCache cache = new MyLRUCache(3);
        for (int i = 0; i < 10; i ++) {
            cache.put(i, i);
            cache.get(1);
        }

        cache.forEach(new BiConsumer() {
            @Override
            public void accept(Object o, Object o2) {
                System.out.println(o + ", " + o2);
            }
        });
    }
}
