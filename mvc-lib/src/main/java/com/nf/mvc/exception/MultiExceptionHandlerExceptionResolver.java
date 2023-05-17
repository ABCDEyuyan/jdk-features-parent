package com.nf.mvc.exception;

import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.ViewResult;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.nf.mvc.ViewResult.adaptHandlerResult;
import static com.nf.mvc.util.ExceptionUtils.getRootCause;

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
 */
public class MultiExceptionHandlerExceptionResolver implements HandlerExceptionResolver {

    private List<HandlerMethod> exHandleMethods = new ArrayList<>();

    public MultiExceptionHandlerExceptionResolver() {
        fetchExHandleMethods();
    }

    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object handleResult;
        Exception exposedException = (Exception) getRootCause(ex);
        HandlerMethod handlerMethod = findMostMatchHandlerMethod(exposedException);
        if (handlerMethod == null) {
            return null;
        }
        Method method = handlerMethod.getHandlerMethod();
        try {
            Object instance = ReflectionUtils.newInstance(method.getDeclaringClass());
            handleResult = method.invoke(instance, exposedException);
            return adaptHandlerResult(handleResult);
        } catch (Exception e) {
           /* 进入到这里就是异常处理方法本身的执行出了错，catch里什么都不干，相当于吞掉异常处理方法本身的异常，
            异常处理方法无法正确的处理异常，也就是说本异常解析器无法处理异常，这样，通过返回null的形式，
            就继续交给下一个异常解析器去处理，下一个异常解析器处理的仍然是最开始抛出的异常，也就是这个方法被调用时传递的第四个参数的值 */
            return null;
        }
    }

    protected void fetchExHandleMethods() {
        scanExHandleMethods(method -> method.isAnnotationPresent(MultiExceptionHandler.class));
    }

    protected void scanExHandleMethods(Predicate<Method> predicate) {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScannedClasses();

        for (Class<?> clz : classList) {
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (predicate.test(method)) {
                    HandlerMethod exHandleMethod = new HandlerMethod(method);
                    exHandleMethods.add(exHandleMethod);
                }
            }
        }
    }

    protected HandlerMethod findMostMatchHandlerMethod(Exception exception) {
        HandlerMethod mostMatchHandlerMethod = null;
        Class<?> mostMatchExceptionClass = null;
        for (HandlerMethod exHandleMethod : exHandleMethods) {
            Method method = exHandleMethod.getHandlerMethod();
            Class<? extends Exception>[] exceptionClasses = method.getDeclaredAnnotation(MultiExceptionHandler.class).value();
            Class<?> matchExceptionClass = null;
            for (Class<? extends Exception> exceptionClass : exceptionClasses) {
                if (exceptionClass.isAssignableFrom(exception.getClass())) {
                    matchExceptionClass = exceptionClass;
                    if (mostMatchExceptionClass == null) {
                        mostMatchExceptionClass = matchExceptionClass;
                    }
                    if (mostMatchExceptionClass.isAssignableFrom(matchExceptionClass)) {
                        mostMatchExceptionClass = matchExceptionClass;
                        mostMatchHandlerMethod = exHandleMethod;
                    }
                }
            }
        }
        return mostMatchHandlerMethod;
    }
}
