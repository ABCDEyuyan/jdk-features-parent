package com.nf.demo.web.controller;


import com.nf.mvc.ViewResult;
import com.nf.mvc.exception.ExceptionHandler;
import com.nf.mvc.support.vo.ResponseVO;

import static com.nf.mvc.handler.HandlerHelper.json;

public class GlboalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ViewResult handleRuntime(RuntimeException re){
        return json(ResponseVO.fail());
    }
}
