package com.xdz.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 通用返回<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/8/11 20:10<br/>
 * Version: 1.0<br/>
 */
@Data
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {
    private static final int RESPONSE_CODE_ERROR = -1;
    private static final int RESPONSE_CODE_SUCCESS = 0;
    private static final String RESPONSE_MSG_SUCCESS = "success";

    int code;
    String msg;
    T data;
    transient Throwable exception;

    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> BaseResponse success(T data) {
        return new BaseResponse(RESPONSE_CODE_SUCCESS, "success", data);
    }

    public static BaseResponse error(int code, String msg, Throwable e) {
        return new BaseResponse(code, msg, null);
    }

    public static BaseResponse error(Exception e) {
        return error(RESPONSE_CODE_ERROR, e.getMessage(), e);
    }
}
