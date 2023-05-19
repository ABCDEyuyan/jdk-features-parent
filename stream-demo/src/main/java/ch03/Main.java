package ch03;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        IntStream stream = IntStream.of(1, 2, 3, 5, 2, 4, 5, 4, 8);

        stream
                .filter(i->i%2 ==0) //2,2,4,4,8
                .map(i->i*2) //4,4,8,8,16
                .skip(2)//8,8,16
                .limit(2)//8,8
                .distinct().//8
                forEach(System.out::println);
    }
}
