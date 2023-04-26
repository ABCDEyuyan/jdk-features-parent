package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {
    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return 返回null表示本异常解析器不能解析此异常
     */
    ViewResult resolveException(
            HttpServletRequest request, HttpServletResponse response,  Object handler, Exception ex);
}
