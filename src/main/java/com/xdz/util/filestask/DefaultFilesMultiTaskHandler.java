package com.xdz.util.filestask;

import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: 多任务处理器。<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/2/3 11:27<br/>
 * Version: 1.0<br/>
 */
@Getter
public class DefaultFilesMultiTaskHandler implements IFilesMultiTaskHandler {

    private static final int THREAD_COUNT_DEFAULT = 8 * 2;
    private static final int THREAD_COUNT_MAX = 30;

    /**
     * task thread count
     */
    private int threadCount;
    /**
     * batchSize per task
     */
    private int batchSize;
    /**
     * task factory
     */
    private IFilesTaskFactory taskFactory;
    /**
     * thread pool
     */
    private ExecutorService executorService;

    public static DefaultFilesMultiTaskHandler newInstance() {
        return new DefaultFilesMultiTaskHandler();
    }

    private DefaultFilesMultiTaskHandler() {
    }

    @Override
    public IFilesMultiTaskHandler setBatchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    @Override
    public IFilesMultiTaskHandler setThreadCount(int threadCount) {
        if (threadCount <= 0) {
            this.threadCount = THREAD_COUNT_DEFAULT;
        } else {
            this.threadCount = Math.min(threadCount, THREAD_COUNT_MAX);
        }
        this.executorService = Executors.newFixedThreadPool(this.threadCount);
        return this;
    }

    @Override
    public IFilesMultiTaskHandler setTaskFactory(IFilesTaskFactory factory) {
        this.taskFactory = factory;
        return this;
    }
}
