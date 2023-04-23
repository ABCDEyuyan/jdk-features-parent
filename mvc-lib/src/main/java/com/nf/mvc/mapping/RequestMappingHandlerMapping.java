package com.nf.mvc.mapping;

import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;
import com.nf.mvc.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private Map<String, HandlerMethod> handlers = new HashMap<>();
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
                    HandlerMethod handlerMethod = new HandlerMethod(method);
                    String urlInMethod = getUrl(method);
                    String url = urlInClass + urlInMethod;
                    addHandler(url, handlerMethod);
                }
            }
        }
    }
    protected void addHandler(String url, HandlerMethod handlerMethod){
        if(handlers.get(url)!=null){
            throw new IllegalStateException("不能有多个处理者对应同一个url");
        }
        this.handlers.put(url, handlerMethod);
    }
    @Override
    public Object getHandler(HttpServletRequest request) throws Exception {
        String requestUrl = getRequestUrl(request);
        return handlers.get(requestUrl);
    }

    /**
     *
     * @param element AnnotatedElement类型代表着所有可以放置注解的元素，比如类，方法参数，字段等
     * @return
     */
    private String getUrl(AnnotatedElement element) {
        return element.isAnnotationPresent(RequestMapping.class) ?
                element.getDeclaredAnnotation(RequestMapping.class).value().toLowerCase() : "";
    }

}
