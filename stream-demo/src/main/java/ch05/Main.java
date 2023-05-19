package ch05;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //mapReview();

        //flatMapDemo1();

        Stream<String> stringStream = Stream.of("a", "b", "c");
//flatMap变成了一个流中流，内部流分别是Stream.of(a),Stream.of("b"),Stream.of("c")
        //stringStream
        // .flatMap(s -> Stream.of(s)) //flatMap的函数接口返回的是Stream<>类型
        // .forEach(System.out::println);

        //只有2个元素，分别list（1,2） ，list（3,4,5）
        Stream<List<Integer>> listStream = Stream.of(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4,5));

        //此时Stream<INteger>里面都是整数，只有5个值1,2,3,4,5
        Stream<Integer> integerStream = listStream
                .flatMap(list -> list.stream());
        integerStream
                .forEach(System.out::println);

        //小结：1.flatMap方法传递的lambda表达式的总目标是：
        // 把原来流中的每一个元素变成一个流，此时就形成流中流
        // 2.经过flatMap运算（也就是此函数的返回值）相当于把流中流已经拆开


    }

    private static void flatMapDemo1() {
        Stream<String> stream = Stream.of("a", "b");
        //stream.forEach(System.out::println);
        //一个大盒子里面有2个小盒子
        Stream<Stream<String>> stream1 = Stream.of(Stream.of("a", "b"), Stream.of("c", "d", "e"));
        //stream1.forEach(System.out::println);
        stream1
                .flatMap(stringStream -> stringStream)
                .forEach(System.out::println);
    }

    private static void mapReview() {
        Stream<String> stream = Stream.of("aaa", "adfaf", "ccc");

        Stream<Integer> integerStream = stream.map(s -> s.length());
        Stream<String> stream1 = stream.map(s -> s.substring(0, 1));

        stream.map(s->new Student(s));
    }
}
class Student{
    private  String name;
    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }
}