package com.xdz.util;

/**
 * Description: common check <br/>
 * Author: dongze.xu<br/>
 * Date: 2022/6/12 14:13<br/>
 * Version: 1.0<br/>
 */
public class CheckUtil {

    public static void checkNotNull(Object obj, String msg) {
        if (obj == null) {
            throw new RuntimeException("obj is null: " + msg);
        }
    }
}
