package com.xdz.util.filestask;

/**
 * Description: 断点恢复<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/10/12 19:47<br/>
 * Version: 1.0<br/>
 */

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 断点恢复
 */
public class BreakpointRecoveryHelper {
    String dirPath;
    String finishFilePath = "bp_recover_finished.txt";
    String finishFileBakPath = "bp_recover_finished.bak.txt";
    String currentFilePath = "bp_recover_current.txt";

    private BufferedWriter finishWriter;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    // 这个变量工作在两个线程中：LogAnalyzer（主动存储bin log）和BreakpointRecovery（被动定时存储bin log）中。
    public AtomicReference<RecoveryStatus> recoveryStatus;

    public void startHandleFile(String filePath) {
        initStatusForNewFile(filePath);
    }

    public void endHandleFile(String filePath) {
        saveFinishedFilePaths(filePath);
    }

    public void cacheLines(int lines) {
        RecoveryStatus status = recoveryStatus.get();
        RecoveryStatus newStatus = new RecoveryStatus(status);
        newStatus.fileLines += lines;
        newStatus.linesFromLastSave += lines;
        recoveryStatus.compareAndSet(status, newStatus);
    }

    /**
     * 恢复到指定的file,line状态
     */
    public BufferedReader recovery(String filePath, int lines) throws IOException {
        int i = 0;
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        while (i < lines && (reader.readLine()) != null) {
            i++;
        }
        doRecovery(filePath, lines);
        return reader;
    }

