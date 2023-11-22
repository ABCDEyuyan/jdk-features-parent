package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 这是一个解析参数类型为自定义的bean类型的参数解析器，此解析器只处理bean的属性（setter方法），
 * 如果属性的类型是非bean类型，那么交给其它的参数解析器去解析，如果仍然是bean类型，就进行递归解析处理<br/>
 * <h3>解析器链中的位置</h3>
 * <i>这个解析器要求是参数解析器链中的最后一个解析器。</i>
 * <h3>解析说明</h3>
 * <p>此解析器解析常见的pojo bean类型，也支持嵌套bean的解析，
 * 数据源的key是属性名，嵌套bean的属性名用句号分隔，类似于el表达式的写法</p>
 * 假定有下面这样一个控制器方法，其参数是POJO类Emp，那么request对象的map中如果有对应的key就可以把此Emp解析出来
 * <pre class="code">
 *     public class SomeController{
 *         public void insert(Emp emp){}
 *     }
 *
 *     &#064;Data
 *     public class Emp{
 *         private int id;
 *         private String name;
 *         private Dept dept;
 *     }
 *
 *     &#064;Data
 *     public class Dept{
 *         private int deptId;
 *         private String deptName;
 *         private Manager manager;
 *     }
 *
 *     &#064;Data
 *     public class Manager{
 *         private String title;
 *     }
 *
 *     private Map<String,String> requestMap = new HashMap<>() ;
 *     requestMap.put("id", "100");
 *     requestMap.put("name", "abc");
 *     requestMap.put("dept.deptId", "1111");
 *     requestMap.put("dept.deptName", "nested hr");
 *     requestMap.put("dept.manager.title", "cto");
 *
 * </pre>
 * </p>
 * <h3>实现逻辑</h3>
 * <ol>
 *     <li>其它解析器解析不了就认为此类是一个bean类型,创建此类的实例</li>
 *     <li>获取并遍历其所有setter方法并调用</li>
 *     <li>如果setter方法的参数其它解析器可以解析就交给其它解析器解析</li>
 *     <li>如果setter方法的参数其它解析器解析不了，就重复第一步，递归处理</li>
 * </ol>
 * <h3>自定义解析链</h3>
 * <p>此类利用了{@link MethodArgumentResolverComposite}类进行了自定义的解析器组合，
 * 先利用这个解析器组合进行解析，解析不了就交给本类解析，也就是进入到上面步骤的第一步：实例化bean类型</p>
 * <h3>线程安全性</h3>
 * <p>这里使用volatile+双重检查的形式实现了线程安全性，具体见{@link #getResolvers()}方法</p>
 *
 * @see MethodArgumentResolverComposite
 * @see MethodArgumentResolver
 */
public class BeanMethodArgumentResolver implements MethodArgumentResolver {
    private volatile MethodArgumentResolverComposite resolvers = null;

    @Override
    public boolean supports(MethodParameter parameter) {
        return !getResolvers().supports(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        // 进到这里来是因为supports为true，也就是mvc框架中其它参数解析器都无法解析参数了，
        // 当前mvc框架就认为此参数类型是一个POJO bean类型，所以就直接实例化此bean类型并填充此bean
        Stack<String> prefixStack = new Stack<>();
        Object bean = ReflectionUtils.newInstance(parameter.getParameterType());
        populateBean(bean, request, prefixStack);
        return bean;
    }

    /**
     * 这个方法会调用bean的所有setter方法，相当于给bean的所有属性进行赋值，
     * 这样就表示对一个bean进行了填充操作。
     * <p>填充的bean可能是控制器方法的参数，比如示例代码中Emp类型，
     * 也可能是控制器方法参数类型内的嵌套属性，比如Emp嵌套属性dept代表的这个Dept类型的bean</p>
     *
     * @param instance：要填充的bean实例
     * @param request:数据来源
     * @param prefixStack:用来存放属性名前缀的
     * @throws Exception 填充bean时可能抛出的异常
     */
    private void populateBean(Object instance, HttpServletRequest request, Stack<String> prefixStack) throws Exception {
        List<Method> allSetterMethods = ReflectionUtils.getAllSetterMethods(instance.getClass());

        for (Method setterMethod : allSetterMethods) {
            String parameterName = ReflectionUtils.getParameterNames(setterMethod).get(0);
            MethodParameter setterMethodParameter = new MethodParameter(setterMethod, 0, parameterName);
            // 没有参数解析器支持当前参数的解析，很可能此setter方法的参数仍然是一个bean类型，比如setDept(Dept dept)
            if (!getResolvers().supports(setterMethodParameter)) {
                // 前缀就是setter方法的名字，比如setDept方法，那么前缀就是dept
                String prefix = setterMethod.getName().substring(3, 4).toLowerCase() + setterMethod.getName().substring(4);
                prefixStack.push(prefix);
                invokeSetterMethod(instance, setterMethod, parameterName, request, prefixStack);
                prefixStack.pop();
            } else {
                invokeSetterMethod(instance, setterMethod, parameterName, request, prefixStack);
            }
        }
    }

    /**
     * 调用setter方法，相当于在填充bean实例，bean的所有setter方法都将得到调用，就表示bean实例填充完毕
     *
     * @param instance:setter方法所在类的实例
     * @param method:某一个setter方法,setter方法有且只有一个参数
     * @param request:数据来源
     * @param parameterName:当前setter方法的唯一的一个参数的名字
     * @param prefixStack:属性名前缀栈
     * @throws Exception 反射调用bean的setter方法时可能抛出的异常
     */
    private void invokeSetterMethod(Object instance, Method method, String
            parameterName, HttpServletRequest request, Stack<String> prefixStack) throws Exception {
        // 如果需要加前缀的话(prefixStack不为空)，就把参数名添加上前缀
        parameterName = handleParameterName(prefixStack, parameterName);
        Object[] paramValues = new Object[1];
        MethodParameter methodParameter = new MethodParameter(method, 0, parameterName);
        paramValues[0] = resolveSetterArgument(methodParameter, request, prefixStack);

        method.invoke(instance, paramValues);
    }

    /**
     * 处理属性的名字，如果有前缀，要把前缀也加上，比如dept.deptId
     * 然后就依据这个处理之后的属性名从request对象中提取属性名对应的值:req.getParameter(propertyName)
     *
     * @param prefixStack:               前缀栈
     * @param parameterName：setter方法的参数名
     * @return: 返回的是添加了前缀的参数名
     */
    private String handleParameterName(Stack<String> prefixStack, String parameterName) {
        if (prefixStack.isEmpty()) {
            return parameterName;
        }

        StringBuilder prefix = new StringBuilder();
        for (String s : prefixStack) {
            prefix.append(s).append(".");
        }

        parameterName = prefix + parameterName;
        return parameterName;
    }

    private Object resolveSetterArgument(MethodParameter parameter, HttpServletRequest request, Stack<String> prefixStack) throws Exception {
        if (getResolvers().supports(parameter)) {
            return getResolvers().resolveArgument(parameter, request);
        } else {
            Object bean = ReflectionUtils.newInstance(parameter.getParameterType());
            populateBean(bean, request, prefixStack);
            return bean;
        }
    }

    /**
     * 返回除了自己以外的其它所有参数解析器组成的解析器组合,也包含用户提供的定制解析器，
     * bean的所有setter方法的参数值都是靠这个解析器组合来进行解析的
     * <p>本来此方法可以写成下面的实现,那么就会导致每次调用此方法时都要遍历并过滤一下所有的解析器，
     * 而这个解析器组合就是所有解析器排除掉自身之后的所有其它解析器组成的，是固定不变的。
     * 每次遍历并过滤这样的操作显得没有必要，所以就声明一个全局的变量resolvers，此变量赋值之后就不再遍历。
     * <pre class="code">
     *      return new MethodArgumentResolverComposite().addResolvers(
     *            MvcContext.getMvcContext().getArgumentResolvers().stream()
     *                      .filter(r -> !(r instanceof BeanMethodArgumentResolver))
     *                      .collect(Collectors.toList()));
     * </pre>
     * </p>
     * <p>又由于参数解析器是单例的，并且其运行在多线程环境下，所以,为了确保resolvers变量只赋值一次
     * (也就是遍历并过滤操作只执行一次),就必须确保线程安全性。这里采用的是双重检查的方式来实现这一点的</p>
     *
     * @return 返回框架中除了自己以外的其它所有参数解析器
     */
    private MethodArgumentResolverComposite getResolvers() {
        if (resolvers == null) {
            synchronized (BeanMethodArgumentResolver.class) {
                if (resolvers == null) {
                    resolvers = new MethodArgumentResolverComposite().addResolvers(
                            MvcContext.getMvcContext().getArgumentResolvers().stream()
                                    .filter(r -> !(r instanceof BeanMethodArgumentResolver))
                                    .collect(Collectors.toList()));
                }
            }
        }
        return resolvers;
    }
}
