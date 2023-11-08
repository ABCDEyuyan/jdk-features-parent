package com.nf.mvc.mapping;

import com.nf.mvc.HandlerExecutionChain;
import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;
import com.nf.mvc.handler.HandlerClass;
import com.nf.mvc.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 依据类的名字来处理映射，比如你的类名是以Controller结尾的，那么，就表明此类是一个Handler，
 * 假定类名是FirstController，那么就设定此类会对请求地址是{@code :/first}的请求进行处理
 * <p>此类并没有真正用起来，主要目的是设计一个简单的HandlerMapping实现，
 * 以便学生能理解什么是HandlerMapping，此类的灵感来自于asp.net mvc框架</p>
 */
public class NameConventionHandlerMapping implements HandlerMapping {
    /**
     * 类名的后缀
     */
    private static final String SUFFIX = "Controller";
    /**
     * 此map中放置的是当前HandlerMapping所能处理的所有请求url及其对应的Handler
     */
    private final Map<String, HandlerClass> handlers = new HashMap<>();

    public NameConventionHandlerMapping() {
        resolveHandlers();
    }

    private void resolveHandlers() {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScannedClasses();
        for (Class<?> clz : classList) {
            //com.FirstController(类的全称）--->FirstController（简单名）
            String simpleName = clz.getSimpleName();
            if (simpleName.endsWith(SUFFIX)) {
                String url = generateHandleUrl(simpleName);
                HandlerClass handlerClass = new HandlerClass(clz);
                handlers.put(url.toLowerCase(), handlerClass);
            }
        }
    }

    private String generateHandleUrl(String simpleName) {
        return "/" + simpleName.substring(0, simpleName.length() - SUFFIX.length());
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) {
        String requestUrl = RequestUtils.getRequestUrl(request);
        Object handler = handlers.get(requestUrl);
        return handler == null ? null : new HandlerExecutionChain(handler, getInterceptors(request));
    }
}
