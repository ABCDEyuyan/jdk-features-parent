package com.nf.mvc.handler;

import com.nf.mvc.argument.MethodParameter;
import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;


public class HandlerMethod extends HandlerClass {

    private Method handlerMethod;

    private MethodParameter[] methodParameters;

    public HandlerMethod(Method handlerMethod) {
        this(handlerMethod.getDeclaringClass(), handlerMethod);
    }

    public HandlerMethod(Method handlerMethod, Object handlerObject) {
        this(handlerObject, handlerMethod);
    }

    private HandlerMethod(Class<?> handlerClass, Method handlerMethod) {
        super(handlerClass);
        this.handlerMethod = handlerMethod;
        initMethodParameters();
    }

    private HandlerMethod(Object handlerObject, Method handlerMethod) {
        super(handlerObject);
        this.handlerMethod = handlerMethod;
        initMethodParameters();
    }


    private void initMethodParameters() {

        List<String> paramNames = ReflectionUtils.getParamNames(getHandlerClass(), getMethodName());
        Parameter[] parameters = handlerMethod.getParameters();
        int parameterCount = handlerMethod.getParameterCount();
        methodParameters = new MethodParameter[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            MethodParameter methodParameter = new MethodParameter(parameters[i], paramNames.get(i));
            methodParameters[i] = methodParameter;
        }

    }

    public String getMethodName() {
        return handlerMethod.getName();
    }


    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public MethodParameter[] getMethodParameters() {
        return methodParameters;
    }

    public int getParameterCount() {
        return handlerMethod.getParameterCount();
    }
}


