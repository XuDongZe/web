package com.xdz.web.util;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

/**
 * Description: http utils<br/>
 * Author: dongze.xu<br/>
 * Date: 2022/1/29 17:03<br/>
 * Version: 1.0<br/>
 */
public class HttpUtil {

    public static class Request {
        private String url;
        private String method;
        private Map<String, String> headerMap = Maps.newHashMap();
        private Map<String, String> paramMap = Maps.newHashMap();

        public static Request builder(String url, String method) {
            Request request = new Request();
            request.url = url;
            request.method = method;
            return request;
        }

        public Request addHeader(String key, String value) {
            this.headerMap.put(key, value);
            return this;
        }

        public Request addHeader(String...headers) {
            for (int i = 0; i < headers.length; i += 2) {
                this.headerMap.put(headers[i], headers[i + 1]);
            }
            return this;
        }

        public Request addParam(String key, String value) {
            this.paramMap.put(key, value);
            return this;
        }

        public Request addParam(String...headers) {
            for (int i = 0; i < headers.length; i += 2) {
                this.paramMap.put(headers[i], headers[i + 1]);
            }
            return this;
        }
    }

    public static String doRequest(Request request) throws IOException {
        if (Objects.equals(request.method, "GET")) {
            return doGet(request);
        } else if (Objects.equals(request.method, "POST")) {
            return doPost(request);
        }
        throw new RuntimeException("not support http method: " + request.method);
    }

    private static String doGet(Request request) throws IOException {
        URL url = new URL(request.url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(request.method);
        connection.setReadTimeout(5000);

        connection.connect();

        int code = connection.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            InputStream is = connection.getInputStream();

        }

        return "helklko";
    }

    private static String doPost(Request request) {
        throw new RuntimeException("not support http method: " + request.method);

    }
}
