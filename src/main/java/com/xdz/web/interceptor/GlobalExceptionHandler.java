package com.xdz.web.interceptor;

import com.xdz.web.model.BaseResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: 全局异常处理<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/8/11 20:44<br/>
 * Version: 1.0<br/>
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    BaseResponse handleException(Exception e) {
        BaseResponse response = BaseResponse.error(e);
        return response;
    }
}
