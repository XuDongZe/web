package com.xdz.util;

import lombok.extern.apachecommons.CommonsLog;

import java.io.Closeable;
import java.io.IOException;

@CommonsLog
public class ResourceUtil {

    /**
     * 关闭一个资源
     */
    public static void close(Closeable... resources) {
        if (resources != null && resources.length > 0) {
            for (Closeable obj : resources) {
                if (obj != null) {
                    try {
                        obj.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("close resource error: " + e.getMessage(), e);
                    }
                }
            }
        }
    }


}
