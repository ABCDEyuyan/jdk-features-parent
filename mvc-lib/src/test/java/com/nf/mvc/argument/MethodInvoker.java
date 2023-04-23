package com.nf.mvc.argument;

import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MethodInvoker {
    public Object invoke(Object instance, Method method, String prefix, Map<String, String> source) throws Exception {
        List<String> paramNames = ReflectionUtils.getParamNames(instance.getClass(), method.getName());
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

    int nestingLevel = 0;
    String prefix = "";

    private Object resolveArgument(Parameter parameter, String paramName, Map<String, String> source) throws Exception {

        Class<?> parameterType = parameter.getType();
        if (parameterType == Integer.TYPE) {
            return Integer.parseInt(source.get(paramName));
        }
        if (parameterType == String.class) {
            return source.get(paramName);
        }
        if (parameterType == Emp2.class) {
            Emp2 empInstance = (Emp2) ReflectionUtils.newInstance(parameterType);
            List<Method> allSetterMethods = ReflectionUtils.getAllSetterMethods(parameterType);
            for (int i = 0; i < allSetterMethods.size(); i++) {
                Method method = allSetterMethods.get(i);

                if (nestingLevel > 0) {
                    prefix += parameterType.getSimpleName().substring(0, 1).toLowerCase()
                            + parameterType.getSimpleName().substring(1);
                }
                invoke(empInstance, method, prefix, source);
                nestingLevel++;
            }


        }
        //省略其它n多类型判断的if
        return null;//表示不识别这种类型，无法解析
    }


}
