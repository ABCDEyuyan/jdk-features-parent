package com.nf.mvc.exception;

import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.ViewResult;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.view.PlainViewResult;
import com.nf.mvc.view.VoidViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nf.mvc.ViewResult.handleViewResult;
import static com.nf.mvc.util.ExceptionUtils.getRootCause;

public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver {
    private List<HandlerMethod> exHandleMethods = new ArrayList<>();

    public ExceptionHandlerExceptionResolver() {
        scanHandleExMethods();
        sortExHandleMethods();
    }

    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object handleResult;
        Exception exposedException = (Exception) getRootCause(ex);
        for (HandlerMethod handlerMethod : exHandleMethods) {
            Method method = handlerMethod.getHandlerMethod();
            Class<?> exceptionClass = method.getDeclaredAnnotation(ExceptionHandler.class).value();
            if (exceptionClass.isAssignableFrom(exposedException.getClass())) {
                try {
                    Object instance = method.getDeclaringClass().getConstructor().newInstance();
                    handleResult = method.invoke(instance, exposedException);
                    return handleViewResult(handleResult);
                } catch (Exception e) {
                    /* 进入到这里就是异常处理方法本身的执行出了错，通过返回null的形式，
                    就继续交给下一个异常解析器去处理，下一个异常解析器处理的仍然是同一个异常对象*/
                    return null;
                }
            }
        }
        return null;
    }


    private void scanHandleExMethods() {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();

        for (Class<?> clz : classList) {
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(ExceptionHandler.class)) {
                    HandlerMethod exHandleMethod = new HandlerMethod(method);
                    exHandleMethods.add(exHandleMethod);
                }
            }
        }
    }

    private void sortExHandleMethods() {
        //RuntimeException.class.isAssignable(ArithmeticException.class)
        Collections.sort(exHandleMethods, (m1, m2) ->
                m1.getHandlerMethod().getDeclaredAnnotation(ExceptionHandler.class).value()
                        .isAssignableFrom(m2.getHandlerMethod().getDeclaredAnnotation(ExceptionHandler.class).value()) ? 1 : -1);
    }

}
