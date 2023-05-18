package ch08;

public class Main2 {
    public static void main(String[] args) {
        //basicDemo();

       // basiceDemo2();

        //basiceDemo3();
        basicDemo4();
    }

    private static void basicDemo4() {
        MyFunction<String,Number> f1 = s-> s.length() * 1.5;
        // MyFunction<Number,Number> f2 = num-> 5;
        MyFunction<Object,Integer> f2 = num-> 5;
        MyFunction<String, Number> myFunction = f1.andThen3(f2);
    }

    private static void basiceDemo3() {
        MyFunction<String,Number> f1 = s-> s.length() * 1.5;
        // MyFunction<Number,Integer> f2 = num-> 5;
        MyFunction<Object,Integer> f2 = num-> 5;
        MyFunction<String, Integer> myFunction = f1.andThen2(f2);
    }

    private static void basiceDemo2() {
        MyFunction<String,Number> f1 = s-> s.length() * 1.5;
        MyFunction<Number,Integer> f2 = num-> 5;
        // MyFunction<Integer, String> f3 = i -> "abc";
        MyFunction<String, Integer> myFunction = f1.andThen(f2);

        //f2.andThen(f1);//报错
        //MyFunction<Number,String> myFunction1 = f2.andThen(f3);

        System.out.println(f1.apply("asdf"));//string->number (asdf->6)
        System.out.println(f2.apply(6));//Number->Integer  6-->5
        System.out.println(myFunction.apply("asdf")); //5
    }

    private static void basicDemo() {
        //叫做泛型类型调用（generic type invocation）
        // 或者叫Parameterized Generic type
        //cj:泛型类是一个工厂，一个模板，它可以用来创建出无数个具体的类型出来
        //比如MyFunction<T,R>  ----> f1类型
        //创建出的这个新类，表明了T，R具体是什么。这样才能使用
        MyFunction<String,Number> f1= (s)->100;
        Number num = f1.apply("asdf");
        System.out.println(num);

        //SomeClass.sm1("asdfsd");
    }
}

class SomeClass<T>{
    /**
     * SomeClass<String> sc = new SomeClass<String>
     *     sc.m1("dasdf");
     * @param t
     */
    public   void m1(T t) {

    }

    /**
     * 因为泛型类型的使用分为2部曲，
     * 第一步：泛型类型调用或者叫参数化，
     * 目标是确定声明的类型变量的具体类型（实参，type argument）
     * 第二步：真正的使用，所有的Type Parameter就用类型实参代替。
     * 这样就可以保证存放与获取的是同样类型的数据。
     *
     * SomeClass.sm1(传递数据）//少了第一步，T还没有确定是什么实参
     * 这就表示什么数据都可以传递，这就不符合泛型的初衷。
     * @param t
     */
  /*  public  static void sm1(T t) {

    }*/

    /**
     * SomeClass.sm2("asdf")
     *
     * 静态方法不能使用所在类型上面声明的类型变量
     * @param t
     * @param <U>
     */
    public  static <U> void sm2(U t) {

    }
}

