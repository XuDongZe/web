package com.xdz.web.interceptor;

import com.xdz.web.model.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 全局返回处理<br/>
 * Author: dongze.xu<br/>
 * Date: 2021/8/12 11:26<br/>
 * Version: 1.0<br/>
 */
@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if (o instanceof BaseResponse) {
            httpServletRequest.getSession().setAttribute("response", o);
            return o;
        } else {
            BaseResponse res = BaseResponse.success(o);
            httpServletRequest.getSession().setAttribute("response", res);
            return res;
        }
    }
}
