package com.xdz.util;

import java.util.Map;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/3/14 14:57<br/>
 * Version: 1.0<br/>
 */
public class MapUtil {
    public static void incrNum(Map<String, Long> counter, String key, Long added) {
        if (counter != null) {
            counter.put(key, counter.getOrDefault(key, 0L) + added);
        }
    }
}
