package com.nf.mvc.argument;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.ObjectMapperUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class RequestBodyMethodArguementResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.getParameter().isAnnotationPresent(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        ObjectMapper objectMapper = ObjectMapperUtils.getObjectMapper();
       //TODL:List集合反序列化问题
        Class<?> paramType = parameter.getParamType();
        return objectMapper.readValue(request.getInputStream(), paramType);
    }
}

