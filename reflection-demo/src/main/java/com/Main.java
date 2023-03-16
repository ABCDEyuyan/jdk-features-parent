package com;

import java.lang.reflect.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        //inspector();
       // inspectMethod();
        //instanceClass();
        //fieldHandle();
        methodHandle();

        //作业1：静态字段的赋值，取值，静态方法的调用怎么弄？
    }

    private static void inspector() {
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


        /*Parent parent = new Parent();
        Class<?> clz = parent.getClass();
        Constructor[] methods = clz.getDeclaredConstructors();

        for (Constructor c : methods) {
            System.out.println(c.getName());

            System.out.println(c.getParameterCount());
        }*/

        //作业1：同一个类的Class对象是相等的吗？
        //作业2：静态方法怎么获取？
        //作业3：方法的参数怎么获取
    }

    private static void inspectMethod() throws NoSuchMethodException {
        Class<Parent> clz = Parent.class;

        Method m1 = clz.getDeclaredMethod("m1",String.class);
        System.out.println("m1.getName() = " + m1.getName());
        System.out.println("Modifier.isPublic(m1.getModifiers()) = " + Modifier.isPublic(m1.getModifiers()));

        System.out.println("m1.getReturnType() = " + m1.getReturnType());
        System.out.println("m1.getParameterCount() = " + m1.getParameterCount());

        Parameter[] parameters = m1.getParameters();
        for (Parameter parameter : parameters) {
            //java的反射api功能比较弱，所以获取不到方法的形参的名字
            //反射获取的名字默认就是arg0，arg1，以此类推

            //但其实class文件中是有记录到方法的形参名的，
            // 所以是有可能获取到方法的形参名的

            //一个简单的获取参数名的方法是编译是提供一个选项，然后就可以通过这个反射api获取到            System.out.println("parameter.getName() = " + parameter.getName());
            System.out.println("parameter.getType() = " + parameter.getType());

        }

    }

    /**
     * 有了Class对象之后，我们是可以直接实例化对象（反射方式）
     * 有两种情况，
     * 第一种：有默认构造函数时，建议直接用Class对象的newInstance方法
     * 第二种：指定某个构造函数实例化，一般用在没有默认构造函数的场景下
     * 利用的是Constructor的newInstance方法实例化
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private static void instanceClass() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<Parent> clz = Parent.class;

        //newInstance默认是会调用默认构造函数（无参）
//        Parent instance = clz.newInstance();
//        System.out.println("instance = " + instance);
//

        Constructor<Parent> c = clz.getDeclaredConstructor(String.class);

        Parent instance = c.newInstance("asdf");
        System.out.println("instance = " + instance);


    }


    private static void fieldHandle() throws Exception {
        Class<Parent> clz = Parent.class;
        Field idField = clz.getDeclaredField("id");
        //字段的赋值，因为字段是实例字段，意思就是对象级别
        //所以先要创建出Parent类的对象出来，然后才能设置值
        Parent instance = clz.newInstance();
        Parent instance2 = clz.newInstance();
        //因为字段是私有的，不能直接赋值，所以需要改权限
        //accessible:可访问的
        idField.setAccessible(true);
        //第一个参数表明是给哪个对象的id字段赋值，
        // 第二个参数是字段实际赋的值
        //这种方式是直接操作一个私有的字段，违法了封装的原则
        //下面的代码是设置字段的值，类似于p.id=333
        idField.set(instance,333);

        //下面的代码是获取字段的值，类似p.id
        System.out.println("idField.get(instance) = " + idField.get(instance));
        //
        idField.setAccessible(false);
        System.out.println("instance.getId() = " + instance.getId());
        System.out.println("instance2.getId() = " + instance2.getId());

    }

    private static void methodHandle() throws Exception {
        Class<Parent> clz = Parent.class;

        Parent instance = clz.newInstance();
        Method m1Method = clz.getDeclaredMethod("m1");
        //p.m1();p.m1("asdf",100)
        m1Method.invoke(instance);
    }


    /**
     * Map<String,Object> map = new HashMap();..
     * map.put("id",100);
     * map.put("username，“abc");
     *
     * 写了一个UserInfo的类，里面刚好有id与username属性
     *
     * UserInfo ui = mapToBean(map,UserInfo.class)
     * ui.getId = 100
     * ui.getUsername="abc
     * @param source
     * @param clz
     * @return
     */


}
