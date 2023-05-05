package com.nf.mvc.argument;

import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 这是一个通用的方法调用类型，2参数的invoke方法是调用静态方法用的
 * 3参数的invoke方法是调用实例方法用的
 * <p>
 * 方法的参数值是从Map这个souce里面获取的，如果参数类型是一个复杂的类型（通常是普通的POJO）的话
 * 会自动对其进行实例化，并对其属性进行数据的填充，给这些属性赋值时，从source中获取值的规则类似于el表达式写法
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
 *     MethodInvoker methodInvoker = new MethodInvoker();
 *     SomeController controller = ReflectionUtils.newInstance(SomeController.class);
 *     Method method = controller.getClass().getDeclaredMethods()[0];
 *     Object invokeResult = methodInvoker.invoke(controller, method, requestMap);
 *     System.out.println("invokeResult = " + invokeResult);
 * </pre>
 * </p>
 */
public class MethodInvokerOptimize {


    /** 这是处理静态方法调用的
     * @param method  代表一个静态方法
     * @param source  数据来源
     * @return
     * @throws Exception
     */
    public Object invoke(Method method, Map<String, ? extends Object> source) throws Exception {
        return invoke(null, method, source);
    }

    /**
     * 这是处理实例方法调用的
     * @param instance:实例方法所属的对象
     * @param method:代表一个实例方法
     * @param source:数据源，通常是web请求map
     * @return
     * @throws Exception
     */
    public Object invoke(Object instance, Method method, Map<String, ? extends Object> source) throws Exception {
        //处理属性前缀用的栈，比如dept.deptId和dept.deptName这2个属性的前缀就是dept，栈里面就会放dept
        Stack<String> prefixStack = new Stack<>();
        return doInvoke(instance, method, source, prefixStack);
    }

    /**
     * 如果能解析到方法参数的所有值，那么一个实例方法调用本来只需要method.invoke(instance,paramValues)一行代码即可 <br/>
     * 但方法参数可能也是一个复杂的类型，而这个嵌套的复杂类型可能也有很多的setter方法需要处理，所以会形成递归调用<br/>
     * 所以就需要改写方法参数值，以便在source中获取参数值。
     * <p>
     *     此方法基本处理逻辑如下
     *     <ul>
     *         <li>处理方法参数前缀问题</li>
     *         <li>获取方法所有参数</li>
     *         <li>依据参数数量创建一个一样数量的参数值数组</li>
     *         <li>循环遍历每一个参数，调用resolveArgument对单个参数值进行解析</li>
     *         <li>最后调用method的invoke对实例方法进行调用操作</li>
     *     </ul>
     * </p>
     * @param instance
     * @param method
     * @param source
     * @param prefixStack
     * @return
     * @throws Exception
     */
    private Object doInvoke(Object instance, Method method, Map<String, ? extends Object> source, Stack<String> prefixStack) throws Exception {
        List<String> paramNames = ReflectionUtils.getParameterNames(method);
        //处理参数的前缀，如果需要加前缀的话，就把参数名改掉，添加上前缀
        handleParamPrefix(prefixStack, paramNames);

        int paramCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        Object[] paramValues = new Object[paramCount];

        for (int i = 0; i < paramCount; i++) {
            String paramName = paramNames.get(i);
            Parameter parameter = parameters[i];
            paramValues[i] = resolveArgument(parameter, paramName, source, prefixStack);

        }
        return method.invoke(instance, paramValues);
    }

    private void handleParamPrefix(Stack<String> prefixStack, List<String> paramNames) {
        if (prefixStack.isEmpty()) return;

        String prefix = "";
        for (int i = 0; i < prefixStack.size(); i++) {
            prefix += prefixStack.get(i) + ".";
            // prefix += ".";
        }

        for (int i = 0; i < paramNames.size(); i++) {
            paramNames.set(i, prefix + paramNames.get(i));
        }

    }


    /**
     * 此方法用来对单个方法的参数进行值的解析操作
     *<p>
     * 解析的基本逻辑如下：
     *    <ul>
     *        <li>解析方法的参数，如果是简单类型，直接从source中取值，无前缀处理</li>
     *        <li>如果是复杂类型，直接对参数类型实例化，创建一个bean对象</li>
     *        <li>接着填充上一步实例化出现的bean</li>
     *        <li>直接return bean完成参数值的处理</li>
     *    </ul>
     *</p>
     * @param parameter
     * @param paramName
     * @param source
     * @param prefixStack
     * @return
     * @throws Exception
     */
    private Object resolveArgument(Parameter parameter, String paramName, Map<String, ? extends Object> source, Stack<String> prefixStack) throws Exception {

        Class<?> parameterType = parameter.getType();
        if (parameterType == Integer.TYPE) {
            return Integer.parseInt(source.get(paramName).toString());
        }
        if (parameterType == String.class) {
            return source.get(paramName);
        }
        if (ReflectionUtils.isComplexProperty(parameterType)) {
            //是复杂参数就直接实例化一个对象，接着填充此对象的属性，最后返回此实例
            Object bean = ReflectionUtils.newInstance(parameterType);
            populateBean(bean,source, prefixStack);
            return bean;
        }
        //省略其它n多类型判断的if
        return null;//表示不识别这种类型，无法解析
    }

    /**
     * 这个方法是用来对原始的method中复杂类型参数的属性进行值的填充操作的（比如示例代码中Emp类型）
     * <p>
     *     填充的基本逻辑是:
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
     * @param instance
     * @param source
     * @param prefixStack
     * @throws Exception
     */
    private void populateBean(Object instance,Map<String, ? extends Object> source, Stack<String> prefixStack) throws Exception {
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
                doInvoke(instance, setterMethod, source, prefixStack);
                prefixStack.pop();
            } else {
                doInvoke(instance, setterMethod, source, prefixStack);
            }
        }
    }

}
