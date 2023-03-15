package com;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {
        //1.反射的基础都是从Class对象来

        //获取class对象有3种方法
        // 下面就是第一种，通过任何类的静态字段class来得到class对象
       /* Class<?> clz = Parent.class;

        System.out.println(clz.getName());
        System.out.println(clz.getSimpleName());*/

        //方法二：forName方法本质是加载类的作用，

      /*  Class<?> clz = Class.forName("com.Parent");

        System.out.println(clz.getName());
        System.out.println(clz.getSimpleName());*/

      //方法三：在已经有类的对象情况下，
        // 通过对象的getClass方法获取Class对象
/*        Parent parent = new Parent();
        Class<?> clz = parent.getClass();
        System.out.println(clz.getName());
        System.out.println(clz.getSimpleName());*/

        //获取类中的所有方法

        //Declare：声明的意思 ，获取自己声明的所有方法
       /* Parent parent = new Parent();
        Class<?> clz = parent.getClass();
        Method[] methods = clz.getDeclaredMethods();

        for (Method method : methods) {
            System.out.println(method.getName());
        }*/


     /*   Parent parent = new Parent();
        Class<?> clz = parent.getClass();
        Field[] methods = clz.getDeclaredFields();

        for (Field field : methods) {
            System.out.println(field.getName());
        }*/


        Parent parent = new Parent();
        Class<?> clz = parent.getClass();
        Constructor[] methods = clz.getDeclaredConstructors();

        for (Constructor c : methods) {
            System.out.println(c.getName());

            System.out.println(c.getParameterCount());
        }

        //作业1：同一个类的Class对象是相等的吗？
        //作业2：静态方法怎么获取？
        //作业3：方法的参数怎么获取
    }
}
