package ch10;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Main {
    public static void main(String[] args) {
//分组之后的结果永远是一个Map<键，值》
        //groupDemo1();
        //groupDemo2();
        Student s1 = new Student(1, "a", 1000.5, "hr");
        Student s2 = new Student(2, "b", 3000.5, "rd");
        Student s3 = new Student(3, "c", 5000.5, "hr");
        Student s4 = new Student(4, "d", 10000.5, "rd");
        Student s5 = new Student(5, "e", 3000.5, "rd");
        List<Student> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        list.add(s5);


        Map<String, DoubleSummaryStatistics> collect = list
                .stream()
                .collect(groupingBy(s -> s.getDeptName(), Collectors.summarizingDouble(s -> s.getSalary())));
        collect.forEach((k, v) -> System.out.println("key:" + k + " value:" + v));


    }

    private static void groupDemo2() {
        Map<Integer, Long> collect = Stream
                .of("a", "1", "bb", "22", "ccc", "333", "xxx", "d")
                .collect(groupingBy(s -> s.length(),
                        Collectors.counting()));

        collect.forEach((k, v) -> System.out.println("key:" + k + " value:" + v));
    }

    private static void groupDemo1() {
        //1:a,1,d;2:bb,22;3:ccc,333
        Map<Integer, List<String>> collect = Stream
                .of("a", "1", "bb", "22", "ccc", "333", "xxx", "d")
                .collect(groupingBy(s -> s.length()));
        collect.forEach((k, v) -> System.out.println("key:" + k + " value:" + v));


        ArrayList<String> manArray = new ArrayList<>();
        manArray.add("刘德华");
        manArray.add("成龙");
        manArray.add("吴彦祖");
        manArray.add("周润发");
        manArray.add("周星驰");
        manArray.add("吴京");

        Map<String, List<String>> collect1 = manArray
                .stream()
                .filter(s->true)
                .collect(groupingBy(s -> s.substring(0, 1)));
        collect1.forEach((k, v) -> System.out.println("key:" + k + " value:" + v));
    }
}
