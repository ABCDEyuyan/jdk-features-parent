package com.nf.mvc.argument;

import com.nf.mvc.SomeClass;
import com.nf.mvc.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodInvokerTest {
    private Map<String,String> requestMap ;

    @Before
    public void before(){
        requestMap = new HashMap<>();
        requestMap.put("id", "100");
        requestMap.put("name", "abc");
        requestMap.put("gender", "true");
        requestMap.put("salary", "5000.68");
        requestMap.put("deptId", "1");
        requestMap.put("deptName", "hr");
        requestMap.put("emp2.deptId", "100");
        requestMap.put("emp2.deptName", "rd");

    }

    @Test
    public void testMethodInvoker() throws Exception{
        MethodInvoker methodInvoker = new MethodInvoker();
        SomeController2 instance = ReflectionUtils.newInstance(SomeController2.class);
        Method method = instance.getClass().getDeclaredMethods()[0];
        Object invokeResult = methodInvoker.invoke(instance, method,"", requestMap);
        System.out.println("invokeResult = " + invokeResult);
    }
}
