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
 * 支持嵌套bean的解析，名字类似于el表达式的方式
 * @code
 */
public class ComplexTypeMethodArgumentResolver implements MethodArgumentResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParamType();
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
        Object bean = ReflectionUtils.newInstance(parameter.getParamType());
        populateBean(bean, request, prefixStack);
        return bean;
    }

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
     * 此复杂类型的解析器利用其它的解析器来进行数据解析，所以要排除掉自己
     *
     * @param parameter
     * @param request
     * @param prefixStack
     * @return
     * @throws Exception
     */
    private Object resolveArgument(MethodParameter parameter, HttpServletRequest request, Stack<String> prefixStack) throws Exception {
        if (getResolvers().supports(parameter)) {
            return getResolvers().resolveArgument(parameter, request);
        } else if (ReflectionUtils.isComplexProperty(parameter.getParamType())) {
            Object bean = ReflectionUtils.newInstance(parameter.getParamType());
            populateBean(bean, request, prefixStack);
            return bean;
        }
        return null;

    }

    private HandlerMethodArgumentResolverComposite getResolvers() {
        List<MethodArgumentResolver> argumentResolvers = MvcContext.getMvcContext().getArgumentResolvers();
        HandlerMethodArgumentResolverComposite result = new HandlerMethodArgumentResolverComposite();
        for (MethodArgumentResolver argumentResolver : argumentResolvers) {
            if (!(argumentResolver instanceof ComplexTypeMethodArgumentResolver)) {
                result.addResolver(argumentResolver);
            }
        }
        return result;
    }
}
