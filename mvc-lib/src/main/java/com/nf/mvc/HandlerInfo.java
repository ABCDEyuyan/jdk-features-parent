package com.nf.mvc;

/**
 * 此类是一个封装handler相关信息的类型,我们现在所有的类都是扫描得到的
 * 所以，基本都是class信息，基本没有实例形式的Handler，这点与spring mvc是不同的
 * spring mvc中的handler很多都是从spring容器中获取的bean 实例。
 * 如果想让HandlerInfo支持实例，可以添加一个字段Object handler的形式来处理
 */
public class HandlerInfo {
    /** 处理者的Class*/
    private Class<?> handlerClass;

    public HandlerInfo(Class<?> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public Class<?> getHandlerClass() {
        return handlerClass;
    }
}
