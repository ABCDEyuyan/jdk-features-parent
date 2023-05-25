package com.nf.stream;

public class Main {
    public static void main(String[] args) {
        //demo1();
        //demo2();
        demo3();
    }

    /**
     * 执行逻辑
     * <ol>
     *     <li>调用getIntegerStream，创建一个流，head = null，next=getIntegerStream(5,6,false)</li>
     *     <li>forEach:先调用上一步创建的流的eval方法，也就是会导致上一步next表达式会执行</li>
     *     <li>next表达式执行后会创建一个新的流，流的head值=5，next=getIntegerStream(6,6,false) </li>
     *     <li>forEach利用consumer消费上一步创建流的head值 </li>
     *     <li>foreach内部递归调用forEach(consumer, myStream.eval()) </li>
     *     <li>回到了第二步 </li>
     * </ol>
     */
    private static void demo1() {
        MyStreamImpl<Integer> integerStream = IntergerStreamGenerator.getIntegerStream(5, 6);//[5,6]
        integerStream.forEach(System.out::println);
    }

    private static void demo2() {
        MyStreamImpl<Integer> integerStream = IntergerStreamGenerator.getIntegerStream(5, 6);//[5,6]
        MyStreamImpl<Integer> filterStream = integerStream.filter(i -> i > 3);
    }

    private static void demo3() {
        MyStreamImpl<Integer> integerStream = IntergerStreamGenerator.getIntegerStream(5, 10);//[5,10]
        integerStream
                .filter(i -> i > 6)
                .limit(2)
                .forEach(System.out::println);
    }
}
