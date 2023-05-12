package com.nf.mvc.argument;

import com.nf.mvc.HandlerContext;
import com.nf.mvc.MethodArgumentResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 此解析器主要是用来解析常见的Servlet相关的类型，比如HttpServletRequest，HttpSession等
 * @see HandlerContext
 */
public class ServletApiMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return HttpServletRequest.class.isAssignableFrom(paramType)||
                HttpServletResponse.class.isAssignableFrom(paramType)||
                HttpSession.class.isAssignableFrom(paramType)||
                ServletContext.class.isAssignableFrom(paramType);
    }

    /**
     * 参数解析，是从请求中获取数据的，所以方法设计没有response对象是合理的
     * 但这样给我们带来一个解析的问题，无法获取到response对象
     * request与response对象必须来自于DispatcherServlet的service
     * @param parameter
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        HandlerContext context = HandlerContext.getContext();
        if (HttpServletRequest.class.isAssignableFrom(paramType)) {
            return request;
        }
        if (HttpServletResponse.class.isAssignableFrom(paramType)) {
            return context.getResponse();
        }
        if (HttpSession.class.isAssignableFrom(paramType)) {
           // return request.getSession();//这样也可以
            return context.getSession();
        }
        if (ServletContext.class.isAssignableFrom(paramType)) {
            return request.getServletContext();
        }
        // 一般不会走到这里，因为supports方法返回true才会调用resolveArgument方法
        return null;
    }
}
