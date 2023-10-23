package com.xdz.util.filestask;

import com.google.common.collect.Lists;
import lombok.Getter;

import javax.servlet.jsp.JspWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * Description: 默认的task processor框架实现<br/>
 * 一个目录下的 按行存储的文件 批处理. 提供日志打印, 断点恢复等简单功能. <br/>
 * Waring!!! 断点恢复不是非常精确, 有一些损耗, 即恢复后有少量数据会重新执行. 所以程序需要保证数据任务重复执行没有副作用!!!.
 * <p>
 * Author: dongze.xu<br/>
 * Date: 2021/10/13 14:51<br/>
 * Version: 1.0<br/>
 * <p>
 * todo JspWriter换成更加通用的：JspWriter和System.out的公共父类。并且支持println。
 * todo 异常处理 捕获后 打印到error.log文件中。
 */
@Getter
public class DefaultFilesProcessor implements IFilesProcessor {
    /**
     * jsp out
     */
    private JspWriter writer;
    /**
     * dirPath
     */
    private String resourcePath;
    /**
     * resource loader
     */
    private IResourceLoader resourceLoader;
    /**
     * 多任务处理器
     */
    private IFilesMultiTaskHandler multiTaskHandler;
    /**
     * 主线程流程处理
     */
    private IFilesMainThreadHandler mainThreadHandler;
    /**
     * log
     */
    private FilesTaskLogAnalyzer logAnalyzer;
    /**
     * 断点恢复
     */
    private BreakpointRecoveryHelper recoveryHelper;
    // 是否启用recovery功能
    private boolean recoveryEnable;
    /**
     * 最终结果
     */
    private TaskResult finalResult;

    public DefaultFilesProcessor(JspWriter writer, String path) {
        assert writer != null;
        assert path != null;

        this.writer = writer;
        this.resourcePath = path;
        this.resourceLoader = new FileResourceLoader();
        this.recoveryHelper = new BreakpointRecoveryHelper(path);
        this.multiTaskHandler = DefaultFilesMultiTaskHandler.newInstance(); // new instance for param-holder
        this.finalResult = new TaskResult();
        // 依赖recoveryHelper finalResult 初始化完毕
        this.logAnalyzer = new FilesTaskLogAnalyzer(writer, finalResult, recoveryHelper);
    }

    public DefaultFilesProcessor setRecoveryEnable(boolean recoveryEnable) {
        this.recoveryEnable = recoveryEnable;
        return this;
    }

    @Override
    public IFilesProcessor setWriter(JspWriter writer) {
        this.writer = writer;
        return this;
    }

    @Override
    public IFilesProcessor setResourceLoad(IResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        return this;
    }

    @Override
    public IFilesProcessor setMultiTaskHandler(IFilesMultiTaskHandler handler) {
        this.multiTaskHandler = handler;
        return this;
    }

    @Override
    public IFilesProcessor setMainThreadHandler(IFilesMainThreadHandler handler) {
        this.mainThreadHandler = handler;
        return this;
    }

//    // 如果finish
//    private Map<String, BufferedReader> fileReaderMap = Maps.newHashMap();

    @Override
    public void process() throws Exception {
        try {
            if (multiTaskHandler == null || multiTaskHandler.getTaskFactory() == null || multiTaskHandler.getExecutorService() == null) {
                throw new RuntimeException("no taskHolder. please set one before process");
            }

            BreakpointRecoveryHelper.RecoveryStatus status = recoveryHelper.recoveryStatus.get();
            String recoveryFilePath = status.filePath;
            int recoveryFileLines = status.fileLines;
            Set<String> finishedFilePaths = status.getFinishedFilePathSet();

            // 控制只跳过currentFile一次。
            boolean skip = false;
            List<File> files = resourceLoader.loadResources(resourcePath);

            if (mainThreadHandler != null) {
                mainThreadHandler.beforeAllFilesStart(this);
            }

            for (File file : files) {
                String filePath = file.getPath();
                // 理论上没有目录了
                if (!file.isFile()) {
                    writer.println("\n******skipDirectory: " + filePath + "*********");
                    continue;
                }
                if (isFileNeedExcluded(file)) {
                    writer.println("\n******excludeFile. file: " + filePath + "*********");
                    continue;
                }
                // recovery
                if (recoveryEnable && finishedFilePaths.contains(filePath)) {
                    writer.println("\n******skipFile. file: " + filePath + "*********");
                    continue;
                }

                BufferedReader reader = null;

                // start new file
                recoveryHelper.startHandleFile(filePath);
                // recovery
                if (recoveryEnable && !skip && Objects.equals(recoveryFilePath, filePath)) {
                    reader = recoveryHelper.recovery(recoveryFilePath, recoveryFileLines);
                    writer.println("\n******skipLines. file: " + recoveryFilePath + " lines: " + recoveryFileLines + "*********");
                    skip = true;
                } else {
                    reader = new BufferedReader(new FileReader(filePath));
                }

                // do handle
                handleFile(filePath, reader);

                writer.flush();
                // save & close
                recoveryHelper.endHandleFile(filePath);
            }

            // 可选的
            if (mainThreadHandler != null) {
                mainThreadHandler.afterAllFilesDone(this);
            }
        } finally {
            // executor
            multiTaskHandler.getExecutorService().shutdown();
            // finishedFile去重
            recoveryHelper.reorganizeFinishedFilePaths();
            recoveryHelper.close();
        }
    }


