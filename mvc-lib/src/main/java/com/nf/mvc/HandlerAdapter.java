package com.nf.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>handler方法为了灵活，可以返回以下类型
 * <ul>
 *     <li>可以返回void，这种情况一般是handler方法直接利用response对象响应请求</li>
 *     <li>返回{@code ViewResult}</li>
 *     <li>返回其它类型，这种情况比较少见(不建议)，现在框架直接把其适配为返回{@link com.nf.mvc.view.PlainViewResult}</li>
 * </ul>
 * </p>
 * <p>通过反射调用一个返回类型为void的方法时，其返回值是null的。
 * 所以，一个Handler方法反射调用返回null，可能是void返回类型或者本身return null
 * </p>
 * @author cj
 * @see com.nf.mvc.view.PlainViewResult
 * @see com.nf.mvc.ViewResult
 * @see com.nf.mvc.adapter.RequestMappingHandlerAdapter
 */
public interface HandlerAdapter {
    boolean supports(Object handler);

    ViewResult handle(HttpServletRequest req, HttpServletResponse resp,Object handler) throws Exception;
}
