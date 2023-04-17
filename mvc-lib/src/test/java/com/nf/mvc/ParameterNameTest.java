package com.nf.mvc;

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
        List<String> paramNames = ParameterNameTest.getParamNames( SomeClass.class,"m1",String.class,Integer.TYPE);
        for (String name : paramNames) {
            System.out.println(name);
        }
    }


    /**
     *
     * @param clazz:方法所在的类
     * @param methodName：方法的名字
     * @param paramTypes：方法的参数类型，以便支持重载
     * @return 方法各个参数的名字（依据参数位置顺序依次返回）
     */
    private static List<String> getParamNames(Class<?> clazz,String methodName,Class... paramTypes) {
        List<String> paramNames = new ArrayList<>();
        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass ctClass = pool.getCtClass(clazz.getName());

            CtClass[] paramClasses = new CtClass[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                paramClasses[i] =pool.getCtClass(paramTypes[i].getName());
            }
            CtMethod ctMethod = ctClass.getDeclaredMethod(methodName,paramClasses);
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
}
