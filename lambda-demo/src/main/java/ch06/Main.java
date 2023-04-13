package ch06;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        //andThen();
        //predicateAnd();
        //predicateNegate();
        //predicateOr();
        //identity();
        //function_andThen();
        //function_andThen_demo();
        //compose：组合，复合的意思
        function_compose();


    }

    private static void function_compose() {
        Function<Integer,Integer> f1 =s->{
            System.out.println("111:" + s);
            return s * 2;
        };
        Function<Integer,Integer> f2 =i->{
            System.out.println("222:" + i);
            return (i+ 10);
        };
        //先执行compose参数里面的f2这个lambda表达式，再执行f1
        Function<Integer, Integer> andThen = f1.compose(f2);//f2->f1
        Integer result = andThen.apply(3);
        System.out.println(result);
    }

    private static void function_andThen_demo() {
        Function<Integer,Integer> f1 = i->i*2;
        Function<Integer, Integer> andThen = f1
                .andThen(f1)
                .andThen(f1);
        Integer result = andThen.apply(3);
        System.out.println(result);
    }

    private static void function_andThen() {
        Function<String,Integer> f1 =s->{
            System.out.println("111:" + s);
            return s.length();
        };
        Function<Integer, Integer> f2 = i->{
            System.out.println("222:" + i);

            return (i+ 10);
        };
        //先执行f1还是先执行f2，第二个执行的lambda表达式的参数还是abcd吗？
        //先执行f1，后执行f2，第一个lambda执行的结果是第二个lambda的入参
        Function<String, Integer> andThen = f1.andThen(f2);
        Integer result = andThen.apply("abcd");
        System.out.println(result);


    }

    private static void identity() {
        //原样返回

        Function<String, String> identity = Function.identity();
        String result = identity.apply("abc");
        System.out.println(result);
    }

    private static void predicateOr() {
        Predicate<Integer> p1 = i -> i>10;
        Predicate<Integer> p2 = i -> i%2==0;
        Predicate<Integer> andPredicate = p1.or(p2);
        boolean result = andPredicate.test(8);
        System.out.println(result);
    }

    private static void predicateNegate() {
        Predicate<Integer> p1 = i -> i>3;

        Predicate<Integer> negatePredicate = p1.negate();
        boolean result = negatePredicate.test(23);
        System.out.println(result);
    }

    private static void predicateAnd() {
        Predicate<Integer> p1 = i -> i>3;
        Predicate<Integer> p2 = i -> i<30;
        Predicate<Integer> andPredicate = p1.and(p2);
        boolean result = andPredicate.test(23);
        System.out.println(result);
    }

    private static void andThen() {
        Consumer<String> c1 = (s)-> System.out.println("长度:" + s.length());
        Consumer<String> c2 = (s)-> System.out.println("首字母:" + s.substring(0,1));
        Consumer<String> consumer = c1
                .andThen(c2);//c1-->c2
        //consumer.accept("abcdef");
        Consumer<String> consumer1 = c1.andThen(c1).andThen(c2).andThen(c1);
        consumer1.accept("abc");
    }
}

