package ch09;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // primitive();
        //  constraint4();
        Object[] strings = new String[2];
        strings[0] = "hi";   // OK，strings最终指向的是字符串数组，放字符串可以的
        //字符串数组是不能放非字符串数据
        strings[1] = 100;
    }

    private static void constraint5() {
        //约束5：不能创建泛型化的类型数组
        //因为数组有协变行，所以字符串数组可以赋值给Object数组
        Object[] strings = new String[2];
        strings[0] = "hi";   // OK，strings最终指向的是字符串数组，放字符串可以的
        //字符串数组是不能放非字符串数据
        strings[1] = 100;    // An ArrayStoreException is thrown.

        // Object[] stringLists = new ArrayList<String>[];  // compiler error, but pretend it's allowed
        //因为泛型擦除之后，没有ArrayList<String>与ArrayList<Integer>的区别
        //所以放的2个数据都是ArrayList
        //stringLists[0] = new ArrayList<String>();   // OK
        //stringLists[1] = new ArrayList<Integer>();  // An ArrayStoreException should be thrown,
        // but the runtime can't detect it.
    }

    private static void constraint4() {
        //约束4：不能对参数化得泛型类使用instanceof或者进行类型转换
        // List<String> list = null;
        //if(list instanceof ArrayList<String>)//不行，因为类型擦除
        //else if(list instanceof ArrayList<Integer>)
    }

    //约束1：不能用基本类型
    private static void primitive() {
        //因为泛型是一个编译器层面的技术---》擦除
        //--->add(T e)--->add(e)(X)-->add(Object e)
        // 基本类型不能转换为Object o = 5;  //自动的装箱+ 向上转型
        // List<int> list;//
    }
}

class SomeClass<T> {
    //static  T t;//约束3：类似前面说的静态方法逻辑


    //约束2：类型参数不能实例化
    public void m() {
        // T t  = new T();
    }
}

//约束6：不能创建泛型类，它是继承异常
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


//约束7：方法参数类型不能是参数化的泛型类，这样不形成重载
class Example {
    //擦除之后都是set类型，参数一样，不叫重载，所以编译出错
    //public void print(Set<String> strSet) { }
    public void print(Set<Integer> intSet) {
    }
}