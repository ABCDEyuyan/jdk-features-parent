package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {
    ViewResult resolveException(
            HttpServletRequest request, HttpServletResponse response,  Object handler, Exception ex);
}
