package ch01;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 如果想快速学习stream（不看这里的案例），看下面三篇文章就可以了(重点是前两篇）
 * https://www.baeldung.com/java-8-streams-introduction
 * https://www.baeldung.com/java-8-streams
 * https://developer.aliyun.com/article/764445 （介绍执行顺序问题）
 *
 * 本章案例用来了解流的基本操作模式：三部曲
 */
public class Main {
    public static void main(String[] args) {
       //操作流的基本步骤：1.创建流，2.追加一系列的中间操作，3选一个最终操作
        hello1();
        //hello2();
    }

    private static void hello2() {
        //集合创建流：调用stream方法，此方法属于接口Collection
        List<Integer> list = Arrays.asList(9, 12, 65);
        Stream<Integer> stream = list.stream();
        stream.filter(i -> i > 10).forEach(System.out::println);
    }

    private static void hello1() {
        Integer[] datas = {100, 200, 300};
        //1. 从数组中创建流
        Stream<Integer> stream = Arrays.stream(datas);
        //2.中间操作filter（intermediate operation）
        Stream<Integer> zhongjian = stream.filter(i -> i % 2 == 0);
        //3.终止操作foreach（最终操作，terminal）
        zhongjian.forEach(System.out::println);
    }
}
