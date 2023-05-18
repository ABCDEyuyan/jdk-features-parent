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

