package ch03.methodref;

import java.util.Arrays;
import java.util.Comparator;

public class Main {
    //https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
    public static void main(String[] args) {
        //1.直接赋值一个静态方法给接口。
        InfMR infMR = SomeClassMR::doSth;

        //2.可以指向构造函数,构造函数调用后返回对象实例，
        // 所以接口的返回类型是此类的实例
        InfMR2 infMR2 = SomeClassMR::new;
        //SomeClassMR m = infMR2.m();//没有传递参数，表明调用的是无参的构造函数

        //构造函数的签名必须与接口方法签名一致，不是固定指向默认构造函数
        InfMR3 infMR3 = SomeClassMR::new;
        //有传递参数，表明调用的是有一个的构造函数
        SomeClassMR m2 = infMR3.m("asdf");

        // 3.引用特定实例的实例方法
        InfMr4Class instance = new InfMr4Class("a");
        InfMr4Class instance2 = new InfMr4Class("bbbbb");

        InfMR4 infMR4 = instance2::doSth;//一定不要加括号，不然就变成了方法调用
        //infMR4.m("abc");

        //4.引用特定类的任意实例的实例方法

        // 自己写lambda表达式，跟正常使用是一样的
        InfMr5 infMr5 = (ss, s) -> System.out.println(ss.toString() + s);
        //infMr5.doSth(new SomeClassMR(), "aaa");


        InfMr5 infMr51 = SomeClassMR::doSth2;
        SomeClassMR toInfmr5 = new SomeClassMR();
        SomeClassMR toInfmr52 = new SomeClassMR();
        System.out.println("hashcode:" + toInfmr52.hashCode());
        infMr51.doSth(toInfmr52, " df");

        //这个案例就是这种特定类（String），任意实例的实例方法关联
        Comparator<String> comparator = String::compareToIgnoreCase;
        String[] array = {"d", "A", "g", "e"};

        Arrays.sort(array, String::compareToIgnoreCase);
        for (String s : array) {
            System.out.println(s);
        }

    }


}
