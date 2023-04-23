package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public class StringMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(Parameter parameter) {
        Class<?> parameterType = parameter.getType();
        return parameterType==String.class;
    }

    @Override
    public Object resolveArgument(Parameter parameter, HttpServletRequest request) throws Exception {
        //parameter.getName()???
        //Integer.parset(request.getParameter(n))
        return null;
    }
}
