package ch04;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 流的执行特点
 * 1.一般没有终止操作，中间指定的lambda不会执行
 * 2.流水线似的执行方式
 * 3.让流的量变小的操作放在前面
 * 4.有些特殊的操作，比如sort就不是流水线似的操作
 */
public class Main {
    public static void main(String[] args) {
        //chixu();
        //cixu2();
        //streamClosed();
        //tuijian();
      Stream.of(2,8,6,7,5)
              .limit(2) //limit要靠后一点
              .filter(num->num>5)
              .forEach(System.out::println);

    }

    private static void tuijian() {
        //写流的代码的总原则：先降低流中数据量

        //推荐，因为先过滤了，少了一些数据，所以sorted量就比较
        IntStream
               .of(10,2,15,1,8)
               .filter(i->i>7)
               .sorted()
               .forEach(System.out::println);
        //不应该像下面这样写
        IntStream
                .of(10,2,15,1,8)
                .sorted()
                .filter(i->i>7)
                .forEach(System.out::println);
    }

    private static void streamClosed() {
        Stream<String> stream = Stream.of("g", "a", "c");
        stream.forEach(System.out::println);//经过这一个最终操作之后，流已经关闭，不能再使用任何操作了
        stream.filter(s->true);//关闭之后，即使是中间操作也会异常
        //只要流经过最终操作导致流关闭之后，任何操作都不行
    }

    /**
     * 排序不像filter这种操作，它是有状态的（需要记忆东西），需要把所有的数据排序完毕
     * 才交给下一个操作。。。
     */
    private static void cixu2() {
        Stream.of("g", "a", "c")
                .sorted((s1, s2) -> {
                    System.out.printf(" sort:%s,%s \n", s1, s2);
                    return s1.compareTo(s2);
                })
                .filter(s -> {
                    System.out.println("filter:" + s);
                    return true;
                })
                .forEach(s -> System.out.println(" foreach:" + s));
    }

    /**
     * 常规情况
     * 1.由最终操作引发流的运算:
     * 从流中取得一个数据----》
     *          中间操作1---》
     *                  中间操作2----》。。。。 ---》最终的这一个最终操作（最终操作只能有一个）
     * <p>
     * <p>
     * 2.流开始运作---》取得数据--》中间操作1----停止
     * ^-----------------------|
     */
    private static void chixu() {
        Stream.of(1, 2, 3, 4, 5)
                .filter(s -> {
                    System.out.println("filter1:" + s);
                    //把下面的代码改为return true和return false分别理解执行顺序
                    return s % 2 == 0;
                })
                .map(s -> {

                    System.out.println("map:" + s);
                    return s * 1;
                })
                .forEach(s -> System.out.println("foreach:" + s));
    }
}
