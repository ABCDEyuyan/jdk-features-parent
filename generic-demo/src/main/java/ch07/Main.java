package ch07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本章讲的是通配符的上下界问题
 */
public class Main {
    public static void main(String[] args) {
        List<Object> objectList = new ArrayList<>();
        objectList.add("212");
        objectList.add(2);

        List<Integer> integerList = Arrays.asList(1, 2, 3);
        List<String> stringList = Arrays.asList("a", "b", "c");

        //--- 得出结论：虽然Integer与String都是Object的子类
        //但是List<String>与List<Integer>不是List<Object>的子类
        //这个就表明泛型没有协变性
        //print(integerList);
        //print(stringList);

        //==========java里面数组就有协变性
        Number number = 100.5;
        number = 6;
        Integer integer = 5;
        Integer[] integers = {integer};
        Number[] numbers = integers;
        //System.out.println(numbers[0]);

        //=========泛型没有协变性====
        //基于下面的代码情况，所以它不设计泛型有协变性
        List<Integer> integerList1 = Arrays.asList(1, 2, 3);
        //如果有协变性，那么下面的赋值就不报错

        // List<Object> objectList= integerList1;

        //因为objectList变量是个List<Object>所以可以放置5.5，
        //但由于objectList指向的其实是一个整数列表，
        // 这样就会导致整数列表中有一个非整数
        // 这样就失去了泛型的初衷。

        // objectList.add(5.5);


        //创建通配符就提供了泛型类型的父类设计
        print2(integerList);
       // print2(stringList);
        System.out.println("=========");
        print3(integerList);
       // print3(stringList);
    }

    static void print(List<Object> list) {
        for (Object o : list) {
            System.out.print(o.toString() + " ");
        }
    }

    /**
     * ？:通配符
     *
     * @param list
     */
    static void print2(List<?> list) {
        
        for (Object o : list) {
            System.out.print(o.toString() + " ");
        }
        System.out.println();
    }

    static <T> void print3(List<T> list) {

        for (Object o : list) {
            System.out.print(o.toString() + " ");
        }
        System.out.println();
    }
}
