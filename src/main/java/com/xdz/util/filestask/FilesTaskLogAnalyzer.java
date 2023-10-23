package com.xdz.util.filestask;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;

/**
 * Description: task日志记录, 断点恢复日志记录。工作在单线程下<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/10/13 14:51<br/>
 * Version: 1.0<br/>
 */
public class FilesTaskLogAnalyzer {
    // 正在读取任务中
    public volatile boolean readingFile = false;

    /**
     * 最终的汇总结果
     */
    private TaskResult finalResult;

    private BreakpointRecoveryHelper recoveryHelper;

    private JspWriter out;

    /**
     * out flushThreadHoldValue
     * 使用这两个做自定义的flush间隔。使用BuffedWriter无法按行控制。
     */
    private int flushThreadHoldValue = 100;
    private int flushAccuCount = 0;

    public FilesTaskLogAnalyzer(JspWriter out, TaskResult finalResult, BreakpointRecoveryHelper recoveryHelper) {
        this.out = out;
        this.finalResult = finalResult;
        this.recoveryHelper = recoveryHelper;
    }

    public Thread resultLogger;

    public void logResult(final BlockingQueue<FutureTask<TaskResult>> tasks) {
        resultLogger = new Thread(new Runnable() {
            @Override
            public void run() {
                while (readingFile || tasks.size() != 0) {
                    try {
                        FutureTask<TaskResult> task = tasks.poll();
                        if (task == null) {
                            continue;
                        }
                        TaskResult result = task.get();
                        finalResult.merge(result);

                        // 打印当前的result 的一些信息
                        logResult(result);

                        // 断点恢复记录
                        recoveryHelper.cacheLines(result.lines);
                    } catch (Exception e) {
                    }
                }

                try {
                    // 退出while循环后，清空本次flush周期内剩余的缓冲区内从
                    out.flush();
                    flushAccuCount = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        resultLogger.start();
    }

    private void logResult(TaskResult result) throws IOException {
//                        out.println(result);
        if (result.msgList != null && !result.msgList.isEmpty()) {
            for (String msg : result.msgList) {
                out.print(msg + "<br/>\n");
            }
        }
        out.print(result.sum + "/" + result.finish + "<br/>\n");

        // 累积flush 提高性能
        flushAccuCount ++;
        if (flushAccuCount >= flushThreadHoldValue) {
            out.flush();
            flushAccuCount = 0;
        }
    }
}
