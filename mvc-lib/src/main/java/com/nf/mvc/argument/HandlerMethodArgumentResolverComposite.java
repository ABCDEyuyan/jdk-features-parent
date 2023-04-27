package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;

public class HandlerMethodArgumentResolverComposite implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        return null;
    }
}
