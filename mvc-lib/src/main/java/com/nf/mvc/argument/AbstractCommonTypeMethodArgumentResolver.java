package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.ObjectUtils;
import com.nf.mvc.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 此类型针对某个类型，及其数组类型、List类型的一个基类实现，比如MultipartFile，MultipartFile[],List<MultipartFile>这样的情况<br/>
 * 并不要求所有的参数解析器继承此类型，比如@RequestBody修饰的参数的解析器就完全没有必要继承此类型,servlet Api的参数解析器也不需要继承此类
 *
 * @see RequestBodyMethodArguementResolver
 * @see MultipartFileMethodArgumentResolver
 * @see SimpleTypeMethodArguementResolver
 */
public abstract class AbstractCommonTypeMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return isSupportedType(parameter) ||
                isSupportedTypeArray(parameter) ||
                isSupportedTypeList(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        //请求中根本没有对应参数名的请求数据时，source可能就是null的，因而objectArray也是空数组
        //这里不直接依据source为null值return结束方法的执行，是因为简单类型参数解析器还需要对这些null值进行一些额外的逻辑处理
        Object source = getSource(parameter, request);
        //如果source为null，会返回一个长度为0的空数组
        Object[] objectArray = ObjectUtils.toObjectArray(source);
        int length = Array.getLength(objectArray);
        if (isSupportedType(parameter)) {
           return resolveArgumentInternal(parameter.getParamType(), length==0?null:getSingleSource(objectArray,parameter),parameter);
        } else if (isSupportedTypeArray(parameter)) {
            Object array = Array.newInstance(parameter.getComponentType(), length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, resolveArgumentInternal(parameter.getComponentType(), Array.get(objectArray, i),parameter));
            }
            return array;
        } else if (isSupportedTypeList(parameter)) {
            List list = new ArrayList();
            for (int i = 0; i < length; i++) {
                list.add(resolveArgumentInternal(parameter.getFirstActualTypeArgument(), Array.get(objectArray, i),parameter));
            }
            return list;
        }
        return null;
    }


    protected abstract boolean supportsInternal(Class<?> type);

    protected abstract Object resolveArgumentInternal(Class<?> type, Object parameterValue,MethodParameter methodParameter) throws Exception;

    protected abstract Object[] getSource(MethodParameter methodParameter, HttpServletRequest request);

    protected abstract Object getSingleSource(Object[] sources,MethodParameter methodParameter);
    private boolean isSupportedType(MethodParameter methodParameter) {
        return supportsInternal(methodParameter.getParamType());
    }

    private boolean isSupportedTypeArray(MethodParameter methodParameter) {
        return methodParameter.isArray() && supportsInternal(methodParameter.getComponentType());
    }

    private boolean isSupportedTypeList(MethodParameter methodParameter) {
        return methodParameter.isList() && supportsInternal(methodParameter.getFirstActualTypeArgument());
    }
}
