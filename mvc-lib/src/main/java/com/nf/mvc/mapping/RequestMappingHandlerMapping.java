package com.nf.mvc.mapping;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nf.mvc.*;
import com.nf.mvc.argument.MethodArgumentResolverComposite;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.support.PathMatcher;
import com.nf.mvc.support.path.AntPathMatcher;
import com.nf.mvc.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

import static com.nf.mvc.mapping.RequestMappingUtils.getUrlPattern;
import static com.nf.mvc.util.ObjectUtils.isEmpty;

/**
 * 此类是Mvc框架的核心{@link HandlerMapping}实现，其核心的功能有
 * <h3>什么是Handler</h3>
 * <p>所有扫描到的方法上有{@link RequestMapping}注解的都可以称之为Handler，如果类上也有此注解，
 * 那么就表明此Handler能处理的请求是由两段地址组成的，比如下面的handler能处理的地址就是<i>/product/list</i>
 * <pre class="code">
 *   &#64;RequestMapping("/product")
 *   public class ProductController{
 *       &#64;RequestMapping("/list")
 *       public ViewResult list(){...}
 *   }
 * </pre>
 * </p>
 * <h3>请求匹配</h3>
 * <p>此类实现借助{@link PathMatcher}可以实现灵活的地址匹配模式，比如区分大小写，不区分大小写，Ant地址模式匹配能，此类默认使用的AntPathMatcher。
 * 一般是在Handler类以及方法上通过注解{@link RequestMapping}指定地址方法，比如/**,然后依据当前请求的requestURI（去掉上下文）的地址进行模式匹配，
 * 能匹配上就返回此Handler作为处理者
 * </p>
 * <h3>缓存</h3>
 * <p>此类利用caffeine进行了缓存实现，会缓存100条url对应的HandlerExecutionChain，避免每次请求过来都去查找执行链以提高性能,
 * caffeine采用的是类似LFU(最近最少使用频率）的淘汰算法，具体见<a href="https://www.cnblogs.com/rickiyang/p/11074158.html">Caffeine Cache-高性能Java本地缓存组件</a>
 * 整个Mvc框架在缓存上的应用的详细介绍见{@link MethodArgumentResolverComposite}</p>
 *
 * @see com.nf.mvc.HandlerMapping
 * @see PathMatcher
 * @see AntPathMatcher
 * @see RequestMapping
 */
public class RequestMappingHandlerMapping implements HandlerMapping {
    private final Map<String, HandlerMethod> handlers = new HashMap<>();
    private PathMatcher pathMatcher = PathMatcher.DEFAULT_PATH_MATCHER;
    private final Cache<String, HandlerExecutionChain> chainCache = Caffeine.newBuilder()
            .initialCapacity(10)
            .maximumSize(100)
            .build();

    public RequestMappingHandlerMapping() {
        resolveHandlers();
    }

    protected void resolveHandlers() {
        List<Class<?>> scannedClasses = MvcContext.getMvcContext().getAllScannedClasses();

        for (Class<?> scannedClass : scannedClasses) {
            String urlInClass = getUrlPattern(scannedClass);
            Method[] methods = scannedClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    HandlerMethod handlerMethod = new HandlerMethod(method);
                    String urlInMethod = getUrlPattern(method);
                    String url = urlInClass + urlInMethod;
                    addHandler(url, handlerMethod);
                }
            }
        }
    }

    protected void addHandler(String url, HandlerMethod handlerMethod) {
        HandlerMethod handler = handlers.get(url);
        if (handler != null) {
            throw new IllegalStateException("当前的url:[" + url + "]已经有一个对应的handler了:" + handler
                    + ",现在想添加的handler是:" + handlerMethod);
        }
        this.handlers.put(url, handlerMethod);
    }

    @SuppressWarnings("RedundantThrows")
    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String requestUrl = RequestUtils.getRequestUrl(request);
        /* get方法的第二个参数是在缓存中没有对应的key时执行的函数，其返回值会自动放置到缓存中
         * 如果返回值是null，那么不会放置到缓存中。
         * 所以，在这个案例中，如果url没有对应的执行链，那么就返回null，chainCache中不会放置url没有对应链的缓存条目 */
        return chainCache.get(requestUrl, k -> {
            HandlerMethod handler = getHandlerInternal(requestUrl);
            if (handler != null) {
                return new HandlerExecutionChain(handler, getInterceptors(request));
            }
            return null;
        });
    }

    protected HandlerMethod getHandlerInternal(String requestUrl) {
        HandlerMethod handler = null;

        Set<String> keys = handlers.keySet();
        List<String> patternKeys = new ArrayList<>(keys);
        patternKeys.sort(getPathMatcher().getPatternComparator(requestUrl));

        for (String key : patternKeys) {
            if (getPathMatcher().isMatch(key, requestUrl)) {
                handler = handlers.get(key);
                break;
            }
        }
        return handler;
    }

    @Override
    public List<HandlerInterceptor> getInterceptors(HttpServletRequest request) {
        List<HandlerInterceptor> currentRequestInterceptors = new ArrayList<>();
        List<HandlerInterceptor> allInterceptors = MvcContext.getMvcContext()
                .getCustomHandlerInterceptors();
        String requestUrl = RequestUtils.getRequestUrl(request);
        for (HandlerInterceptor interceptor : allInterceptors) {
            Class<? extends HandlerInterceptor> interceptorClass = interceptor.getClass();
            if (interceptorClass.isAnnotationPresent(Intercepts.class)) {
                Intercepts intercepts = interceptorClass.getDeclaredAnnotation(Intercepts.class);
                String[] includesPattern = intercepts.value();
                String[] excludesPattern = intercepts.excludePattern();
                PathMatcher pathMatcher = getInterceptorPathMatcher(intercepts);
                if (shouldApply(pathMatcher, requestUrl, includesPattern) &&
                        !shouldApply(pathMatcher, requestUrl, excludesPattern)) {
                    currentRequestInterceptors.add(interceptor);
                }
            } else {
                // 没有注解修饰的拦截器被认为是拦截所有的请求，完全不理会当前请求url是什么
                currentRequestInterceptors.add(interceptor);
            }
        }
        return currentRequestInterceptors;
    }

    protected boolean shouldApply(PathMatcher pathMatcher, String requestUrl, String... patterns) {
        boolean shouldApply = false;
        if (isEmpty(patterns)) {
            return false;
        }
        for (String pattern : patterns) {
            shouldApply = pathMatcher.isMatch(pattern, requestUrl);
            if (shouldApply) {
                break;
            }
        }
        return shouldApply;
    }

    /**
     * 此方法用来获取拦截器地址的路径匹配器，获取Handler的路径匹配器与拦截器的路径匹配器是可以不一样的，
     * 当前的实现是两者都是一样的，并且可以通过MvcConfigurer来统一调整Handler与拦截器的路径匹配器
     *
     * @param intercepts 拦截器注解
     * @return 拦截器使用的路径匹配器
     */
    protected PathMatcher getInterceptorPathMatcher(Intercepts intercepts) {
        return getPathMatcher();
    }

    /**
     * 可以通过实现自定义的{@link MvcConfigurer}来配置此HandlerMapping的PathMatcher
     *
     * @param pathMatcher 路径匹配器
     */
    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }
}
