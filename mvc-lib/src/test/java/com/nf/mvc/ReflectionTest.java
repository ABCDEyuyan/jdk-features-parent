package com.nf.mvc;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReflectionTest {

    @Test
    public void testClassEquals(){
        Class<SomeClass> aClass = SomeClass.class;
        SomeClass instance = new SomeClass();
        //下面都返回true，在spring中是用==的比较方式
        System.out.println(aClass == instance.getClass());
        System.out.println(aClass.equals(instance.getClass()));
    }

    @Test(expected = NoSuchMethodException.class)
    public void testMethod() throws NoSuchMethodException {
        Class<SomeClass> aClass = SomeClass.class;
        //测试只给方法名，没有给正确的参数（少了一个List参数），
        // 这样是获取不到方法的，会抛NoSuchMethodException异常
        Method m2 = aClass.getDeclaredMethod("m2");
    }

    @Test
    public void testMethodReturnVoid() throws Exception{
        Class<SomeClass> aClass = SomeClass.class;
        Method m1 = aClass.getDeclaredMethod("m1", String.class, int.class);
        Class<?> m1ReturnType = m1.getReturnType();

        System.out.println(m1ReturnType == Void.class);//false
        System.out.println(m1ReturnType == Void.TYPE); //true
        System.out.println(m1ReturnType == void.class);//true

    }

    @Test
    public void testGenericParameter() throws Exception{
        Class<SomeClass> aClass = SomeClass.class;
        Method m2 = aClass.getDeclaredMethod("m2", List.class);

        Parameter parameter = m2.getParameters()[0];

        ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
        Class<?> genericType = (Class) parameterizedType.getActualTypeArguments()[0];
        //输出的java.lang.String ,也就是m2方法的参数类型List<String>的中类型实参
        System.out.println(genericType);

        System.out.println("============");
        Method m4 = aClass.getDeclaredMethod("m4", Map.class);

        Parameter m4Parameter = m4.getParameters()[0];

        ParameterizedType m4ParameterParameterizedType = (ParameterizedType) m4Parameter.getParameterizedType();
        Class<?> g1 = (Class) m4ParameterParameterizedType.getActualTypeArguments()[0];
        Class<?> g2 = (Class) m4ParameterParameterizedType.getActualTypeArguments()[1];

        //分别输出String与Integer
        System.out.println(g1);
        System.out.println(g2);
    }
}
