package ch08;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class Main2 {
    public static void main(String[] args) {
         //basicDemo();
        //basiceDemo2();
         basiceDemo3();
        //basicDemo4();
    }

    private static void basicDemo() {
        MyFunction<String, Number> f1 = (s) -> 123;
        Number num = f1.apply("asdf");

        System.out.println(num);
    }

    private static void basiceDemo2() {
        MyFunction<String, Number> f1 = s -> s.length() * 1.5;
        MyFunction<Number, Integer> f2 = num -> 5;
        MyFunction<Object, Integer> f3 = i -> 100;
        MyFunction<String, Integer> f1f2 = f1.andThen(f2);

        // f2.andThen(f1);//报错
        // f1.andThen(f3);//报错 f1的执行结果是Number，f3的入参是Object，不匹配,只能是Number

        System.out.println(f1.apply("asdf"));//string->number (asdf->6)
        System.out.println(f2.apply(6));//Number->Integer  6-->5
        System.out.println(f1f2.apply("asdf")); // 5
    }

    private static void basiceDemo3() {
        MyFunction<String, Number> f1 = s -> s.length() * 1.5;
        MyFunction<Number, Integer> f2 = num -> 5;
        /*表明f3的入参是Object类型，也就是f3能处理Object类型的数据，而f1返回的是number，
         * number就是Object，所以f1与f3应该能进行andThen操作，为了能达成这个有逻辑意义的操作，
         * 那么andThen2方法的第一个参数就需要声明为? super R，
         * 否则的话，只能是f2这样的声明才能与f1进行andThen操作（类似basiceDemo2的情况），限制太死*/
        MyFunction<Object, Integer> f3 = num -> 6;
        MyFunction<String, Integer> f1f2 = f1.andThen2(f2);
        MyFunction<String, Integer> f1f3 = f1.andThen2(f3);
        BiConsumer<String,Integer> f11= (t,n)->{};
        BiPredicate<String,String> fbi=(s,n)-> false;
    }

    private static void basicDemo4() {
        MyFunction<String, Number> f1 = s -> s.length() * 1.5;
        MyFunction<Number, Number> f2 = num -> 6.5;
        /* f1f2的声明就表明andThen3方法里面T是String，V是Integer
        进而表明andThen3方法的参数为：? super String,? extend Integer
        而f2的声明，表明其返回类型是Number，Number不是Integer的子类，所以下面的这行代码报错 */
        //MyFunction<String, Integer> f1f2 = f1.andThen3(f2); //报错

        MyFunction<String, Number> f1f2_2 = f1.andThen3(f2);
        MyFunction<String, Object> f1f2_3 = f1.andThen3(f2);

        /* f1f2_2 与f1f2_3调用apply方法后，虽然实际返回的类型都是Number，
        但用Number与Object接收结果是符合逻辑的，所以，andThen3方法的第二个参数
        声明为? extends V的好处是对f1f2_2,f1f2_3变量的类型限制就宽泛一些，
        否则的话就只能是MyFunction<String,Number>了
        * */
        Number result = f1f2_2.apply("abc");
        Object result2 = f1f2_3.apply("abc");

    }

}

