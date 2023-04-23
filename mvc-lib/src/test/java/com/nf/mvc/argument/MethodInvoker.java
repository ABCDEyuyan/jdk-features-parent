package com.nf.mvc.argument;

import com.nf.mvc.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MethodInvoker {
    public Object invoke(Object instance, Method method, Map<String,String> source) throws Exception{
        List<String> paramNames = ReflectionUtils.getParamNames(instance.getClass(), method.getName());
        int paramCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        Object[] paramValues = new Object[paramCount];
        for (int i = 0; i < paramCount; i++) {
            String paramName = paramNames.get(i);
            Parameter parameter = parameters[i];
            paramValues[i] = resolveArgument(parameter,paramName,source);

        }
        System.out.println("Arrays.toString(paramValues) = " + Arrays.toString(paramValues));

        return method.invoke(instance, paramValues);
    }

    private Object resolveArgument(Parameter parameter,String paramName,Map<String,String> source) {
        Class<?> parameterType = parameter.getType();
        if (parameterType == Integer.TYPE) {
            return Integer.parseInt(source.get(paramName));
        }
        if (parameterType == String.class) {
            return source.get(paramName);
        }

        //省略其它n多类型判断的if
        return null;//表示不识别这种类型，无法解析
    }


}
