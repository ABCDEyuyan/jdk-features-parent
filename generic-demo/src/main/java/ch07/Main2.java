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

    /**
     * 用上通配符，能对其进行的操作是非常有限的
     * @param numers
     * @return
     */
    public static void test(List<?> numers) {
//        numers.add(5);
//        numers.add("5");
//        numers.add(new Object());
        // Object o = numers.get(0);
    }
}
