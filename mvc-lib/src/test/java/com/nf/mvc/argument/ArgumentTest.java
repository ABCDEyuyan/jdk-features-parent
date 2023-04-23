package com.nf.mvc.argument;

import com.nf.mvc.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentTest {
    private Map<String,String> requestMap ;

    /**
     * Before：之前的意思
     * 就是在每个单元测试方法执行之前先执行的方法
     */
    @Before
    public void before(){
        requestMap = new HashMap<>();
        requestMap.put("id", "100");
        requestMap.put("name", "abc");
        requestMap.put("gender", "true");
        requestMap.put("salary", "5000.68");
        requestMap.put("deptId", "1");
        requestMap.put("deptName", "hr");

    }
    @Test
    public void testPrimitive() throws Exception{
        Class<SomeController> controllerClass = SomeController.class;
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("method.getName() = " + method.getName());
            int paramCount = method.getParameterCount();
            List<String> paramNames = ReflectionUtils.getParamNames(controllerClass, method.getName());
            Parameter[] parameters = method.getParameters();

            Object[] paramValues = new Object[paramCount];
            for (int i = 0; i < paramCount; i++) {
                String paramName = paramNames.get(i);
                Parameter parameter = parameters[i];
                paramValues[i] = resolveArgument(parameter,paramName);

            }
            System.out.println("Arrays.toString(paramValues) = " + Arrays.toString(paramValues));

            method.invoke(controllerClass.newInstance(), paramValues);
        }
    }

    private Object resolveArgument(Parameter parameter,String paramName) {
        Class<?> parameterType = parameter.getType();
        if (parameterType == Integer.TYPE) {
            return Integer.parseInt(requestMap.get(paramName));
        }
        if (parameterType == String.class) {
            return requestMap.get(paramName);
        }

        //省略其它n多类型判断的if
        return null;//表示不识别这种类型，无法解析
    }
}
