package ch04;

import java.util.function.*;

/**
 * lambda表达式出来之后，jdk帮我们已经创建了一些常见的接口
 * 主要在java.util包下面
 * <p>
 * Consumer 消费者：有参 accept() 接受
 * Supplier 提供者：有返回值 get() 获取
 * Predicate 谓语，断言：返回逻辑值 test() 测试
 * Function 函数：有参有返回值 apply() 应用
 * Bi：Binary（二元）
 * Unary（一元）
 * 泛型不能使用基本类型，所以有以下几类接口
 * 1.纯泛型的，比如BiConsumer
 * 2.纯基本类型，比如IntConsumer
 * 3.对象与基本类型共有：ObjIntConsumer
 * <p>
 * 看到BI这种指的就是Binary，也就是二元运算，比如求和运算a + b就是二元运算，因为有2个数参与
 * <p>
 * 看到Unary就表示是一元运算：比如i++
 */
public class Main {
    public static void main(String[] args) {

        consumerDemo();

        //supplierDemo();

        //predicateDemo();

        //functionDemo();


    }

    private static void functionDemo() {
        Function<String, Integer> function = s -> s.length();
        Integer result = function.apply("abc");
        System.out.println(result);
    }

    private static void predicateDemo() {
        Predicate<String> predicate = s -> s.length() > 3;
        boolean result = predicate.test("abcd");
        System.out.println(result);
    }

    private static void supplierDemo() {
        Supplier<Double> supplier = () -> 10.0;
        System.out.println(supplier.get());
    }

    private static void consumerDemo() {
        Consumer<String> consumer = s -> System.out.println("s");
        consumer.accept("abc");

        BiConsumer<String, Integer> consumer1 = (a, b) -> {
            System.out.println(a.length() + b);
        };

        consumer1.accept("abc", 5);


        IntConsumer intConsumer = i -> System.out.println(i);
        intConsumer.accept(11);

        ObjIntConsumer<String> objIntConsumer = (s, i) -> System.out.println(s.length() + i);
        objIntConsumer.accept("aaa", 11);
    }
}
