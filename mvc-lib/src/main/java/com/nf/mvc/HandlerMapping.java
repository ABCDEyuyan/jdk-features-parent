package com.nf.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 此接口用来依据请求找到对应的处理者（Handler)
 */
public interface HandlerMapping {
    /**
     * @param request
     * @return 是一个请求处理者（或者执行链）
     * @throws Exception
     */
    Object getHandler(HttpServletRequest request) throws Exception;

    default String getRequestUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return request.getRequestURI().substring(contextPath.length());
    }
}
