package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.support.AntPathMatcher;
import com.nf.mvc.util.RequestUtils;
import com.nf.mvc.util.WebTypeConverterUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 路径变量参数解析器,基本只对简单类型数据做解析,因为数据来源是路径上的某一个片段的值
 * <p>此参数解析器最好放置在{@link SimpleTypeMethodArguementResolver}之前使用</p>
 */
public class PathVariableMethodArgumentResolver implements MethodArgumentResolver {
  AntPathMatcher pathMatcher = new AntPathMatcher.Builder().build();
  @Override
  public boolean supports(MethodParameter parameter) {
    return parameter.isPresent(PathVariable.class) && parameter.isSimpleType();
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
    String patternInClass = parameter.getContainingClass().getDeclaredAnnotation(RequestMapping.class).value();
    String patternInMethod = parameter.getMethod().getDeclaredAnnotation(RequestMapping.class).value();
    String pattern = patternInClass + patternInMethod;

    String path = RequestUtils.getRequestUrl(request);

    Map<String, String> variables = pathMatcher.extractPathVariables(pattern, path);
    String varName = parameter.getParameter().getDeclaredAnnotation(PathVariable.class).value();

    String value = variables.get(varName);
    return WebTypeConverterUtils.getTypeConverter(parameter.getParameterType()).convert(value);
  }
}
