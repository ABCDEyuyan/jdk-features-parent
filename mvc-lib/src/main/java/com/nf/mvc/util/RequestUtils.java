package com.nf.mvc.util;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestUtils {
    /**
     * 用来获取当前请求地址，排除掉上下文（contextPath）的部分
     * @param request
     * @return
     */
    public static String getRequestUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return request.getRequestURI().substring(contextPath.length());
    }
}
