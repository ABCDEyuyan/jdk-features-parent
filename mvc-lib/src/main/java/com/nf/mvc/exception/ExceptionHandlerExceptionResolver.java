package com.nf.mvc.exception;

import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.ViewResult;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.util.ScanUtils;
import com.nf.mvc.view.PlainViewResult;
import com.nf.mvc.view.VoidViewResult;
import io.github.classgraph.ClassInfoList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver {
    private List<HandlerMethod> exHandleMethods = new ArrayList<>();

    public ExceptionHandlerExceptionResolver() {
        scanHandleExMethods();
        sortExHandleMethods();
    }

    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object handleResult = null;
        for (HandlerMethod handlerMethod : exHandleMethods) {
            Method method = handlerMethod.getHandlerMethod();
            Class<?> exceptionClass = method.getDeclaredAnnotation(ExceptionHandler.class).value();
            if (exceptionClass.isAssignableFrom(ex.getClass())) {
                try {
                    Object instance = method.getDeclaringClass().getConstructor().newInstance();
                    handleResult = method.invoke(instance, ex);
                    return handleViewResult(handleResult);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    private ViewResult handleViewResult(Object handlerResult) {
        ViewResult viewResult ;
        if(handlerResult ==null){
            //这种情况表示handler方法执行返回null或者方法的签名本身就是返回void
            viewResult = new VoidViewResult();
        } else if (handlerResult instanceof ViewResult) {
            viewResult = (ViewResult) handlerResult;
        }else{
            viewResult = new PlainViewResult(handlerResult.toString());
        }

        return viewResult;
    }
    private void scanHandleExMethods() {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScanedClasses();

        for (Class<?> clz : classList) {
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(ExceptionHandler.class)) {
                    HandlerMethod exHandleMethod = new HandlerMethod(method);
                    exHandleMethods.add( exHandleMethod);
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
