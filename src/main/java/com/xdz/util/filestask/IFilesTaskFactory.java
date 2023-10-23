package com.xdz.util.filestask;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Description: task holder抽象类<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/10/13 14:28<br/>
 * Version: 1.0<br/>
 */
public interface IFilesTaskFactory {
    Callable<TaskResult> createTask(List<String> lines);
}
