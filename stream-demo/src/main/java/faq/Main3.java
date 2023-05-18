package faq;

import java.util.Arrays;
import java.util.stream.Stream;

public class Main3 {
    public static void main(String[] args) {
        //primitiveAsListIssue();
        int[] arr1 = {1, 2, 3};
        String[] arr2 = {"aaa", "bbb"};
        Stream.of(arr2).forEach(System.out::println);

    }

    private static void primitiveAsListIssue() {
        int[] arr1 = {1, 2, 3};
        String[] arr2 = {"a", "b"};
        //
//        List list;
//        list.forEach();

        //因为arr1是一个基本类型的数组，而asList方法需要的是T...
        //意思就是T必须是一个非基本类型的
        //所以导致asList方法把arr1当成一个数据，类型是int数组
        //而arr2当成的是字符串数组。。。。

        //当传递arr1  List<int[]> ,
        //当传递arr2  List<String>
        Arrays.asList(arr1).forEach(System.out::println);
        System.out.println(arr1);
        //Main3.mm("1","a");
        Main3.mm(arr2);
    }


    static void mm(String... data){
        System.out.println(data.getClass());
    }
}
