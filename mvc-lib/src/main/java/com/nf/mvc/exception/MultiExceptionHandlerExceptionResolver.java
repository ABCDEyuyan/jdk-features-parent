package com.nf.mvc.exception;

import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 本异常解析器支持异常处理方法通过注解{@link MultiExceptionHandler}指定能处理多个异常的情况，
 * 使用情况如下：
 * <pre class="code">
 *     &#64;MultiExceptionHandler({AException.class,BException.class})
 *     public void handleExAAndB(Exception ex){
 *
 *     }
 * </pre>
 * 注意，这种情况异常处理方法的参数仍然只能是一个异常类型的参数，而且是它声明的多个异常类型的共同父类才可以
 * <p>本类没有应用在框架中，只是用来写着玩的，也没有经过严格的测试</p>
 * @see com.nf.mvc.DispatcherServlet
 * @see HandlerExceptionResolver
 * @see ExceptionHandlerExceptionResolver
 */
public class MultiExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

    /**
     * 这样重写之后，相当于只有scan功能，没有添加handle的功能，等价于把方法{@link #handleExceptionHandlerMethods(List)}
     * 重写为一个空的方法
     */
    @Override
    protected void resolveExceptionHandlerMethods() {
        scanExceptionHandlerMethods(method -> method.isAnnotationPresent(MultiExceptionHandler.class));
    }

    @Override
    protected HandlerMethod findMostMatchedHandlerMethod(List<HandlerMethod> handlerMethods,Exception exception) {
        HandlerMethod mostMatchedHandlerMethod = null;
        Class<?> mostMatchedExceptionClass = null;
        for (HandlerMethod exHandleMethod : handlerMethods) {
            Method method = exHandleMethod.getHandlerMethod();
            Class<? extends Exception>[] exceptionClasses = method.getDeclaredAnnotation(MultiExceptionHandler.class).value();
            Class<?> matchedExceptionClass;
            for (Class<? extends Exception> exceptionClass : exceptionClasses) {
                if (exceptionClass.isAssignableFrom(exception.getClass())) {
                    matchedExceptionClass = exceptionClass;
                    if (mostMatchedExceptionClass == null) {
                        mostMatchedExceptionClass = matchedExceptionClass;
                    }
                    if (mostMatchedExceptionClass.isAssignableFrom(matchedExceptionClass)) {
                        mostMatchedExceptionClass = matchedExceptionClass;
                        mostMatchedHandlerMethod = exHandleMethod;
                    }
                }
            }
        }
        return mostMatchedHandlerMethod;
    }
}
