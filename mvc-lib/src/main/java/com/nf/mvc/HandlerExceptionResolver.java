package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 编写自己的异常解析时，如果异常不是你能处理的，就尽快返回null结束处理
 * 比如
 * <code>
 *     public class MyHandlerExceptionResolver implements HandlerExceptionResolver{
 *         ViewResult resolveException(
 *             HttpServletRequest request, HttpServletResponse response,  Object handler, Exception ex){
 *                 if(!(ex instanceOf RuntimeException)){
 *                     return null;
 *                 }
 *                 //下面才是真正的解析逻辑
 *             }
 *     }
 * </code>
 */
public interface HandlerExceptionResolver {
    /**
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return 返回null表示本异常解析器不能解析此异常,会继续交给下一个异常解析器去处理
     * @see com.nf.mvc.exception.ExceptionHandlerExceptionResolver
     */
    ViewResult resolveException(
            HttpServletRequest request, HttpServletResponse response,  Object handler, Exception ex);
}
