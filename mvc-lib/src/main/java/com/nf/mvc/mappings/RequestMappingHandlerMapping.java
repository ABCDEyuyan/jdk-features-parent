package com.nf.mvc.mappings;

import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;
import com.nf.mvc.handler.HandlerInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private Map<String, HandlerInfo> handlers = new HashMap<>();
    public RequestMappingHandlerMapping() {
        resolveHandlers();
    }

    protected void resolveHandlers(){
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();

        for (Class<?> clz : classList) {
            String urlInClass = getUrl(clz);
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    HandlerInfo handlerInfo = new HandlerInfo(method);
                    String urlInMethod = getUrl(method);
                    String url = urlInClass + urlInMethod;
                    //TODO:不能有重复的url，要抛出异常
                    handlers.put(url, handlerInfo);
                }
            }
        }
    }
    @Override
    public Object getHandler(HttpServletRequest request) throws Exception {
        String requestUrl = getRequestUrl(request);
        return handlers.get(requestUrl);
    }

    private String getUrl(AnnotatedElement element) {
        return element.isAnnotationPresent(RequestMapping.class) ?
                element.getDeclaredAnnotation(RequestMapping.class).value().toLowerCase() : "";
    }

}
