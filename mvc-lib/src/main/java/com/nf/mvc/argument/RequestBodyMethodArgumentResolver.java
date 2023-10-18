package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.nf.mvc.util.JacksonUtils.fromJson;

/**
 * 此解析器只解析参数上有注解@RequestBody修饰的参数，
 * 目前的实现只支持普通的POJO类以及List<POJO>这样的类型的解析
 * @see RequestBody
 */
public class RequestBodyMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isPresent(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        if (parameter.isList()) {
            //假定方法参数是List<Emp>的话，那么下面的actualTypeArgument变量指的就是Emp
            Class<?> actualTypeArgument = parameter.getFirstActualTypeArgument();
            //可以理解为实例化一个集合，比如new ArrayList<Emp>();
            return fromJson(request.getInputStream(), List.class,actualTypeArgument);
        } else {
            return fromJson(request.getInputStream(), parameter.getParameterType());
        }
    }
}
