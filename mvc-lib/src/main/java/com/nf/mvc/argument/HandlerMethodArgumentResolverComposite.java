package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.MvcContext;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * composite:复合，组合的意思，就是表示此类管理很多的解析器
 */
public class HandlerMethodArgumentResolverComposite implements MethodArgumentResolver {

    private final List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();

    private final Map<MethodParameter, MethodArgumentResolver> resolverCache = new ConcurrentHashMap<>(256);

    public HandlerMethodArgumentResolverComposite addResolver(MethodArgumentResolver resolver) {
        argumentResolvers.add(resolver);
        return this;
    }

    public HandlerMethodArgumentResolverComposite addResolvers(MethodArgumentResolver... resolvers) {
        if(resolvers!=null) {
            Collections.addAll(this.argumentResolvers, resolvers);
        }
        return this;
    }

    public List<MethodArgumentResolver> getResolvers() {
        return Collections.unmodifiableList(this.argumentResolvers);
    }

    public void clear() {
        this.argumentResolvers.clear();
    }


    public HandlerMethodArgumentResolverComposite addResolvers(List<MethodArgumentResolver> resolvers) {
        if(resolvers!=null) {
            this.argumentResolvers.addAll(resolvers);
        }
        return this;
    }

    @Override
    public boolean supports(MethodParameter parameter) {
        return getArgumentResolver(parameter) !=null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        MethodArgumentResolver resolver = getArgumentResolver(parameter);
        if (resolver == null) {
            throw new IllegalArgumentException("不支持的参数类型 [" +
                    parameter.getParamType() + "]. supportsParameter 方法应该先调用");
        }
        return resolver.resolveArgument(parameter, request);

    }


    private MethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
        MethodArgumentResolver result = resolverCache.get(parameter);
        if (result == null) {
            for (MethodArgumentResolver argumentResolver : argumentResolvers) {
                if (argumentResolver.supports(parameter)) {
                    result= argumentResolver;
                    resolverCache.put(parameter, argumentResolver);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 此方法创建的实例已经是包含框架提供的解析器与用户提供的解析器了，你可以在此基础上额外再添加一些解析器
     * @return
     */
    public static HandlerMethodArgumentResolverComposite defaultInstance(){
        return new HandlerMethodArgumentResolverComposite()
                .addResolvers(MvcContext.getMvcContext().getArgumentResolvers());

    }
}
