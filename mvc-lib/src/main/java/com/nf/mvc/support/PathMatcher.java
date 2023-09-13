package com.nf.mvc.support;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;

/**
 * 路径模式匹配的接口，主要应用在HandlerMapping组件与拦截器组件里，
 * 用来解析当前请求用哪一个Handler去处理(见{@link com.nf.mvc.mapping.RequestMappingHandlerMapping#getHandler(HttpServletRequest)})
 * 以及拦截器是否要应用到当前请求上(见:{@link com.nf.mvc.mapping.RequestMappingHandlerMapping#getInterceptors(HttpServletRequest)})
 * @see AntPathMatcher
 * @see EqualPathMatcher
 * @see EqualIgnoreCasePathMatcher
 * @see com.nf.mvc.mapping.RequestMappingHandlerMapping
 */
public interface PathMatcher {
    /**
     * @param pattern:模式，比如/*,
     * @param path:具体的请求地址/a
     * @return
     */
    boolean isMatch(final String pattern, final String path);

    default Comparator<String> getPatternComparator(String path){
        throw new UnsupportedOperationException("不支持对模式进行比较");
    }
}
