package com.nf.stream;

public class Main {
    public static void main(String[] args) {
        MyStreamImpl<Integer> integerStream = IntergerStreamGenerator.getIntegerStream(5, 10);//[5,10]
        integerStream
                .filter(i -> i > 6)
                .limit(2)
                .forEach(System.out::println);
    }
}
