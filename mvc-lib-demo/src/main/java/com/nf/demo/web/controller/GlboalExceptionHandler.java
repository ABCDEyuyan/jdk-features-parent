package com.nf.demo.web.controller;

import com.nf.demo.vo.ResponseVO;
import com.nf.mvc.ViewResult;
import com.nf.mvc.exception.ExceptionHandler;

import static com.nf.mvc.handler.HandlerHelper.json;

public class GlboalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ViewResult handleRuntime(RuntimeException re){
        return json(new ResponseVO(500, "cuowu", null));
    }
}
