package com.nf.mvc.util;

import javax.servlet.http.HttpServletRequest;

/**
 * <h3>参考资料</h3>
 * <a href="https://stackoverflow.com/questions/4931323/whats-the-difference-between-getrequesturi-and-getpathinfo-methods-in-httpservl">getRequestUri,getPathInfo等方法区别</a>
 * <a href="https://codebox.net/pages/java-servlet-url-parts">request对象路径相关方法含义</a>
 */
public abstract class RequestUtils {
    /**
     * 用来获取当前请求地址，排除掉上下文（contextPath）的部分,
     * 此方法的实现参考了spring中UrlPathHelper的代码(通过找HandlerMapping源码中路径实现可以找到这个类)
     * TODO:有bug,如果DispatcherServlet的模式为*.do这样的设置,要去掉.do这三个字符
     * @param request
     * @return
     */
    public static String getRequestUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return request.getRequestURI().substring(contextPath.length());
    }
}
