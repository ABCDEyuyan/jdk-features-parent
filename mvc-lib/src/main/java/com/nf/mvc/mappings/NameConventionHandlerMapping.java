package com.nf.mvc.mappings;

import com.nf.mvc.HandlerInfo;
import com.nf.mvc.HandlerMapping;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 依据类的名字来处理映射，
 * 比如你的类是以Controller结尾的，名字是FirstController
 * 那么就表明你能处理的请求地址是:/first
 */
public class NameConventionHandlerMapping implements HandlerMapping {

    private static final String SUFFIX = "Controller";
    private Map<String, HandlerInfo> handlers = new HashMap<>();
    private ClassInfoList infoList ;

    public NameConventionHandlerMapping(ClassInfoList infoList) {
        this.infoList = infoList;

        for (ClassInfo classInfo : infoList) {
            //FirstController
            String simpleName= classInfo.getSimpleName();
            if(simpleName.endsWith(SUFFIX)){
                String url ="/"+ simpleName.substring(0, simpleName.length() - SUFFIX.length());
                HandlerInfo handlerInfo = new HandlerInfo(classInfo.loadClass());
                handlers.put(url.toLowerCase(),handlerInfo);
            }
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) throws ServletException {
        String contextPath= request.getContextPath();
        String uri = request.getRequestURI().substring(contextPath.length());
        return handlers.get(uri);
    }
}
