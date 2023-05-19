package ch04;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class Main {
    public static void main(String[] args) {
        //basic1();

        //basic2();

        MyInf<String> inf = new MyInfImpl3<Double, String>();
        //下面的报错，因为接口的类型实参是String，然后MyInfImpl3实现类的
        //第二个类型实参与接口是一样的，但下面用的是Double，不一致，所以报错
        //MyInf<String> inf2 = new MyInfImpl3<Double, Double>();
        MyInf<String> inf3 = new MyInfImpl3<Integer, String>();
        inf3.set("dasf");  //必须是String

        ((MyInfImpl3<Integer, String>)inf3).set2("asdf",100);
    }

    //子类型可以改变父类型的类型参数的意义
    private static void basic2() {
        BiFunction<String,Double,Integer> biFunction = (a,b)-> 100;

        BinaryOperator<String>  operator = (a,b)-> a + b;
    }

    private static void basic1() {
        MyInf<String> myInf = new MyInfImpl1<>();

        MyInfImpl1<Integer> infImpl1 = new MyInfImpl1<>();
        MyInfImpl1<String> infImpl2 = new MyInfImpl1<>();
        //myInf= infImpl1; //报错
        myInf = infImpl2;
    }
}
