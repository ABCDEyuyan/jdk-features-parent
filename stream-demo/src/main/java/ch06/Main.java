package ch06;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Main {
    public static void main(String[] args) {
        //collect(收集）

        //1.变成一个List
        List<String> collect = Stream.of("a", "b", "c", "a")
                .collect(Collectors.toList());
        //2.变成一个set集合（里面没有重复元素
        Stream.of("a", "b", "c", "a")
                .collect(toSet()) //利用了静态方法导入
                .forEach(System.out::println);
        //3.变成map

        //如果有重复键的可能，就会报错
        Map<String, Integer> map = Stream.of("a", "bxxx", "cd")
                .collect(Collectors.toMap(k -> k, v -> v.length()));
        map.forEach((k, v) -> System.out.println("key: " + k + " value:" + v));

        //4 数组
        Object[] objects = Stream.of("a", "b").toArray();
    }
}
