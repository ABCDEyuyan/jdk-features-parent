package faq;

import java.util.stream.Stream;

public class Main2 {
    public static void main(String[] args) {
        Stream<String> stream = Stream.of("a", "bbb");

        //产生一个新的流，用stream1指代，老的流stream就会设置为“关闭”状态
        Stream<String> stream1 = stream.filter(s -> s.length() > 2);
        //stream已经关闭，再追加操作就会报异常
        stream.forEach(System.out::println);
    }
}
