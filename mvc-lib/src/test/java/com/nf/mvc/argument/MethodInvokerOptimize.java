package com.nf.mvc.argument;

import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
//没有优化版本在测试模块的MethodInvoker类里面，这个更容易理解，这里是优化版本
public class MethodInvokerOptimize {


    public Object invoke(Object instance, Method method, Map<String, String> source) throws Exception {
        Stack<String> prefixStack = new Stack<>();
        return doInvoke(instance, method, source,prefixStack);
    }

    private Object doInvoke(Object instance, Method method, Map<String, String> source,Stack<String> prefixStack) throws Exception {
        List<String> paramNames = ReflectionUtils.getParamNames(instance.getClass(), method.getName());
        String prefix = "";
        for (int i = 0; i < prefixStack.size(); i++) {
            prefix += prefixStack.get(i);
            prefix += ".";
        }

        if (prefix.length() > 0) {
            for (int i = 0; i < paramNames.size(); i++) {
                paramNames.set(i, prefix + paramNames.get(i));
            }
        }
        int paramCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        Object[] paramValues = new Object[paramCount];
        for (int i = 0; i < paramCount; i++) {
            String paramName = paramNames.get(i);
            Parameter parameter = parameters[i];
            paramValues[i] = resolveArgument(parameter, paramName, source,prefixStack);

        }
        return method.invoke(instance, paramValues);
    }


    private Object resolveArgument(Parameter parameter, String paramName, Map<String, String> source,Stack<String> prefixStack) throws Exception {

        Class<?> parameterType = parameter.getType();
        if (parameterType == Integer.TYPE) {
            return Integer.parseInt(source.get(paramName));
        }
        if (parameterType == String.class) {
            return source.get(paramName);
        }

        if (ReflectionUtils.isComplexProperty(parameterType)) {

            Object instance = ReflectionUtils.newInstance(parameterType);
            String prefix = "";

            List<Method> allSetterMethods = ReflectionUtils.getAllSetterMethods(parameterType);
            for (int i = 0; i < allSetterMethods.size(); i++) {
                //这里相当于Emp类的setDept方法、setId，setName等方法
                Method method = allSetterMethods.get(i);
                //比如Emp类的setDept方法
                if(ReflectionUtils.isComplexProperty(method.getParameters()[0].getType())){

                    prefix = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    prefixStack.push(prefix);
                    doInvoke(instance, method,  source,prefixStack);
                    prefixStack.pop();

                }else{
                    doInvoke(instance, method, source,prefixStack);
                }
            }
            return instance;
        }
        //省略其它n多类型判断的if
        return null;//表示不识别这种类型，无法解析
    }

}