    public void close() {
        {
            try {
                if (finishWriter != null) {
                    finishWriter.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BreakpointRecoveryHelper(String dirPath) {
        try {
            assert dirPath != null;
            if (new File(dirPath).isFile()) {
                dirPath = Paths.get(dirPath).getParent().toAbsolutePath().toString();
            }
            this.dirPath = dirPath;
            finishFilePath = Paths.get(dirPath, finishFilePath).toAbsolutePath().toString();
            currentFilePath = Paths.get(dirPath, currentFilePath).toAbsolutePath().toString();

            // pre pare
            createFileIfNotExist(finishFilePath);
            createFileIfNotExist(currentFilePath);

            // io

            // 这里会有重复的问题。需要处理一下
            finishWriter = new BufferedWriter(new FileWriter(finishFilePath, true));

            // recovery status
            AtomicReference<RecoveryStatus> tmp = new AtomicReference<>();
            RecoveryStatus status = new RecoveryStatus();
            // load from disk
            CurrentFileStatus currentFileStatus = readCurrentFileStatus();
            status.setFilePath(currentFileStatus.path);
            status.setFileLines(currentFileStatus.lines);
            Set<String> finishedFileSet = readFinishedFilePaths();
            status.setFinishedFilePathSet(finishedFileSet);
            tmp.set(status);
            recoveryStatus = tmp;

            // back-save
            final Config config = loadConfig();
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    boolean save = false;
                    RecoveryStatus oldStatus = recoveryStatus.get();
                    RecoveryStatus newStatus = new RecoveryStatus(oldStatus);
                    if (newStatus.linesFromLastSave >= config.linesToSave) {
                        save = true;
                    }

                    if (save) {
                        try {
                            // todo current reader 每次不用重新打开文件。使用seek重置光标即可
                            saveCurrentFileStatus(newStatus.filePath, newStatus.fileLines);
                            newStatus.setMsFromLastSave(0);
                            newStatus.setLinesFromLastSave(0);
                            recoveryStatus.compareAndSet(oldStatus, newStatus);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, config.periodMsToSave, config.periodMsToSave, TimeUnit.MILLISECONDS); // 等待1次任务执行来填充数据
        } catch (IOException e) {
            throw new RuntimeException("bp recovery init exception: " + e.getMessage(), e);
        }
    }

    private void createFileIfNotExist(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    // todo 这个出问题后怎么再无人干预的情况下 重试恢复：初始化的时候如果有备份文件，则将备份文件复制到源文件，然后删除备份文件。
    public void reorganizeFinishedFilePaths() {
        try {
            // 文件复制
            String tmpPath = Paths.get(dirPath, finishFileBakPath).toAbsolutePath().toString();
            moveFinishedFileTo(tmpPath);
            // 文件rename 此时旧文件已经有了。
            if (new File(tmpPath).exists()) {
                // 确保已经备份过了 再处理finishFilePath这个文件
                moveFinishedFileTo(finishFilePath);
                Files.deleteIfExists(Paths.get(tmpPath));
            }
        } catch (IOException e) {

        }
    }

    private void moveFinishedFileTo(String tmpPath) throws IOException {
        Set<String> set = recoveryStatus.get().finishedFilePathSet;
        BufferedWriter tmpWriter = new BufferedWriter(new FileWriter(tmpPath));
        for (String s : set) {
            tmpWriter.write(s);
            tmpWriter.newLine();
            tmpWriter.flush();
        }
        tmpWriter.close();
    }

    private Set<String> readFinishedFilePaths() throws IOException {
        BufferedReader finishReader = new BufferedReader(new FileReader(finishFilePath));
        Set<String> set = Sets.newHashSet();
        String line;
        while ((line = finishReader.readLine()) != null) {
            set.add(line);
        }
        finishReader.close();
        return set;
    }

    private void saveFinishedFilePaths(String path) {
        try {
            finishWriter.write(path);
            finishWriter.newLine();
            finishWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CurrentFileStatus readCurrentFileStatus() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(currentFilePath));
        CurrentFileStatus status = new CurrentFileStatus();
        String line;
        int n = 0;
        while ((line = reader.readLine()) != null) {
            n++;
            if (n == 1) {
                status.path = line;
            }
            if (n == 2) {
                status.lines = Integer.parseInt(line);
            }
        }
        reader.close();
        return status;
    }

    /**
     * 每隔一定时间或者写入n行数据, 就保存一次
     */
    private void saveCurrentFileStatus(String path, int line) throws IOException {
        BufferedWriter currentWriter = new BufferedWriter(new FileWriter(currentFilePath));

        currentWriter.write(path);
        currentWriter.newLine();
        currentWriter.write(line + "");
        currentWriter.newLine();

        currentWriter.flush();
        currentWriter.close();
    }

    private void initStatusForNewFile(String filePath) {
        RecoveryStatus status = recoveryStatus.get();
        RecoveryStatus newStatus = new RecoveryStatus(status);
        newStatus.filePath = filePath;
        newStatus.fileLines = 0;
        recoveryStatus.compareAndSet(status, newStatus);
    }

    private void doRecovery(String filePath, int lines) {
        RecoveryStatus status = recoveryStatus.get();
        RecoveryStatus newStatus = new RecoveryStatus(status);
        newStatus.filePath = filePath;
        newStatus.fileLines = lines;
        recoveryStatus.compareAndSet(status, newStatus);
    }

    /**
     * first: file path, "FINISH"表示全部文件已经完成 "NULL"表示没有开始.
     * second: finished lines
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentFileStatus {
        String path;
        int lines;
    }

    private Config loadConfig() {
        Config config = new Config();
        return config;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        long periodMsToSave = 1000;
        int linesToSave = 1000;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    /**
     * 有 > lines写入 则save
     * */
    public static class RecoveryStatus {
        long msFromLastSave;
        int linesFromLastSave;

        Set<String> finishedFilePathSet;
        String filePath;
        int fileLines;

        public RecoveryStatus(RecoveryStatus status) {
            this.msFromLastSave = status.msFromLastSave;
            this.linesFromLastSave = status.linesFromLastSave;

            this.finishedFilePathSet = status.finishedFilePathSet;
            this.filePath = status.filePath;
            this.fileLines = status.fileLines;
        }
    }

    /**
     * 是否是内部file
     */
    public boolean isInnerFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String absolutePath = file.getAbsolutePath();
        return Objects.equals(absolutePath, finishFilePath) || Objects.equals(absolutePath, currentFilePath);
    }

    public static void main(String[] args) throws IOException {
        // test interval save
//        String filePath = "E:\\project\\java\\cube_national_real_name\\a.csv";
//        int lines = 179000;
//        for (int i = 0; i < 3; i++) {
//            INSTANCE.saveCurrentFileStatus(filePath, lines);
//            System.out.println("success");
//        }
//        for (int i = 0; i < 3; i ++) {
//            INSTANCE.saveFinishedFilePaths("E:\\project\\java\\cube_national_real_name\\a.csv");
//        }

        // test read
    }
}
