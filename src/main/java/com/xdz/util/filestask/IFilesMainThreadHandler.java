package com.xdz.util.filestask;

public interface IFilesMainThreadHandler {
    /**
     * 在所有的文件处理之前
     */
    void beforeAllFilesStart(IFilesProcessor processor);

    /**
     * 在所有的任务启动之前
     */
    void beforeAllThreadStart(IFilesProcessor processor);
    /**
     * 在一个文件的所有的Task完成之后，
     */
    void afterAllThreadDone(IFilesProcessor processor);

    /**
     * 再所有的文件都处理完毕之后，
     */
    void afterAllFilesDone(IFilesProcessor processor);
}
