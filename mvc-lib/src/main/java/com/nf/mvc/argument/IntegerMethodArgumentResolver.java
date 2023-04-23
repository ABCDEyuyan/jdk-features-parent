package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public class IntegerMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(Parameter parameter) {
        Class<?> parameterType = parameter.getType();
        return parameterType==Integer.class || parameterType == Integer.TYPE;
    }

    @Override
    public Object resolveArgument(Parameter parameter, HttpServletRequest request) throws Exception {
        //parameter.getName()???
        //Integer.parset(request.getParameter(n))
        return null;
    }
}
