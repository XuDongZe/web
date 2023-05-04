package com.xdz.web.controller;

import com.xdz.web.service.ChatGptService;
import com.xdz.web.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/5/4 12:31<br/>
 * Version: 1.0<br/>
 */
@Controller
@RequestMapping(path = "/sse")
public class SseRestController {
    private static final Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    @Autowired
    private SseService sseService;
    @Autowired
    private ChatGptService chatService;

    @ResponseBody
    @RequestMapping(path = "/subscribe")
    public SseEmitter subscribe(String id) {
        return sseService.subscribe(id);
    }

    @ResponseBody
    @RequestMapping(path = "/chat")
    public void chat(String id, String input) {
        chatService.chat(id, input);
    }

    @ResponseBody
    @RequestMapping(path = "/push")
    public String push(String id, String content) throws IOException {
        sseService.push(id, content);
        return "over";
    }

    @ResponseBody
    @RequestMapping(path = "/over")
    public String over(String id) {
        sseService.over(id);
        return "over";
    }
}
