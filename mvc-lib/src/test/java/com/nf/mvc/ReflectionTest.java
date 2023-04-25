package com.nf.mvc;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.*;

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
    public void testSimpleArray() throws Exception{
        Object instance = Array.newInstance(Integer.TYPE, 2);
        Array.set(instance,0,100);
        Array.set(instance,1,200);
        //不能这样判断
        boolean result = instance instanceof Array;
        System.out.println("result = " + result); //false

        System.out.println(Array.get(instance, 0));
        System.out.println(Array.get(instance, 1));
    }

    @Test
    public void testArrayParam() throws Exception{

        Map<String, String[]> data = new HashMap<>();
        data.put("names", new String[]{"100", "200"});

        Method m5 = SomeClass.class.getDeclaredMethod("m5", int[].class);
        Parameter parameter = m5.getParameters()[0];
        boolean isArray = parameter.getType().isArray();

        System.out.println("isArray = " + isArray);
        System.out.println("parameter.getType() = " + parameter.getType());
        //不能用下面的方式给数组实例化
        //parameter.getType().newInstance();

        Class<?> componentType = parameter.getType().getComponentType();
        System.out.println("componentType = " + componentType);
        //这里instance的实际类型是一个整数数组，声明的类型是Object
        Object instance = Array.newInstance(componentType, 2);
        //data.get得到的是一个字符串数组，赋值给一个声明为Object类型数据是可以的，不报错，
        // 但此时instance就代表着一个字符串数组类型了，这不是我们的期望，我们期望的是一个整数类型
        instance = data.get("names");
        //Arrays.toString(instance);
        //所以在直接调用m5方法时就报错了。报Argument type mismatch（参数类型不匹配）
        SomeClass someClass = SomeClass.class.newInstance();
        m5.invoke(someClass, instance);
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

    @Test
    public void testTypedParameter() throws Exception{
        Field field;

        List<String> list = new ArrayList<>();
        ParameterizedType parameterizedType = (ParameterizedType) list.getClass().getGenericSuperclass();

        Class<?> genericType = (Class) parameterizedType.getActualTypeArguments()[0];

        System.out.println(genericType);
    }
}
