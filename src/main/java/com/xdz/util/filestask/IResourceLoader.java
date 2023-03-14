package com.xdz.util.filestask;

import java.io.File;
import java.util.List;

/**
 * Description: 数据资源加载<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/2/3 15:04<br/>
 * Version: 1.0<br/>
 */
public interface IResourceLoader {
    /**
     * 加载资源 结果以文件列表的形式给出
     */
    List<File> loadResources(String path);
}
