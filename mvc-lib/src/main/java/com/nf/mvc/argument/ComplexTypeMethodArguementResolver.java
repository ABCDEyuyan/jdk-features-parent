package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.ReflectionUtils;
import com.nf.mvc.util.WebTypeConverterUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

public class ComplexTypeMethodArguementResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParamType();
        return ReflectionUtils.isComplexProperty(paramType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        //BeanUtils来自于Apache的commons-beanutils工具包
        //如果你不想用BeanUtils实现，你可以用之前学过的Introspector
        Class<?> paramType = parameter.getParamType();
        Object instance = ReflectionUtils.newInstance(paramType);
        BeanUtils.populate(instance,request.getParameterMap());
        return instance;
    }
}
