package com.nf.mvc.adapter;

import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.ViewResult;
import com.nf.mvc.argument.MethodParameter;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.util.ReflectionUtils;
import com.nf.mvc.view.PlainViewResult;
import com.nf.mvc.view.VoidViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static com.nf.mvc.ViewResult.handleViewResult;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private List<MethodArgumentResolver> argumentResolvers;

    public RequestMappingHandlerAdapter() {

        this(MvcContext.getMvcContext().getArgumentResolvers());
    }

    public RequestMappingHandlerAdapter(List<MethodArgumentResolver> argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod &&
                ((HandlerMethod) handler).getHandlerMethod() != null;
    }

    @Override
    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Object instance = handlerMethod.getInstance();

        Method method = handlerMethod.getHandlerMethod();

        Object[] paramValues = resolveParamValues(req, handlerMethod);
        //handler的方法没有要求一定要返回ViewResult（通常会返回ViewResult）
        //所以handler的方法执行之后，可能返回别的类型，或者void
        Object handlerResult = method.invoke(instance, paramValues);

        return handleViewResult(handlerResult);

    }

    private Object[] resolveParamValues(HttpServletRequest req, HandlerMethod handlerMethod) throws Exception {
        int parameterCount = handlerMethod.getParameterCount();
        Object[] paramValues = new Object[parameterCount];

        for (int i = 0; i < parameterCount; i++) {
            MethodParameter parameter = handlerMethod.getMethodParameters()[i];
            paramValues[i] = resolveArgument(parameter, req);
        }
        return paramValues;
    }

    private Object resolveArgument(MethodParameter parameter, HttpServletRequest req) throws Exception {
        Class<?> parameterType = parameter.getParamType();
        for (MethodArgumentResolver resolver : argumentResolvers) {
            if (resolver.supports(parameter)) {
                return resolver.resolveArgument(parameter, req);
            }
        }
        return null;
    }
}
