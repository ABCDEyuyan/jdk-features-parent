package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Stack;

/**
 * 这是一个复杂类型的参数解析器，所谓的复杂类型就是通常我们编写的pojo类，<br/>
 * 此解析器支持嵌套bean的解析，数据源的key是属性名，用句号分隔，类似于el表达式的写法
 * <p>
 *     此复杂类型的解析逻辑是遍历其所有的setter方法，如果setter方法的参数是其它解析器可以解析的，就交给其它解析器解析，
 *     如果其它解析器不支持，比如仍然是一个复杂的pojo类型，就又交给本解析器进行递归处理。<br/>
 *     所以，本解析器本质上只负责复杂类型的实例化,并利用其它解析器解析内部的简单类型的属性以填充此复杂类型的实例
 * </p>
 * <p>
 * 假定有下面这样一个控制，其参数是POJO类Emp，那么request对象的map中如果有对应的key就可以把此Emp解析出来
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
 *      &#064;Data
 *     public class Dept{
 *         private int deptId;
 *         private String deptName;
 *         private Manager manager;
 *     }
 *
 *      &#064;Data
 *     public class Manager{
 *         private String title;
 *     }
 *
 *     private Map<String,String> requestMap = new HashMap<>() ;
 *     requestMap.put("id", "100");
 *     requestMap.put("name", "abc");
 *     requestMap.put("dept.deptId", "1111");
 *     requestMap.put("dept.deptName", "nested hr");
 *     requestMap.put("dept.manager.title", "jingli");
 *     requestMap.put("dept.manager.youxi.gameName", "starcraft");
 *
 * </pre>
 * </p>
 */
public class ComplexTypeMethodArgumentResolver implements MethodArgumentResolver {
    /* 这里添加volatile是为了防止指令重排序与内存可见性的目的添加的，防止new了对象之后还没有初始化完毕就返回，
      不过jdk4之后这个问题已经修复，可以不用加这个关键字了 */
    private volatile HandlerMethodArgumentResolverComposite resolvers = null;
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return ReflectionUtils.isComplexProperty(paramType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        /*
        //BeanUtils来自于Apache的commons-beanutils工具包
        //如果你不想用BeanUtils实现，你可以用之前学过的Introspector
        Class<?> paramType = parameter.getParamType();
        Object instance = ReflectionUtils.newInstance(paramType);
        BeanUtils.populate(instance,request.getParameterMap());
        return instance;
        */

        Stack<String> prefixStack = new Stack<>();
        Object bean = ReflectionUtils.newInstance(parameter.getParameterType());
        populateBean(bean, request, prefixStack);
        return bean;
    }

    /**
     * 这个方法是用来对原始的method中复杂类型参数的属性进行值的填充操作的（比如示例代码中Emp类型）
     * <p>
     * 填充的基本逻辑是:
     *     <ul>
     *         <li>获取复杂类型参数其所有的setter方法（也就是示例代码中Emp的几个setter方法）</li>
     *         <li>如果某个setter方法的参数是复杂类型，要把当前这个setter方法对应的属性名入栈，比如setDept，那么就是dept入栈</li>
     *         <li>接着递归调用doInvoke，对当前这个setter方法进行调用，也相当于完成此方法参数值的处理,也就是示例代码的Dept类型参数的处理
     *          <ul>
     *              <li> 而此时由于栈里面有内容dept，所以doInvoke对参数前缀处理后给每个参数加上前缀，也就是setDept方法参数dept再加上一个dept</li>
     *              <li>此时进入到Dept对象的populate过程，就会获取Dept的所有setter方法，也就是setDeptId，setDeptName，setManager</li>
     *              <li>由于deptId与deptName属性是简单属性，继续递归调用，但栈中还是原来的dept内容</li>
     *              <li>处理setDeptId方法时获取其参数名是deptId，再加上前缀dept就变成了dept.deptId</li>
     *              <li>这样直接就走简单类型的流程，从source中可以获取值了</li>
     *          </ul>
     *         </li>
     *         <li>复杂类型的递归doInvoke调用完毕后，要进行出栈处理</li>
     *         <li>
     *         </li>
     *     </ul>
     * </p>
     *
     * @param instance
     * @param request
     * @param prefixStack
     * @throws Exception
     */
    private void populateBean(Object instance, HttpServletRequest request, Stack<String> prefixStack) throws Exception {
        List<Method> allSetterMethods = ReflectionUtils.getAllSetterMethods(instance.getClass());

        for (int i = 0; i < allSetterMethods.size(); i++) {
            Method setterMethod = allSetterMethods.get(i);
            Parameter setterMethodParameter = setterMethod.getParameters()[0];
            Class<?> parameterType = setterMethodParameter.getType();

            if (ReflectionUtils.isComplexProperty(parameterType)) {
                String prefix = setterMethod.getName().substring(3, 4).toLowerCase() + setterMethod.getName().substring(4);
                //这里改成方法参数名更统一，但反射获取不到方法名，要改当前populateBean方法签名，后续用MethodParameter封装Parameter解析参数名问题
                // String prefix = setterMethodParameter.getName();
                prefixStack.push(prefix);
                doInvoke(instance, setterMethod, request, prefixStack);
                prefixStack.pop();
            } else {
                doInvoke(instance, setterMethod, request, prefixStack);
            }
        }
    }

