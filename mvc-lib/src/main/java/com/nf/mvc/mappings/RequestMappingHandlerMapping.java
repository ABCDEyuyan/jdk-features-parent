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
            String urlPrefix="";
            if(clz.isAnnotationPresent(RequestMapping.class)){
                RequestMapping clzDeclaredAnnotation = clz.getDeclaredAnnotation(RequestMapping.class);
                urlPrefix = clzDeclaredAnnotation.value();
            }
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    HandlerInfo handlerInfo = new HandlerInfo(method);
                    RequestMapping methodDeclaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
                    String methodUrl = methodDeclaredAnnotation.value();
                    String url = urlPrefix.toLowerCase()+ methodUrl.toLowerCase();

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

   /* private String getUrl(AnnotatedElement element) {
        if (element.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = element.getDeclaredAnnotation(RequestMapping.class);
            annotation.value();


        }
    }*/
}
