package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.ReflectionUtils;
import com.nf.mvc.util.WebTypeConverterUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

public class SimpleTypeMethodArguementResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParamType();
        if( ReflectionUtils.isSimpleProperty(paramType)){
            return true;
        }
        if (ReflectionUtils.isAssignableToAny(paramType, Set.class,List.class)) {
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameter().getParameterizedType();
            Class<?> genericType = (Class) parameterizedType.getActualTypeArguments()[0];
            return ReflectionUtils.isSimpleType(genericType);
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        Object value ;
        String paramName = parameter.getParamName();
        //获取参数类型
        Class<?> paramType = parameter.getParamType();
        //转换为数组、集合、或具体类型的值
        if(paramType.isArray()) {
            Class<?> componentType = paramType.getComponentType();
            value = WebTypeConverterUtils.toSimpleTypeArray(componentType, request.getParameterValues(paramName));
        } else if(List.class.isAssignableFrom(paramType) || Set.class.isAssignableFrom(paramType)) {
            //获取集合泛型参数类型
            ParameterizedType type = (ParameterizedType) parameter.getParameter().getParameterizedType();
            Class<?> genericType = (Class) type.getActualTypeArguments()[0];
            value = WebTypeConverterUtils.toCollection(paramType, genericType, request.getParameterValues(paramName));
        } else {
            value = WebTypeConverterUtils.toSimpleTypeValue(paramType, request.getParameter(paramName));
        }
        return value;
    }
}