    /**
     * 如果能解析到方法参数的所有值，那么一个实例方法调用本来只需要method.invoke(instance,paramValues)一行代码即可 <br/>
     * 但方法参数可能也是一个复杂的类型，而这个嵌套的复杂类型可能也有很多的setter方法需要处理，所以会形成递归调用<br/>
     * 所以就需要改写方法参数值，以便在source中获取参数值。
     * <p>
     * 此方法基本处理逻辑如下
     *     <ul>
     *         <li>处理方法参数前缀问题</li>
     *         <li>获取方法所有参数</li>
     *         <li>依据参数数量创建一个一样数量的参数值数组</li>
     *         <li>循环遍历每一个参数，调用resolveArgument对单个参数值进行解析</li>
     *         <li>最后调用method的invoke对实例方法进行调用操作</li>
     *     </ul>
     * </p>
     *
     * @param instance
     * @param method
     * @param request
     * @param prefixStack
     * @return
     * @throws Exception
     */
    private Object doInvoke(Object instance, Method method, HttpServletRequest request, Stack<String> prefixStack) throws Exception {
        List<String> paramNames = ReflectionUtils.getParameterNames(method);
        //处理参数的前缀，如果需要加前缀的话，就把参数名改掉，添加上前缀
        handleParamPrefix(prefixStack, paramNames);

        int paramCount = method.getParameterCount();
        Object[] paramValues = new Object[paramCount];

        for (int i = 0; i < paramCount; i++) {
            String paramName = paramNames.get(i);
            MethodParameter methodParameter = new MethodParameter(method, i, paramName);
            paramValues[i] = resolveArgument(methodParameter, request, prefixStack);

        }
        return method.invoke(instance, paramValues);
    }

    private void handleParamPrefix(Stack<String> prefixStack, List<String> paramNames) {
        if (prefixStack.isEmpty()) return;

        String prefix = "";
        for (int i = 0; i < prefixStack.size(); i++) {
            prefix += prefixStack.get(i) + ".";
        }

        for (int i = 0; i < paramNames.size(); i++) {
            paramNames.set(i, prefix + paramNames.get(i));
        }

    }

    /**
     * 此方法用来对单个方法的参数进行值的解析操作
     * <p>
     * 解析的基本逻辑如下：
     *    <ul>
     *        <li>解析方法的参数，如果是简单类型，直接从source中取值，无前缀处理</li>
     *        <li>如果是复杂类型，直接对参数类型实例化，创建一个bean对象</li>
     *        <li>接着填充上一步实例化出现的bean</li>
     *        <li>直接return bean完成参数值的处理</li>
     *    </ul>
     * </p>
     * @param parameter
     * @param request
     * @param prefixStack
     * @return
     * @throws Exception
     */
    private Object resolveArgument(MethodParameter parameter, HttpServletRequest request, Stack<String> prefixStack) throws Exception {
        if (getResolvers().supports(parameter)) {
            return getResolvers().resolveArgument(parameter, request);
        } else if (ReflectionUtils.isComplexProperty(parameter.getParameterType())) {
            Object bean = ReflectionUtils.newInstance(parameter.getParameterType());
            populateBean(bean, request, prefixStack);
            return bean;
        }
        return null;

    }

    /**
     * 此复杂类型的解析器利用其它的解析器来进行数据解析，所以要排除掉自己
     *
     * 参数解析器是单例的，但其运行在多线程环境下，下面的代码采用的是双重检查的方式确保resolvers只会被求值一次以确保线程安全性
     * @return
     */
    private  HandlerMethodArgumentResolverComposite getResolvers() {
        if (resolvers == null) {
            synchronized (ComplexTypeMethodArgumentResolver.class) {
                List<MethodArgumentResolver> argumentResolvers = MvcContext.getMvcContext().getArgumentResolvers();
                HandlerMethodArgumentResolverComposite result = new HandlerMethodArgumentResolverComposite();
                for (MethodArgumentResolver argumentResolver : argumentResolvers) {
                    if (!(argumentResolver instanceof ComplexTypeMethodArgumentResolver)) {
                        result.addResolver(argumentResolver);
                    }
                }
                resolvers = result;
            }
        }

        return resolvers;
    }
}
