package ch09;

import java.util.stream.LongStream;

/**
 * 并行流有两大类创建方式
 * 1.一种是某些对象本身就有方法创建，比如List有parallelStream
 * List list;
 * list.parallelStream();
 * 2.已经有stream对象的情况下，调用parallel方法
 * Stream stream;
 * stream.parallel();
 */
public class Main {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        LongStream longStream = LongStream.rangeClosed(1, 1000000000);
        long sum1 = longStream.reduce(0, Long::sum);
        long end = System.currentTimeMillis();

        System.out.println("和：" + sum1 + " 耗时: " + (end-start) + "毫秒");


       /* long start2 = System.currentTimeMillis();
        LongStream longStream2 = LongStream.rangeClosed(1, 1000000000).parallel();
        long sum2 = longStream2.reduce(0, (l1,l2)->{
            //System.out.println(Thread.currentThread().getName() + " --" + l1 +"--" + l2);
            return (l1+l2);
        });
        long end2 = System.currentTimeMillis();

        System.out.println("和：" + sum2 + " 耗时: " + (end2-start2) + "毫秒");
*/


    }
}
