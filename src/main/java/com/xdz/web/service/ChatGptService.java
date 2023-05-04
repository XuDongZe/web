package com.xdz.web.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description: TODO<br/>
 * Author: dongze.xu<br/>
 * Date: 2023/5/4 14:32<br/>
 * Version: 1.0<br/>
 */
@Slf4j
@Service
public class ChatGptService {
    // com.nwpu.computer.xdz@gmail.com 谷歌账号
    private static final String API_KEY = "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";

    private OpenAiStreamClient client;
    private OpenAiClient syncClient;

    @Autowired
    private SseService sseService;

    @PostConstruct
    public void init() {
        //国内访问需要做代理，国外服务器不需要
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
//                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        client = OpenAiStreamClient.builder()
                .apiKey(Arrays.asList(API_KEY))
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient)
                .build();

        syncClient = OpenAiClient.builder()
                .apiKey(Arrays.asList(API_KEY))
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient)
                .build();
    }

    public void chat(String id, String question) {
        Message user = Message.builder()
                .role(Message.Role.USER)
                .content(question)
                .build();
        List<Message> messages = Lists.newArrayList();
        messages.add(user);
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .temperature(0.1)
                .maxTokens(2048)
                .messages(messages)
                .stream(true)
                .build();

        WebEventSourceListener listener = new WebEventSourceListener(id, sseService);
        client.streamChatCompletion(chatCompletion, listener);
    }

    /**
     * 具有独立id属性的EventSource
     */
    public static class WebEventSourceListener extends ConsoleEventSourceListener {
        String sourceId;
        SseService sseService;

        public WebEventSourceListener(String sourceId, SseService sseService) {
            this.sourceId = sourceId;
            this.sseService = sseService;
        }

        @Override
        public void onEvent(EventSource eventSource, String id, String type, String data) {
            super.onEvent(eventSource, id, type, data);
            try {
                JSONObject obj = JSONObject.parseObject(data);
                String content = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("delta").getString("content");
                if (content != null && content.length() > 0) {
                    sseService.push(sourceId, content);
                }
            } catch (Exception e) {
                // do-nothing
            }
        }

        @Override
        public void onClosed(EventSource eventSource) {
            log.info("OpenAI关闭sse连接...");
            sseService.over(sourceId);
        }
    }
}
