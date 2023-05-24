package com.nf.web.filter;

import com.nf.mvc.HandlerInterceptor;
import com.nf.mvc.Interceptors;
import com.nf.mvc.util.JacksonUtils;
import com.nf.mvc.util.StringUtils;
import com.nf.utils.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName LoginInterceptor
 * @Author ZL
 * @Date 2023/5/12 9:03
 * @Version 1.0
 * @Explain
 **/

@Interceptors(value = {"/product/insert","product/update","product/delete"},excludePattern={"/user/login"})
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth = request.getHeader("auth");
//        if(!StringUtils.isNullOrEmpty(auth)) {
//            return true;
//        }
        //TODO:1
        ResponseVo responseVo=new ResponseVo(501,"未登录",null);
        String s = JacksonUtils.toJson(responseVo);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(s);
        return false;
    }

}
