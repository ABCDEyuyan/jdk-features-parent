package com.nf.mvc.argument;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.JacksonUtils;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import static com.nf.mvc.util.JacksonUtils.fromJson;
import static com.nf.mvc.util.JacksonUtils.getObjectMapper;
import static com.nf.mvc.util.ReflectionUtils.isCollection;
import static com.nf.mvc.util.ReflectionUtils.isListOrSet;

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
        Class<?> paramType = parameter.getParamType();
        if (isListOrSet(paramType)) {
            ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameter().getParameterizedType();
            Class<?> actualTypeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            CollectionType collectionType = null;
            if (List.class == paramType) {
                collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, actualTypeArgument);
            } else if (Set.class == paramType) {
                collectionType = TypeFactory.defaultInstance().constructCollectionType(Set.class, actualTypeArgument);
            }
            return fromJson(request.getInputStream(), collectionType);
        } else {
            return fromJson(request.getInputStream(), paramType);
        }
    }
}

class Emp {
}
