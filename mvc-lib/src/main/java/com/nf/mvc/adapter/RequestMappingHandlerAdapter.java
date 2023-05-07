package com.nf.mvc.adapter;

import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.ViewResult;
import com.nf.mvc.argument.HandlerMethodArgumentResolverComposite;
import com.nf.mvc.argument.MethodParameter;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.support.MethodInvoker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

import static com.nf.mvc.ViewResult.adaptHandlerResult;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private static final HandlerMethodArgumentResolverComposite defaultResolvers = HandlerMethodArgumentResolverComposite.defaultInstance();

    private final HandlerMethodArgumentResolverComposite resolvers;
    private final MethodInvoker methodInvoker;

    public RequestMappingHandlerAdapter() {
        this(defaultResolvers);
    }

    public RequestMappingHandlerAdapter(HandlerMethodArgumentResolverComposite resolvers) {
        this.resolvers = resolvers;
        methodInvoker = new MethodInvoker(resolvers);
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod &&
                ((HandlerMethod) handler).getHandlerMethod() != null;
    }

    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Object instance = handlerMethod.getInstance();
        Method method = handlerMethod.getHandlerMethod();

        Object handlerResult = methodInvoker.invoke(instance, method, req);
        return adaptHandlerResult(handlerResult);

    }
}