    @Override
    public TaskResult getFinalTaskResult() {
        return finalResult;
    }

    private void handleFile(String filePath, BufferedReader reader) throws Exception {
        String line;
        List<String> lines = Lists.newArrayList();
        BlockingQueue<FutureTask<TaskResult>> tasks = new ArrayBlockingQueue<>(1000);
        ExecutorService executorService = multiTaskHandler.getExecutorService();
        try {
            // 可选的
            if (mainThreadHandler != null) {
                mainThreadHandler.beforeAllThreadStart(this);
            }

            writer.println("\nstart File: " + filePath);
            writer.flush();
            reader = reader == null ? new BufferedReader(new FileReader(filePath)) : reader;

            // 开启日志
            logAnalyzer.readingFile = true;
            logAnalyzer.logResult(tasks);
            // 提交任务
            int lineSize = 0;
            int taskSize = 0;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                if (lines.size() >= multiTaskHandler.getBatchSize()) {
                    FutureTask<TaskResult> task = new FutureTask<>(multiTaskHandler.getTaskFactory().createTask(lines));
                    tasks.put(task);
                    executorService.submit(task);
                    lineSize += lines.size();
                    taskSize++;
                    lines = Lists.newArrayList();
                }
            }
            if (lines.size() > 0) {
                FutureTask<TaskResult> task = new FutureTask<>(multiTaskHandler.getTaskFactory().createTask(lines));
                tasks.put(task);
                executorService.submit(task);
                lineSize += lines.size();
                taskSize++;
            }
            // 等待所有task处理完毕
            logAnalyzer.readingFile = false;
            logAnalyzer.resultLogger.join();
            // 可选的final handler
            if (mainThreadHandler != null) {
                mainThreadHandler.afterAllThreadDone(this);
            }

            writer.println("end File: " + filePath);
            writer.println("read lines: " + lineSize);
            writer.println("sum: " + finalResult.sum);
            writer.println("finish: " + finalResult.finish);
            writer.println("tasks(finish/sum): " + (taskSize - tasks.size()) + "/" + taskSize);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 是否需要跳过文件
     */
    private boolean isFileNeedExcluded(File file) {
        boolean exclude = false;
        exclude = exclude || recoveryHelper.isInnerFile(file);
        return exclude;
    }

    /**
     * example @see  animal-web/processor.jsp
     */
    public static void main(String[] args) throws Exception {
        String dirPath = "E:\\data\\real_name\\dev";
        // 175745540数据量 按照25 task * 100 batch参数。任务为一次或者两次cmem batch rpc读取（IO任务）+ 简单计数（cpu任务，可忽略）
        // 耗时1.5h左右
        JspWriter out = null;
        IFilesProcessor processor = new DefaultFilesProcessor(out, dirPath)
                // config
                // 禁用断点恢复组件的恢复功能，断点恢复的日志功能是一直开启的
                .setRecoveryEnable(false)
                // 主线程 切面
                .setMainThreadHandler(null)
                // 工作线程
                .setMultiTaskHandler(DefaultFilesMultiTaskHandler.newInstance()
                        .setThreadCount(25)
                        .setBatchSize(100)
                        .setTaskFactory(null));

        processor.process();
    }
}