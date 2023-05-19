package ch07;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //reduce（归纳）


        //reduce方法的第二个重载（只有一个参数）accu accumulate（累计）
        //        Integer integer = Stream.of(3, 4, 5,6,7).reduce((accu, num) -> {
        //            System.out.println("accu:" + accu + " num:" + num);
        //            return accu + num;}).get();
        //System.out.println(integer);


        //这种重载不需要调用get
        //reduce方法的第一个参数是每个执行分支（并行流的情况下）的起始种子（seed）值
        Integer result = Stream.of(3, 4, 5).parallel()
                .reduce(0, (accu, num) -> {
                    System.out.println("accu:" + accu + " num:" + num);
                    return accu + num;
                });

        System.out.println(result);

    }
}
