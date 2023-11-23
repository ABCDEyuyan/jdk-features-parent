package com.nf.mvc;

import com.nf.mvc.argument.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求处理者方法的参数解析器，web开发环境下，数据基本都是来自于HttpServletRequest对象
 * 其典型的使用方式如下：
 * <pre class="code">
 *   for(MethodArgumentResolver resolver:resolvers){
 *     if(resolver.supports(parameter){
 *       Object value = resolver.resolveArgument(parameter,request);
 *     }
 *   }
 * </pre>
 * <p>只有在{@link #supports(MethodParameter)}方法返回true的情况下才会调用{@link #resolveArgument(MethodParameter, HttpServletRequest)}进行参数解析,
 * 如果参数类型没有一个解析器可以解析，那么就抛出异常，见{@link MethodArgumentResolverComposite#resolveArgument(MethodParameter, HttpServletRequest)}</p>
 * <h3>参数名</h3>
 * <p>解析通常是基于方法的参数名从请求中获取对应的字符串数据或者Part类型的数据，这个参数名是利用反射的方式获取,
 * 所以编译项目时需要添加-parameters编译选项,如果是maven项目，就需要给maven-compiler-plugin插件的配置项parameters设置为true.
 * 如果不想用这种方式获取参数名或者想指定别名，可以通过注解{@link RequestParam}的value属性来实现。
 * </p>
 * <h3>解析逻辑</h3>
 * <ul>
 *   <li>针对某个参数获取其对应的参数解析器,获取不到直接抛异常,提前结束请求流程,见{@link MethodArgumentResolverComposite#resolveArgument(MethodParameter, HttpServletRequest)}</li>
 *   <li>获取到某个合适解析就交给此解析器进行解析,所以解析器的顺序特别重要,解析器解析失败抛出异常即可,返回null不代表解析失败,因为有些参数就是允许赋值为null的</li>
 * </ul>
 * <h3>null值问题</h3>
 * <p>异常解析器链是只要某个解析器解析结果{@link HandlerExceptionResolver#resolveException(HttpServletRequest, HttpServletResponse, Object, Exception)}返回null,
 * 就表示本解析器解析不了,就交给链的下一个解析器去解析,但参数解析器链的解析逻辑不像异常解析器链,不能靠方法{@link #resolveArgument(MethodParameter, HttpServletRequest)}是否返回null
 * 来决定当前参数解析器是否可以解析参数，因为参数值为null可能是有实际业务意义的，所以参数解析器要靠supports方法来决定自己是否能解析此类型的参数</p>
 * <h3>单例与原型</h3>
 * <p>除了{@link MethodArgumentResolverComposite}解析器,其它所有的参数解析器都加入到了Mvc框架参数解析器链里，
 * 链中的解析器都是单例的.但{@link MethodArgumentResolverComposite}不要求是单例的,而且它通常是原型的</p>
 * <h3>顺序问题</h3>
 * <p>链中的参数解析器不允许存在实例化时有顺序依赖的问题,不能出现要实例化B参数解析器时必须先实例化A参数解析器的情况,
 * 只有{@link MethodArgumentResolverComposite}这个链外的解析器依赖其它参数解析器</p>
 *
 * @see MethodParameter
 * @see MethodArgumentResolverComposite
 * @see com.nf.mvc.argument.RequestParam
 * @see RequestBodyMethodArgumentResolver
 * @see PathVariableMethodArgumentResolver
 * @see com.nf.mvc.argument.MultipartFileMethodArgumentResolver
 * @see SimpleTypeMethodArgumentResolver
 * @see BeanMethodArgumentResolver
 */
public interface MethodArgumentResolver {

    /**
     * 用来判断此解析器是否支持对此参数的解析
     *
     * @param parameter：封装了某一个方法的参数信息
     * @return true表示支持对此参数的解析
     */
    boolean supports(MethodParameter parameter);

    /**
     * 通常是基于方法参数的类型与名称从请求传递过来的字符串解析出兼容类型的值
     *
     * @param parameter 方法参数
     * @param request   servlet请求对象
     * @return 返回兼容类型的值，也可能返回null值，null并不表示此解析器解析不了，这点与HandlerMapping是不一样的,<br/>
     * 请求端没有传递过来数据就有可能解析结果为null
     * @throws Exception 通常是请求端有数据，但无法解析成兼容类型的参数值时就抛出异常，建议抛一个RuntimeException
     */
    Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception;
}
