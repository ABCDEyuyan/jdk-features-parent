package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.support.PathMatcher;
import com.nf.mvc.support.WebTypeConverters;
import com.nf.mvc.support.path.AntPathMatcher;
import com.nf.mvc.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.nf.mvc.mapping.RequestMappingUtils.getUrlPattern;

/**
 * 路径变量参数解析器,基本只对简单类型数据做解析,因为数据来源是路径上的某一个片段的值
 * <p>方法参数的值可以是方法上的RequestMapping中设定的路径变量也可能是类上设置的路径变量,
 * 方法参数名是不重要的,只要PathVariable注解指定的值与路径模式中声明的变量名一样就可以了,
 * 当前的实现也不支持通过PathVariable注解指定默认值,因为提供默认值会对路径模式匹配逻辑产生影响.
 * 此解析器对方法参数上的注解RequestParam注解是不支持的,也就是说通过RequestParam指定方法参数名
 * 与默认值是无效的,示例使用代码如下
 * <pre class="code">
 *     &#064;RequestMapping("classes/{classId}")
 *     public class StudentController{
 *        &#064;RequestMapping("students/{page}")
 *        public List<Student> getPagedStudents
 *          (@PathVariable("classId")Integer classId,@PathVariable("page") Integer pageNo){
 *
 *        }
 *     }
 * </pre>
 * </p>
 * <p>此参数解析器要放置在{@link SimpleTypeMethodArgumentResolver}之前使用</p>
 */
public class PathVariableMethodArgumentResolver implements MethodArgumentResolver {
    private PathMatcher pathMatcher = PathMatcher.DEFAULT_PATH_MATCHER;

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isPresent(PathVariable.class) && parameter.isSimpleType();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        String patternInClass = getUrlPattern(parameter.getContainingClass());
        String patternInMethod = getUrlPattern(parameter.getMethod());
        String pattern = patternInClass + patternInMethod;

        String path = RequestUtils.getRequestUrl(request);

        Map<String, String> variables = pathMatcher.extractPathVariables(pattern, path);
        String varName = parameter.getParameter()
                .getDeclaredAnnotation(PathVariable.class)
                .value();

        String value = variables.get(varName);
        return WebTypeConverters.convert(value, parameter.getParameterType());
    }

    public void setPathMatcher(AntPathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }
}
