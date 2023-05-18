package ch01;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
       //操作流的基本步骤：1.创建流，2.追加一系列的中间操作，3选一个最终操作

        //hello1();

        hello2();
    }

    private static void hello2() {
        List<Integer> list = Arrays.asList(9, 12, 65);

        Stream<Integer> stream = list.stream();

        stream
                .filter(i -> i > 10)
                .forEach(System.out::println);
    }


    private static void hello1() {
        List<Integer> list = Arrays.asList(1, 8, 5);
        //1. 从集合中创建流
        Stream<Integer> stream = list.stream();

        //2.中间操作（intermediate operation）
        Stream<Integer> zhongjian = stream.filter(i -> i % 2 == 0);

        //3.终止操作（最终操作，terminal）
        zhongjian.forEach(System.out::println);
    }
}
