package com.nf.mvc.argument;

import com.nf.mvc.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class ArgumentTest {
    private Map<String, String> requestMap;

    /**
     * Before：之前的意思
     * 就是在每个单元测试方法执行之前先执行的方法
     */
    @Before
    public void before() {
        requestMap = new HashMap<>();
        requestMap.put("id", "100");
        requestMap.put("name", "abc");
        requestMap.put("gender", "true");
        requestMap.put("salary", "5000.68");
        requestMap.put("deptId", "1");
        requestMap.put("deptName", "hr");

    }

    @Test
    public void testPrimitive() throws Exception {
        Class<SomeController> controllerClass = SomeController.class;
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("method.getName() = " + method.getName());
            int paramCount = method.getParameterCount();
            List<String> paramNames = ReflectionUtils.getParameterNames(method);
            Parameter[] parameters = method.getParameters();

            Object[] paramValues = new Object[paramCount];
            for (int i = 0; i < paramCount; i++) {
                String paramName = paramNames.get(i);
                Parameter parameter = parameters[i];
                paramValues[i] = resolveArgument(parameter, paramName);

            }
            System.out.println("Arrays.toString(paramValues) = " + Arrays.toString(paramValues));

            method.invoke(controllerClass.newInstance(), paramValues);
        }
    }

    private Object resolveArgument(Parameter parameter, String paramName) {
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
//region 测试空值,注意：request对象取不存在的数据时返回的是null不是空字符串
    @Test(expected = NumberFormatException.class)
    public void testNull() throws Exception {
        String data = null;
        int i = Integer.valueOf(data);
        System.out.println("i = " + i);
    }

    @Test(expected = NumberFormatException.class)
    public void testEmpty() throws Exception {
        String data = " ";
        int i = Integer.valueOf(data);
        System.out.println("i = " + i);
    }
//endregion
}
