package com.nf.mvc.adapters;

import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.handler.HandlerInfo;
import com.nf.mvc.HttpRequestHandler;
import com.nf.mvc.ViewResult;
import com.nf.mvc.view.VoidViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpRequestHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
       return handler instanceof HandlerInfo
                && HttpRequestHandler.class
                        .isAssignableFrom(((HandlerInfo)handler).getHandlerClass());
    }

    @Override
    public ViewResult handle(HttpServletRequest req,
                             HttpServletResponse resp,
                             Object handler) throws Exception {
        HandlerInfo handlerInfo = (HandlerInfo) handler;
        Class<?> handlerInfoClz = handlerInfo.getHandlerClass();
        Object instance = handlerInfoClz.newInstance();


        HttpRequestHandler requestHandler = (HttpRequestHandler) instance;
        requestHandler.processRequest(req, resp);
        return new VoidViewResult();

    }
}
