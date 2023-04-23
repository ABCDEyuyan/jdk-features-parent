package com.nf.mvc.mapping;

import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 依据类的名字来处理映射，
 * 比如你的类是以Controller结尾的，名字是FirstController
 * 那么就表明你能处理的请求地址是:/first
 */
public class NameConventionHandlerMapping implements HandlerMapping {

    /** 类名的后缀 */
    private static final String SUFFIX = "Controller";
    /** 此map中放置的是当前HandlerMapping所能处理的所有请求 */
    private Map<String, HandlerMethod> handlers = new HashMap<>();


    public NameConventionHandlerMapping() {

        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();
        for (Class<?> clz : classList) {
            //com.FirstController(类的全程）--->FirstController（简单名）
            String simpleName= clz.getSimpleName();
            if(simpleName.endsWith(SUFFIX)){

                String url = generateHandleUrl(simpleName);
                HandlerMethod handlerMethod = new HandlerMethod(clz);
                handlers.put(url.toLowerCase(), handlerMethod);
            }
        }

    }

    private String generateHandleUrl(String simpleName) {
        return "/" + simpleName.substring(0, simpleName.length() - SUFFIX.length());
    }

    @Override
    public Object getHandler(HttpServletRequest request) throws ServletException {
        String uri = getRequestUrl(request);
        return handlers.get(uri);
    }


}