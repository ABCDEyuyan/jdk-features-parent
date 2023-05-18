package ch07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main3 {
    public static void main(String[] args) {
        List<Object> objectList = null;
        List<Number> numbers = null;
        List<Integer> integers = new ArrayList<Integer>(){{add(1);add(2);add(3);}};
       // print(objectList);
       // print(numbers);
        print(integers);

        Arrays.asList();

    }

    public static void print(List<? super Integer> list) {
        Integer object = (Integer) list.get(0);
        System.out.println(object);
        list.add(5);
        System.out.println(list.get(3));
    }
}
