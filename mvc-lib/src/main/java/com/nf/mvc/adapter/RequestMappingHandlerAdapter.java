package com.nf.mvc.adapter;

import com.nf.mvc.HandlerAdapter;
import com.nf.mvc.ViewResult;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.util.ReflectionUtils;
import com.nf.mvc.view.PlainViewResult;
import com.nf.mvc.view.VoidViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public ViewResult handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> handlerClass = ((HandlerMethod) handler).getHandlerClass();
        Object instance = ReflectionUtils.newInstance(handlerClass);

        Method method = handlerMethod.getHandlerMethod();

        List<String> paramNames = ReflectionUtils.getParamNames(handlerClass, method.getName());
        Parameter[] parameters = method.getParameters();
        int parameterCount = method.getParameterCount();
        Object[] paramValues = new Object[parameterCount];

        for (int i = 0; i < parameterCount; i++) {
            Parameter parameter = parameters[i];
            paramValues[i]= resolveArgument(parameter,paramNames.get(i),req);
        }
        //handler的方法没有要求一定要返回ViewResult（通常会返回ViewResult）
        //所以handler的方法执行之后，可能返回别的类型，或者void
        Object handlerResult = method.invoke(instance,paramValues);

        ViewResult viewResult ;
        if(handlerResult==null){
            //这种情况表示handler方法执行返回null或者方法的签名本身就是返回void
            viewResult = new VoidViewResult();
        } else if (handlerResult instanceof ViewResult) {
            viewResult = (ViewResult) handlerResult;
        }else{
            viewResult = new PlainViewResult(handlerResult.toString());
        }

        return viewResult;

    }

    private Object resolveArgument(Parameter parameter, String paramName,HttpServletRequest req) {
        Class<?> parameterType = parameter.getType();
        if(parameterType==Integer.class || parameterType==Integer.TYPE){
            return Integer.parseInt(req.getParameter(paramName));
        }
        if (parameterType == String.class) {
            return req.getParameter(paramName);
        }
        return null;
    }
}
