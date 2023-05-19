package faq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //asList返回的是一个只读的list实现
        List<Integer> list = Arrays.asList(1, 2);
        //下面2个操作都会报错，因为只读
       // list.add(3);
        //list.remove(0);
        //可以修改，但建议不要修改，仅仅只是当做只读的数据来看待
        list.set(0, 100);
        System.out.println(list.get(0));


        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        List<Integer> integers = Collections.unmodifiableList(list2);
        //integers.add(5);
        list2.add(5);
        //创建出只读集合的原始数据有变化，这个只读集合也会跟着变。
        integers.forEach(System.out::println);
    }
}
