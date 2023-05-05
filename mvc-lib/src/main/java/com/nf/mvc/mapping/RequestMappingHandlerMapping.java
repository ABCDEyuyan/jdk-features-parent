package com.nf.mvc.mapping;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nf.mvc.HandlerExecutionChain;
import com.nf.mvc.HandlerInterceptor;
import com.nf.mvc.HandlerMapping;
import com.nf.mvc.MvcContext;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.support.AntPathMatcher;
import com.nf.mvc.support.EqualPathMatcher;
import com.nf.mvc.support.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private static final PathMatcher defaultPathMatcher = new AntPathMatcher.Builder().build();

    private Map<String, HandlerMethod> handlers = new HashMap<>();

    private PathMatcher pathMatcher = new EqualPathMatcher();

    Cache<String, HandlerExecutionChain> cache = Caffeine.newBuilder()
            .initialCapacity(10)
            .maximumSize(100)
            .build();

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
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String requestUrl = getRequestUrl(request);
        /* get方法的第二个参数是在缓存中没有对应的key值执行的函数，其返回值会自动放置到缓存中
        * 如果返回值是null，那么不会放置到缓存中。
        * 所以，在这个案例中，如果url没有对应的handler，那么就返回null，cache中不会放置这个不存在url的缓存条目 */
        HandlerExecutionChain chain = cache.get(requestUrl,k->{
            HandlerMethod handler = handlers.get(requestUrl);
            if (handler != null) {
               return new HandlerExecutionChain(handler, getInterceptors(request));
            }
            return null;
        });
        return chain;
    }

    /**
     * @param element AnnotatedElement类型代表着所有可以放置注解的元素，比如类，方法参数，字段等
     * @return 返回RequestMapping注解中指定的url值
     */
    private String getUrl(AnnotatedElement element) {
        return element.isAnnotationPresent(RequestMapping.class) ?
                element.getDeclaredAnnotation(RequestMapping.class).value() : "";
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }
}
