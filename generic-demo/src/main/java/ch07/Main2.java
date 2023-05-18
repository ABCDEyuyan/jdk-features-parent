package ch07;

import java.util.Arrays;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
        System.out.println(sum(integers));
        System.out.println(sum(doubles));
    }

    public static double sum(List<? extends Number> numers) {
        double sum = 0;
        for(Number num:numers){
            sum += num.doubleValue();
        }
        return sum;
    }
}
