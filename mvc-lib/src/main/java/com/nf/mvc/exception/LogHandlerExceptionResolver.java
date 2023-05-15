package com.nf.mvc.exception;

import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.ViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.nf.mvc.handler.HandlerHelper.empty;

public class LogHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (!(ex instanceof RuntimeException)) {
            return null;
        }
        //我们这个解析器只解析RuntimeException
        System.out.println("\n==============");
        System.out.println("异常消息是:" + ex.getMessage());
        System.out.println("==============");

        return null;
    }
}
