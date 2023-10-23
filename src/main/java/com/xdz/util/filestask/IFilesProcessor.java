package com.xdz.util.filestask;

import javax.servlet.jsp.JspWriter;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/2/3 10:55<br/>
 * Version: 1.0<br/>
 */

// done 组件关系 参数传递 => 重构。finalProcessor和multiTaskProcessor上面再包一层来管理。final不是multi的子组件 是兄弟组件。
// done 支持单文件/单目录/多目录 模式，目前只支持单目录
// todo 限速器 批处理n行之后 sleep m秒，向上接口可以是maxRate=k 行/秒 所有线程的总速率。
// todo TaskFactory Helper 减少重复代码。
// todo 进度值打印 a/b (all lines / current lines) 平均速率
// todo 参数校验
    // todo counter计数时 异常怎么办？现在再最后才记录一下。需要定时同步减少异常现象发生。

public interface IFilesProcessor {

    /**
     * 要处理的目录文件
     */
    String getResourcePath();

    /**
     * 输出流
     * todo 可以换成别的
     */
    JspWriter getWriter();
    IFilesProcessor setWriter(JspWriter writer);

    /**
     * 资源加载
     */
    IResourceLoader getResourceLoader();
    IFilesProcessor setResourceLoad(IResourceLoader resourceLoader);

    /**
     * 多任务处理器
     */
    IFilesMultiTaskHandler getMultiTaskHandler();
    IFilesProcessor setMultiTaskHandler(IFilesMultiTaskHandler handler);

    /**
     * 主线程流程处理器
     */
    IFilesMainThreadHandler getMainThreadHandler();
    IFilesProcessor setMainThreadHandler(IFilesMainThreadHandler handler);

    /**
     * 断点恢复
     */
    BreakpointRecoveryHelper getRecoveryHelper();

    /**
     * 日志
     */
    FilesTaskLogAnalyzer getLogAnalyzer();

    /**
     * 处理流入口
     */
    void process() throws Exception;

    /**
     * 所有任务处理完毕后，统计数据
     * todo 这个需要做一些多线程的同步操作。
     */
    TaskResult getFinalTaskResult();
}
