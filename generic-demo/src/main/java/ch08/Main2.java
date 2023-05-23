package ch08;

public class Main2 {
    public static void main(String[] args) {
        // basicDemo();
        // basiceDemo2();
        // basiceDemo3();
        basicDemo4();
    }

    private static void basicDemo() {
        MyFunction<String, Number> f1 = (s) -> 100;
        Number num = f1.apply("asdf");
        System.out.println(num);
    }

    private static void basiceDemo2() {
        MyFunction<String, Number> f1 = s -> s.length() * 1.5;
        MyFunction<Number, Integer> f2 = num -> 5;
        MyFunction<Integer, String> f3 = i -> "abc";
        MyFunction<String, Integer> f1f2 = f1.andThen(f2);

        //f2.andThen(f1);//报错
        MyFunction<Number, String> f2f3 = f2.andThen(f3);

        System.out.println(f1.apply("asdf"));//string->number (asdf->6)
        System.out.println(f2.apply(6));//Number->Integer  6-->5
        System.out.println(f1f2.apply("asdf")); //5
    }

    private static void basiceDemo3() {
        MyFunction<String, Number> f1 = s -> s.length() * 1.5;
        MyFunction<Number,Integer> f2 = num-> 5;
        MyFunction<Object, Integer> f3 = num -> 6;
        MyFunction<String, Integer> f1f2 = f1.andThen2(f2);

        MyFunction<String, Integer> f1f3 = f1.andThen2(f3);
    }

    private static void basicDemo4() {
        MyFunction<String, Number> f1 = s -> s.length() * 1.5;
        MyFunction<Number,Number> f2 = num-> 5;
        MyFunction<Number, Integer> f3 = num -> 6;
        MyFunction<String, Number> f1f2 = f1.andThen3(f2);

        MyFunction<String, Integer> f1f3 = f1.andThen3(f3);
        MyFunction<String, Number> f1f3_2 = f1.andThen3(f3);
        MyFunction<String, Object> f1f3_3 = f1.andThen3(f3);

        Integer f1f3Apply = f1f3.apply("2");

        Integer f1f3_2Apply = f1f3.apply("2");
        Number f1f3_22Apply = f1f3.apply("2");
        Object f1f3_222Apply = f1f3.apply("2");

    }

}

