package ch02;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        //1.从集合中创建流
        List list = new ArrayList();
        Stream stream = list.stream();
        //从map中创建流主要是靠其entrySet
        Map<String, Integer> map = new HashMap<>();
        Stream<Map.Entry<String, Integer>> stream1 = map.entrySet().stream();

        //2.数组中创建流
        String[] arr = {"a", "b"};
        //方式1
        Stream<String> sequential = Arrays.stream(arr).sequential();
        sequential.forEach(System.out::println);
        //方式二：
        Stream<String> arrStream2 = Arrays.stream(arr);

        //3.从字符串中创建流
        IntStream chars = "abc".chars();

        //4.基本类型的流主要是int，double，long

        IntStream intStream = IntStream.of(5, 6, 7, 8);
        IntStream intStream1 = IntStream.range(5, 9);//[5,9)
        IntStream intStream2 = IntStream.rangeClosed(5, 8);//[5,8]
        // 5.获取一个空流
        Stream<String> streamEmpty = Stream.empty();
        // 6 stream.of创建流
        Stream<String> streamOfArray = Stream.of("a", "b", "c");
        // 7 利用builder创建流
        Stream<String> streamBuilder =
                Stream.<String>builder().add("a").add("b").add("c").build();
        // 8 利用generate创建流（无限容量流）
        Stream<String> streamGenerated =
                Stream.generate(() -> "element").limit(10);
        // 9 利用iterate方法创建无限流
        Stream<Integer> streamIterated = Stream.iterate(40, n -> n + 2).limit(20);
        //10 文件流
        Path path = Paths.get("C:\\file.txt");
        Stream<String> streamOfStrings = Files.lines(path);
        Stream<String> streamWithCharset =
                Files.lines(path, Charset.forName("UTF-8"));
    }
}
