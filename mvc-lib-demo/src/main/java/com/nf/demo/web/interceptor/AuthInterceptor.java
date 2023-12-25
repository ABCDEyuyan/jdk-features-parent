package com.nf.demo.web.interceptor;


import com.nf.mvc.HandlerInterceptor;
import com.nf.mvc.Intercepts;
import com.nf.mvc.support.vo.CommonResultCodeEnum;
import com.nf.mvc.support.vo.ResponseVO;
import com.nf.mvc.util.JacksonUtils;
import com.nf.mvc.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Intercepts(value = {"/product/insert", "/product/update", "/product/delete"}, excludePattern = "/user/login")
public class AuthInterceptor implements HandlerInterceptor {


    /**
     * 1.请求请求数据中是否有已登录的用户的相关信息
     * 2.如果有--->表明用户已经登录--》放行
     * 3.如果没有--->表明没有登录-->访问的地址是否是要登录才能访问-->阻止
     * --》放行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("auth");
        // 表明请求头有数据，意味着登录过了，所以放行
        if (StringUtils.hasText(authHeader)) {
            return true;
        }
        // 因为拦截器注解写了要拦截的地址，所以只会在请求地址就是要拦截的
        // 地址时，才会进到此拦截器,因为请求头没有信息，所以阻止请求，要求登录
        // 发一个响应给前端
        ResponseVO responseVO = ResponseVO.fail(CommonResultCodeEnum.FORBIDDEN);
        String json = JacksonUtils.toJson(responseVO);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .print(json);
        return false;

    }

}
