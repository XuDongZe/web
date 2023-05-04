package com.xdz.web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/5/4 14:34<br/>
 * Version: 1.0<br/>
 */
@Service
public class SseService {
    private static final Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String id) {
        // 超时时间设置为1小时
        SseEmitter sseEmitter = new SseEmitter(3600_000L);
        sseCache.put(id, sseEmitter);
        sseEmitter.onTimeout(() -> sseCache.remove(id));
        sseEmitter.onCompletion(() -> System.out.println("完成！！！"));
        return sseEmitter;
    }

    public void push(String id, String content) throws IOException {
        SseEmitter sseEmitter = sseCache.get(id);
        if (sseEmitter != null) {
            sseEmitter.send(content);
        }
    }

    public void over(String id) {
        SseEmitter sseEmitter = sseCache.get(id);
        if (sseEmitter != null) {
            sseEmitter.complete();
            sseCache.remove(id);
        }
    }
}
