package com.xdz.web.demo.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * Description: guava loading cache test<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/12/10 18:45<br/>
 * Version: 1.0<br/>
 */
public class GuavaCache {
    private final static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            // 超过这个开始驱逐策略
            .maximumSize(3)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .refreshAfterWrite(5, TimeUnit.SECONDS)
            .weakKeys()
            .weakValues()
            .softValues()
            .concurrencyLevel(2)
            .initialCapacity(1)
            .recordStats()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return "value";
                }
            });

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            cache.put(i + "", i + "");
        }
    }
}
