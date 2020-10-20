package com.colm.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 当博客没有找到的时候报这个异常
 * alt + ins 快速生成
 *
 * Created by Colm on 2020/10/18
 */
// 只有加上这个才能找到404页面(同时在ControllerExceptionHandler中做出响应处理)，不然走到error/error
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends java.lang.RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
