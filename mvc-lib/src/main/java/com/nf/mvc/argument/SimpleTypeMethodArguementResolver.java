package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.ReflectionUtils;
import com.nf.mvc.util.WebTypeConverterUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

public class SimpleTypeMethodArguementResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParamType();
        if (ReflectionUtils.isSimpleProperty(paramType)) {
            return true;
        }
        if (ReflectionUtils.isAssignableToAny(paramType, Set.class, List.class)) {
            //List<String> 支持，不仅是一个List集合，并且其泛型实参是String,所以就支持
            //但List<Emp>就不支持，因为其实参是Emp
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameter().getParameterizedType();
            Class<?> genericType = (Class) parameterizedType.getActualTypeArguments()[0];
            return ReflectionUtils.isSimpleType(genericType);
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        Object value;
        String paramName = getName(parameter);
        //获取参数类型
        Class<?> paramType = parameter.getParamType();
        //转换为数组、集合、或具体类型的值
        if (paramType.isArray()) {
            Class<?> componentType = paramType.getComponentType();
            value = WebTypeConverterUtils.toSimpleTypeArray(componentType, request.getParameterValues(paramName));
        } else if (List.class.isAssignableFrom(paramType) || Set.class.isAssignableFrom(paramType)) {
            //获取集合泛型参数类型
            ParameterizedType type = (ParameterizedType) parameter.getParameter().getParameterizedType();
            Class<?> genericType = (Class) type.getActualTypeArguments()[0];
            value = WebTypeConverterUtils.toCollection(paramType, genericType, request.getParameterValues(paramName));
        } else {
            //比如String name,简单类型解析要考虑RequestParam注解
            //如果出现转换异常？比如前端没有传递值，那么转成Integer这种类型就报异常
            //前端没有传值，那么getP就是null，赋给String这种是可以，但这个defaultValue要开始生效

            //toSimpleTypeValue方法的实现，是转换出了异常就返回null
            value = WebTypeConverterUtils.toSimpleTypeValue(paramType, request.getParameter(paramName));
            if (value == null && ReflectionUtils.isPrimitive(paramType)) {
                throw new IllegalArgumentException("参数名:" + paramName +" 的值为null，不能把null给简单类型:" + paramType.getName());
            }

            if (value==null && parameter.getParameter().isAnnotationPresent(RequestParam.class)) {
                String defaultValue = parameter.getParameter().getDeclaredAnnotation(RequestParam.class).defaultValue();
                if (!defaultValue.equals(ValueConstants.DEFAULT_NONE)) {
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    private String getName(MethodParameter parameter) {
        String name = parameter.getParamName();
        Parameter param = parameter.getParameter();
        //参数上有注解，可能只是用来设置默认值，没有设置value
        if(param.isAnnotationPresent(RequestParam.class) ){
            String value = param.getDeclaredAnnotation(RequestParam.class).value();
                //没有设置value，value就会保留默认值，这个值我们不采用，
            // 仍然用方法的参数名（javassist解析出来的
            //如果你有注解，并且设置了value，我们才采用
            if (value.equals(ValueConstants.DEFAULT_NONE) == false) {
                name = value;
            }
        }
        return name;

    }
}
