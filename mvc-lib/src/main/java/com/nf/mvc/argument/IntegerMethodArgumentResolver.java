package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public class IntegerMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParamType();
        return parameterType==Integer.class || parameterType == Integer.TYPE;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {

        return Integer.parseInt(request.getParameter(parameter.getParamName()));
    }
}
