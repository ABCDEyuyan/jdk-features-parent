package com.nf.mvc.util;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestUtils {
    public static String getRequestUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return request.getRequestURI().substring(contextPath.length());
    }
}
