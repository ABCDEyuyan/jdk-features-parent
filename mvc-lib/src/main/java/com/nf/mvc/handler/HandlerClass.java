package com.nf.mvc.handler;

import com.nf.mvc.util.ReflectionUtils;


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
public class HandlerClass {
    private Class<?> handlerClass;
    private Object handlerObject;

    public HandlerClass(Class<?> handleClass) {
        this.handlerClass = handleClass;
    }

    public HandlerClass(Object handleObject) {
        this.handlerObject = handleObject;
    }

    public String getClassSimpleName(){
        return handlerClass!=null?
                handlerClass.getSimpleName():
                handlerObject.getClass().getSimpleName();
    }
    public Class<?> getHandlerClass() {
        return handlerClass;
    }


    public Object getHandlerObject() {
        return handlerObject;
    }

    public Object getInstance(){
        if (handlerObject != null) {
            return  handlerObject;
        }
        return ReflectionUtils.newInstance(handlerClass);
    }
}
