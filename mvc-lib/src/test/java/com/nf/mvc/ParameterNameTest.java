package com.nf.mvc;

import com.nf.mvc.util.ReflectionUtils;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterNameTest {

    /**
     * 默认情况下，方法的参数名，通过反射是获取不到的
     * 获取的名字是arg0,arg1,arg2....
     *
     * 如果想获取，需要在编译源代码的时候，加上-parameters这个编译选项
     * @throws Exception
     */
    @Test
    public void testGetParameterNameByReflection() throws Exception{
        Class<SomeClass> clz = SomeClass.class;
        Method m1 = clz.getDeclaredMethod("m1", String.class, int.class);
        Parameter[] parameters = m1.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            System.out.println(parameter.getName());
        }
    }

    /**
     * 一般想获取参数名，都是通过第三方的一些库来完成
     * 这里我们用的就是javassist（在业界有2个特别出名的字节码
     * 操作的库，分别叫cglib，javassist）
     */
    @Test
    public void testMethodParamName() {
        List<String> paramNames = ReflectionUtils.getParamNamesWithParamType( SomeClass.class,"m1",String.class,Integer.TYPE);
        for (String name : paramNames) {
            System.out.println(name);
        }
    }

    @Test
    public void testIntegerArray1() throws Exception{
        Class<SomeClass> clz = SomeClass.class;
        Method m1 = clz.getDeclaredMethod("m6", Integer[].class);
        List<String> paramNames = ReflectionUtils.getParamNames(clz, "m6");
        paramNames.forEach(System.out::println);
    }

    @Test
    public void testIntegerArray2() throws Exception{
        Class<SomeClass> clz = SomeClass.class;
        Method m1 = clz.getDeclaredMethod("m7", Integer[].class);
        List<String> paramNames = ReflectionUtils.getParamNames(clz, "m7");
        paramNames.forEach(System.out::println);
    }

}
