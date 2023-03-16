package com;

import java.lang.reflect.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        //inspector();
       inspectMethod();
        //instanceClass();
        //fieldHandle();
       // methodHandle();

        //作业1：静态字段的赋值，取值，静态方法的调用怎么弄？
    }

    //反射（Reflection）就是运行时解析类本身，调用方法，设置属性，实例化
    private static void inspector() {
        //1.反射的基础都是从Class对象来

        //获取class对象有3种方法
        // 下面就是第一种，通过任何类的静态字段class来得到class对象
    /*    Class<?> clz = Parent.class;

        System.out.println(clz.getName());
        System.out.println(clz.getSimpleName());

*/
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
        Parent parent = new Parent();
        Class<?> clz = parent.getClass();
        //方法要么是类或者接口自己写的，要么是继承过来
        //DeclaredMethods获取的就是自己编写的，不管它的修饰符是什么（4个）
        Method[] methods = clz.getDeclaredMethods();
//  getMethods:包含自己声明的以及继承过来的，但只包括public的方法
       // clz.getMethods()

        for (Method method : methods) {
            System.out.println(method.getName());
        }


     /* Parent parent = new Parent();
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

        //因为有重载的原因（方法名一样，参数的类型或者个数不一样）
        //所以反射时光指定名字是不能准确的获取某个方法的
        //还需要指定参数，下面的写法表名获取的是名字为m1,有一个参数，并且类型是String的方法
        Method m1 = clz.getDeclaredMethod("m1",String.class);
        Method m11 = clz.getDeclaredMethod("m1",String.class);

        System.out.println("m1 == m11：" + (m1 == m11));

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

        // 实例化一个对象的时候，一定是会调用某个构造函数的
        //newInstance默认是会调用默认构造函数（无参）
        //下面这一行代码是反射的方式创建对象
        //效果就等价于Parent p = new Parent();
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
        //别忘记关闭安全性，因为反射是直接操作字段本身，而且跳过了setter方法
        idField.setAccessible(false);
        System.out.println("instance.getId() = " + instance.getId());
        System.out.println("instance2.getId() = " + instance2.getId());

        Field urlField = clz.getDeclaredField("url");
        urlField.setAccessible(true);

        urlField.set(null,"fanshe");
        //获取字段的值用get方法，如果是实例字段，get的第一个参数就是一个对象
        System.out.println("urlField.get(null) = " + urlField.get(null));
    }

    private static void methodHandle() throws Exception {
        Class<Parent> clz = Parent.class;

        Parent instance = clz.newInstance();
        Method m1Method = clz.getDeclaredMethod("m1");
        //p.m1();p.m1("asdf",100)
        //如果被调用的方法有返回值，就可以像下面这样接收
        //如果被调用的方法没有返回值，result就是null
        Object result = m1Method.invoke(instance);
        System.out.println("result = " + result);
        //下面是静态的方法的调用，静态方法的调用的第一个参数为null就可以
        Method sm1 = clz.getDeclaredMethod("sm1");
        sm1.invoke(null);

        Method sm2 = clz.getDeclaredMethod("sm2",String.class);
        sm2.invoke(null,"abc");
    }



}
