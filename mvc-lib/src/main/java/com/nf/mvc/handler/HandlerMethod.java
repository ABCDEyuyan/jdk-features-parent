package com.nf.mvc.handler;

import java.lang.reflect.Method;

/**
 * 此类是一个封装handler相关信息的类型,现在支持以下几种类型的Handler
 * <ul>
 *     <li>只有handlerClass信息，无方法信息，具体调用哪个方法由具体的{@codeHandlerAdapter}决定</li>
 *     <li>只有Handler信息，无方法信息，具体调用哪个方法由具体的{@codeHandlerAdapter}决定</li>
 *     <li>有HandlerClass信息，有方法信息，实例化由mvc框架处理</li>
 *     <li>有Handler信息，有方法信息，实例化不需要mvc框架处理</li>
 * </ul>
 *
 * @see com.nf.mvc.HandlerAdapter
 * @see com.nf.mvc.adapter.RequestMappingHandlerAdapter
 */
public class HandlerMethod {
    /** 处理者的Class*/
    private Class<?> handlerClass;
    private Method handlerMethod;
    private Object handler;

    public HandlerMethod(Method handlerMethod) {
        this(handlerMethod.getDeclaringClass(), handlerMethod);
    }

    public HandlerMethod(Class<?> handlerClass) {
        this(handlerClass, null);
    }

    public HandlerMethod(Class<?> handlerClass, Method handlerMethod) {
        this.handlerClass = handlerClass;
        this.handlerMethod = handlerMethod;
    }

    public HandlerMethod(Object handler) {
        this(handler, null);
    }

    public HandlerMethod(Object handler, Method handlerMethod) {
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }
    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public Object getHandler() {
        return handler;
    }
}


