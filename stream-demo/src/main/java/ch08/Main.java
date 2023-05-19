package ch08;

import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        IntSummaryStatistics intSum = IntStream.of(3, 4, 5).summaryStatistics();

        //三种基本类型流都一个返回统计信息的方法summaryStatistics
        //LongStream.of(222).summaryStatistics()

        //统计信息有最大，最小，平均等
        System.out.println(intSum.getMin());
        System.out.println(intSum.getMax());
        System.out.println(intSum.getSum());
        System.out.println(intSum.getAverage());
        System.out.println(intSum.getCount());


    }
}
