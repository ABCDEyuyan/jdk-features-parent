package ch09;

import java.util.List;
import java.util.Set;

/**
 * java泛型的使用限制
 * 主要的限制有：
 * <ul>
 *     <li>泛型实参不能是基本类型 {@link #primitive()}</li>
 *     <li>泛型实参不能实例化 {@link SomeClass#m()}</li>
 *     <li>不能声明类型为类型参数的静态变量以及静态方法不能使用类上的类型变量 {@link SomeClass}</li>
 *     <li>不能对参数化的泛型类使用instanceof或者进行类型转换{@link #constraint4()}</li>
 *     <li>不能创建泛型化的类型数组{@link #constraint5()} ()}</li>
 *     <li>不能创建泛型类继承于异常类{@link #genericEx} ()}</li>
 *     <li>方法参数类型不能是参数化的泛型类，这样不形成重载{@link SomeClass#print(Set)} ()}</li>
 * </ul>
 * https://docs.oracle.com/javase/tutorial/java/generics/restrictions.html
 */
public class Main {
    public static void main(String[] args) {
        // primitive();
        //  constraint4();
        Object[] strings = new String[2];
        strings[0] = "hi";   // OK，strings最终指向的是字符串数组，放字符串可以的
        //字符串数组是不能放非字符串数据,下面这行代码会报ArrayStoreException
        strings[1] = 100;
    }

    private static void constraint5() {
        //约束5：不能创建泛型化的类型数组
        //因为数组有协变行，所以字符串数组可以赋值给Object数组
        Object[] strings = new String[2];
        strings[0] = "hi";   // OK，strings最终指向的是字符串数组，放字符串可以的
        //字符串数组是不能放非字符串数据
        strings[1] = 100;    // An ArrayStoreException is thrown.

         //Object[] stringLists = new ArrayList<String>[2];  // compiler error, but pretend(假装) it's allowed
        //因为泛型擦除之后，没有ArrayList<String>与ArrayList<Integer>的区别
        //所以放的2个数据都是ArrayList
        //stringLists[0] = new ArrayList<String>();   // OK
        //stringLists[1] = new ArrayList<Integer>();  // An ArrayStoreException should be thrown,
        // but the runtime can't detect it.

        /* 简单来说就是由于泛型擦除，ArrayList<String>[] 与ArrayList<Integer>[]本质上都是ArrayList[],
        无法区分两者的不同，你以为声明了两个不同类型的数组，其实都是一样的，没有意义，所以干脆不允许*/
    }

    private static void constraint4() {
        //约束4：不能对参数化得泛型类使用instanceof或者进行类型转换
        List<String> list = null;
        List<Integer> list2 = null;
        //if(list instanceof ArrayList<String>)//不行，因为类型擦除
        //else if(list instanceof ArrayList<Integer>)
        //通配符是可以的
        if (list instanceof List<?>) {

        }
    }

    //约束1：不能用基本类型
    private static void primitive() {
        // 基本类型并没有继承Object，其包装类型才继承自Object
        //因为泛型是一个编译器层面的技术---》擦除
        //--->add(T e)--->add(e)(X)-->add(Object e)
        // 基本类型不能转换为Object o = 5;  //自动的装箱+ 向上转型
        // List<int> list;//
    }

    private static void genericEx() {
        //约束6：不能创建泛型类继承于异常类
        //假定能继承。
        // try{

        //擦除之后catch的都是同一个类型
        //不符合异常catch的语法。父子
        //}catch(MyEx<String> ex1){

        //}catch(MyEx<Integer> ex2){

        //      }
/*
class MyEx<T> extends  Throwable{

}*/
    }
}




