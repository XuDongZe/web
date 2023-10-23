package com.xdz.util.filestask;

import java.util.concurrent.ExecutorService;

public interface IFilesMultiTaskHandler {
    int getBatchSize();
    /**
     * 单次任务的批处理量
     */
    IFilesMultiTaskHandler setBatchSize(int batchSize);

    /**
     * 线程数
     */
    int getThreadCount();
    IFilesMultiTaskHandler setThreadCount(int threadCount);

    /**
     * 任务工厂
     */
    IFilesTaskFactory getTaskFactory();
    IFilesMultiTaskHandler setTaskFactory(IFilesTaskFactory factory);

    /**
     * 线程池
     */
    ExecutorService getExecutorService();
}
