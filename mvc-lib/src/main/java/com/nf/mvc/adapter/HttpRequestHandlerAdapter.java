package com.nf.mvc.adapter;

import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.handler.HandlerClass;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.HttpRequestHandler;
import com.nf.mvc.ViewResult;
import com.nf.mvc.util.ReflectionUtils;
import com.nf.mvc.view.VoidViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
       return handler instanceof HandlerClass
                && HttpRequestHandler.class
                        .isAssignableFrom(((HandlerClass)handler).getHandlerClass());
    }

    @Override
    public ViewResult handle(HttpServletRequest req,
                             HttpServletResponse resp,
                             Object handler) throws Exception {
        HandlerClass handlerClass = (HandlerClass) handler;
        Class<?> handlerInfoClz = handlerClass.getHandlerClass();
        Object instance = ReflectionUtils.newInstance(handlerInfoClz);

        HttpRequestHandler requestHandler = (HttpRequestHandler) instance;
        requestHandler.processRequest(req, resp);
        return new VoidViewResult();

    }
}
