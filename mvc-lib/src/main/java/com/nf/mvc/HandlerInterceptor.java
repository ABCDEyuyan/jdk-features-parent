package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器实现，这个拦截器类似于Servlet技术中的Filter，执行情况也类似，与Spring Mvc的拦截器执行逻辑是不同的。
 * 假定有3个拦截器I1,I2,I3,其中I2拦截器的preHandle返回false，那么执行顺序如下：
 * <ol>
 *     <li>I1.preHandle</li>
 *     <li>I1.preHandle(正是此前置代码返回false才阻止了链的执行)</li>
 *     <li>I1.postHandle</li>
 * </ol>
 * 如果没有一个拦截器的前置逻辑返回false，那么执行顺序如下
 * <ol>
 *     <li>I1.preHandle</li>
 *     <li>I2.preHandle</li>
 *     <li>I3.preHandle</li>
 *     <li>handler</li>
 *     <li>I3.postHandle</li>
 *     <li>I2.postHandle</li>
 *     <li>I1.postHandle</li>
 * </ol>
 * <p>
 *     如果创建的拦截器没有通过注解{@link Interceptors}进行拦截url方面的设置，那么此拦截器默认拦截所有的请求，
 *     如果拦截器经过了注解的设置，那么只会对符合设置条件的请求进行拦截，实现逻辑见{@link com.nf.mvc.mapping.RequestMappingHandlerMapping#getInterceptors(HttpServletRequest)}
 * </p>
 * <p>
 *     如果要调整拦截器的顺序，就在拦截器类上添加{@link com.nf.mvc.support.Order}注解处理，值越大，顺序越靠后，
 *     不指定顺序，所有拦截器顺序是未定的。有一定随机性
 * </p>
 * @see Interceptors
 * @see com.nf.mvc.mapping.RequestMappingHandlerMapping
 */
public interface HandlerInterceptor {
    /**
     * 在Handler执行之前执行，返回true才继续后续的拦截器或Handler的执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return true;
    }

    /**
     * 在Handler执行完毕之后执行，与前置逻辑（preHandle）是反序执行的，
     * 即便是拦截器的前置逻辑或者handler出了异常，也要求后置逻辑能得到执行，具体实现见{@link DispatcherServlet#doDispatch(HttpServletRequest, HttpServletResponse, HandlerExecutionChain)}
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    }

}
