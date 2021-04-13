package com.autopay.autopay.controller;

import com.autopay.autopay.constants.ReturnCode;
import com.autopay.autopay.domain.exception.AppException;
import com.autopay.autopay.domain.response.common.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 处理response中的code和message
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        final String returnTypeName = returnType.getParameterType().getTypeName();

        // 无返回值
        if ("void".endsWith(returnTypeName)) {
            return RestResponse.success();
        }
        // 非JSON返回
        if (!selectedContentType.includes(MediaType.APPLICATION_JSON)) {
            return body;
        }
        // 已设置响应
        if ("com.huluxia.developer.server.domain.response.common.RestResponse".equals(returnTypeName)) {
            return body;
        }
        if ("org.springframework.http.ResponseEntity".equals(returnTypeName)) {
            return body;
        }
        return RestResponse.success(body);
    }

    /**
     * 返回前是否调用beforeBodyWrite的方法判断, 可用自定义注解适配
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 系统异常
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public RestResponse<String> handleException(Exception e) {
        log.error("[系统内部未知异常]", e);
        return RestResponse.error(ReturnCode.INTERNAL_ERROR, "出错了,请联系客服!");
    }

    /**
     * AppException
     */
    @ExceptionHandler(AppException.class)
    @ResponseBody
    public RestResponse<String> customerException(AppException e) {
        return RestResponse.error(ReturnCode.APP_ERROR, e.getMessage());
    }

}