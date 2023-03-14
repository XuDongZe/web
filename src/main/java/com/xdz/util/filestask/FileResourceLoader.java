package com.xdz.util.filestask;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 加载文件系统资源：path可以是目录 | 单个文件<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/2/3 15:06<br/>
 * Version: 1.0<br/>
 */
public class FileResourceLoader implements IResourceLoader {

    @Override
    public List<File> loadResources(String path) {
        List<File> result = listFiles(new File(path));
        return result == null ? new ArrayList<File>() : result;
    }

    @Nullable
    private List<File> listFiles(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        if (file.isFile()) {
            return Lists.newArrayList(file);
        }
        // now file is direcotry
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }

        List<File> res = Lists.newArrayList();
        for (File t : files) {
            List<File> files1 = listFiles(t);
            if (files1 != null && !files1.isEmpty()) {
                res.addAll(files1);
            }
        }
        return res;
    }
}
