package com.nf.mvc;

import com.nf.mvc.argument.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

/**
 * 处理者的方法参数的解析器，web开发环境下，数据都是来自于HttpServletRequest对象，
 * 其典型的使用方式如下：
 * <pre class="code">
 *      if(resolver.supports(parameter){
 *          Object value = resolver.resolveArgument(parameter,request);
 *      }
 * </pre>
 * <p>只有在supports方法返回true的情况下才会调用resolveArgument进行参数解析</p>
 * @see MethodParameter
 */
public interface MethodArgumentResolver {

    /**
     * 用来判断此解析器是否支持对此参数的解析
     * @param parameter：封装了某一个方法的参数信息
     * @return true表示支持对此参数的解析
     */
    boolean supports(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception;
}
