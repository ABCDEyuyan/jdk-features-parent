package com.nf.mvc.handler;

import com.nf.mvc.argument.MethodParameter;
import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 此类是一个对请求方法进行封装的类型。
 * <p>
 *     既然用到此类，就表明是有方法的，所以父类注释中说的4种情况，只有2种情况是需要本类处理的：
 *     <ul>
 *         <li>有HandlerClass，有方法，对应{@link #HandlerMethod(Class, Method)}与{@link #HandlerMethod(Method)},
 *         后一个构造函数只是一个便利的构造函数
 *         </li>
 *         <li>有Handler对象，有方法，对应{@link #HandlerMethod(Object, Method)}</li>
 *     </ul>
 * </p>
 */
public class HandlerMethod extends HandlerClass {

    private Method handlerMethod;

    private MethodParameter[] methodParameters;

    public HandlerMethod(Method handlerMethod) {
        this(handlerMethod.getDeclaringClass(), handlerMethod);
    }

    /**
     * 这个构造函数几乎不会被用到，直接用第一个构造函数更方便
     * @param handlerClass
     * @param handlerMethod
     */
    public HandlerMethod(Class<?> handlerClass, Method handlerMethod) {
        super(handlerClass);
        this.handlerMethod = handlerMethod;
        initMethodParameters();
    }

    public HandlerMethod(Object handlerObject, Method handlerMethod) {
        this(handlerObject.getClass(), handlerMethod);
    }

    private void initMethodParameters() {
        List<String> paramNames = ReflectionUtils.getParameterNames(handlerMethod);
        int parameterCount = handlerMethod.getParameterCount();
        methodParameters = new MethodParameter[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            MethodParameter methodParameter = new MethodParameter(handlerMethod, i,paramNames.get(i));
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


