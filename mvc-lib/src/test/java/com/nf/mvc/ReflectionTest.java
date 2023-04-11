package com.nf.mvc;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionTest {


    @Test
    public void testMethodParamName() {
        List<String> paramNames = ReflectionTest.getParamNames("m1", SomeClass.class);
        for (String name : paramNames) {
            System.out.println(name);
        }
    }

    private static List<String> getParamNames(String methodName, Class<?> clazz) {
        List<String> paramNames = new ArrayList<>();
        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass ctClass = pool.getCtClass(clazz.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
            // 使用javassist的反射方法的参数名
            javassist.bytecode.MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                int len = ctMethod.getParameterTypes().length;
                // 非静态的成员函数的第一个参数是this
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                for (int i = 0; i < len; i++) {
                    paramNames.add(attr.variableName(i + pos));
                }

            }
            return paramNames;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testClassEquals(){
        Class<SomeClass> aClass = SomeClass.class;
        SomeClass instance = new SomeClass();
        System.out.println(aClass == instance.getClass());
        System.out.println(aClass.equals(instance.getClass()));
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
}
