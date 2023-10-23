package com.xdz.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Description: 多线程的处理器<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/7/20 15:38<br/>
 * Version: 1.0<br/>
 */
public class MultiTaskHandler {

    private ExecutorService executor;

    public static final MultiTaskHandler INSTANCE = new MultiTaskHandler(30);

    public MultiTaskHandler(int nThreads) {
        executor = Executors.newFixedThreadPool(nThreads);
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    // total timeout = timeoutMs * tasks.size()
    public <T> List<T> commitAndWaitAll(List<Callable<T>> tasks, long timeoutMs) {
        List<T> result = Lists.newArrayList();

        List<Future<T>> futures = Lists.newArrayList();
        for (Callable<T> t : tasks) {
            Future<T> future = executor.submit(t);
            futures.add(future);
        }

        CountDownLatch latch = new CountDownLatch(futures.size());
        for (Future<T> f : futures) {
            T t = null;
            try {
                t = f.get(timeoutMs, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
                f.cancel(true);
            } finally {
                latch.countDown();
            }
            if (t != null) {
                result.add(t);
            }
        }
        try {
            latch.await(timeoutMs * futures.size(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

}
