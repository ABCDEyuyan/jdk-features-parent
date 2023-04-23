package com.nf.mvc.argument;

import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MethodInvoker {
    int nestingLevel = 0;

    Stack<String> prefixStack = new Stack<>();
    public Object invoke(Object instance, Method method, boolean complex, Map<String, String> source) throws Exception {
        String stackContent = nestingLevel >1 && prefixStack.size()>0 ? prefixStack.peek():" ";
        System.out.println("===nestingLevel:" + nestingLevel + " method name:" + method.getName() + " stack: " + stackContent + " stack size: " + prefixStack.size());
        List<String> paramNames = ReflectionUtils.getParamNames(instance.getClass(), method.getName());
        String prefix = "";
        for (int i = 0; i < prefixStack.size(); i++) {
            prefix += prefixStack.get(i);
            prefix += ".";
        }
        System.out.println("prefix = " + prefix);

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
            paramValues[i] = resolveArgument(parameter, paramName, source);

        }
       // System.out.println("Arrays.toString(paramValues) = " + Arrays.toString(paramValues));

        return method.invoke(instance, paramValues);
    }



    private Object resolveArgument(Parameter parameter, String paramName, Map<String, String> source) throws Exception {

        Class<?> parameterType = parameter.getType();
        if (parameterType == Integer.TYPE) {
            return Integer.parseInt(source.get(paramName));
        }
        if (parameterType == String.class) {
            return source.get(paramName);
        }
        //控制器方法参数是复杂类型m1(Emp emp)
        if (ReflectionUtils.isComplexProperty(parameterType)) {
            nestingLevel++;
          //  System.out.println("+++ nestingLevel = " + nestingLevel + " paramName: " + paramName +" paramType：" + parameterType.getName());
            //直接实例化，并返回，剩下的就是遍历复杂属性的每一个setter方法，进行赋值处理，并处理前缀问题
            //第一层，也就是控制器方法的参数不作为前缀
            Object instance = ReflectionUtils.newInstance(parameterType);
            String prefix = "";
            //这里相当于获取控制器方法m1的参数Emp的所有setter方法
            List<Method> allSetterMethods = ReflectionUtils.getAllSetterMethods(parameterType);
            for (int i = 0; i < allSetterMethods.size(); i++) {
                //这里相当于Emp类的setDept方法、setId，setName等方法
                Method method = allSetterMethods.get(i);
                //比如Emp类的setDept方法
                if(ReflectionUtils.isComplexProperty(method.getParameters()[0].getType())){

                    prefix = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    prefixStack.push(prefix);
                    invoke(instance, method, true, source);
                    prefixStack.pop();

                }else{ //比如Emp的setId，setName方法
                    invoke(instance, method, false, source);
                }
            }

            nestingLevel--;

          //  System.out.println("--- nestingLevel = " + nestingLevel + " paramName: " + paramName +" paramType：" + parameterType.getName());
            return instance;
        }
        //省略其它n多类型判断的if
        return null;//表示不识别这种类型，无法解析
    }

    public Object invoke_backup(Object instance, Method method, String prefix, Map<String, String> source) throws Exception {
        List<String> paramNames = ReflectionUtils.getParamNames(instance.getClass(), method.getName());
        if (prefix.isEmpty() == false) {
            for (int i = 0; i < paramNames.size(); i++) {
                paramNames.set(i, prefix + "." + paramNames.get(i));
            }
        }
        int paramCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        Object[] paramValues = new Object[paramCount];
        for (int i = 0; i < paramCount; i++) {
            String paramName = paramNames.get(i);
            Parameter parameter = parameters[i];
            paramValues[i] = resolveArgument(parameter, paramName, source);

        }
        System.out.println("Arrays.toString(paramValues) = " + Arrays.toString(paramValues));

        return method.invoke(instance, paramValues);
    }

    private Object resolveArgument_backup(Parameter parameter, String paramName, Map<String, String> source) throws Exception {

        Class<?> parameterType = parameter.getType();
        if (parameterType == Integer.TYPE) {
            return Integer.parseInt(source.get(paramName));
        }
        if (parameterType == String.class) {
            return source.get(paramName);
        }
        if (ReflectionUtils.isComplexProperty(parameterType)) {
            Object instance = ReflectionUtils.newInstance(parameterType);
            List<Method> allSetterMethods = ReflectionUtils.getAllSetterMethods(parameterType);
            for (int i = 0; i < allSetterMethods.size(); i++) {
                Method method = allSetterMethods.get(i);
                String prefix = "";
                Parameter setterMethodParameter = method.getParameters()[0];
                if (ReflectionUtils.isComplexProperty(setterMethodParameter.getType())) {
                    prefix = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                    Object nestedBeanInstance = ReflectionUtils.newInstance(setterMethodParameter.getType());
                    List<Method> nestedBeanSetterMethods = ReflectionUtils.getAllSetterMethods(nestedBeanInstance.getClass());
                    for (int j = 0; j < nestedBeanSetterMethods.size(); j++) {
                        invoke_backup(nestedBeanInstance, nestedBeanSetterMethods.get(j), prefix, source);
                    }
                    method.invoke(instance,nestedBeanInstance);

                } else {

                    invoke_backup(instance, method, prefix, source);
                }

            }
            // nestingLevel++;
            return instance;
        }
        //省略其它n多类型判断的if
        return null;//表示不识别这种类型，无法解析
    }

}
