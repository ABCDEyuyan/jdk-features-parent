package com.nf.mvc.argument;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nf.mvc.MethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.nf.mvc.util.JacksonUtils.fromJson;
import static com.nf.mvc.util.ReflectionUtils.isList;

/**
 * 集合的反序列化参考了文章https://stackoverflow.com/questions/9829403/deserialize-json-to-arraylistpojo-using-jackson
 */
public class RequestBodyMethodArguementResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.getParameter().isAnnotationPresent(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        if (isList(paramType)) {
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameter().getParameterizedType();
            //假定方法参数是List<Emp>的话，那么下面的actualTypeArgument变量指的就是Emp
            Class<?> actualTypeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            //可以理解为实例化一个集合，比如new ArrayList<Emp>();
            CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, actualTypeArgument);
            return fromJson(request.getInputStream(), collectionType);
        } else {
            return fromJson(request.getInputStream(), paramType);
        }
    }
}
