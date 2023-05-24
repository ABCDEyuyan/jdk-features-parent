package impl;

import impl.stream.MyStream;
import impl.stream.Stream;
import impl.stream.genetator.IntegerStreamGenerator;

public class TestMyStream {

    public static void main(String[] args){
        demo1();
        //demo2();
        //demo3();
    }

    private static void demo1(){
        MyStream<Integer> integerStream = IntegerStreamGenerator.getIntegerStream(5, 10);
        //limit没有返回新的stream，map等操作是返回新的stream
        MyStream<Integer> limitStream = integerStream.limit(2);
        limitStream.forEach(System.out::println);
        System.out.println("==========");
        MyStream<Integer> integerStream2 = IntegerStreamGenerator.getIntegerStream(5, 10);
        integerStream2.forEach(System.out::println);
        //上面的代码等价下面的
/*        IntegerStreamGenerator.getIntegerStream(5, 10)
                .limit(2)
                .forEach(System.out::println);*/

    }

    private static void demo2(){
        Integer sum = IntegerStreamGenerator.getIntegerStream(1,10)
                .filter(item-> item%2 == 0) // 过滤出偶数
                .map(item-> item * item)    // 映射为平方
                .limit(2)                   // 截取前两个
                .reduce(0,(i1,i2)-> i1+i2); // 最终结果累加求和(初始值为0)

        System.out.println(sum); // 20
    }

    private static void demo3() {
        // 生成关于list的流
        Stream<Integer> intStream = IntegerStreamGenerator.getIntegerStream(1,10);
        // intStream基础上过滤出偶数的流
        Stream<Integer> filterStream =  intStream.filter(item-> item%2 == 0);
        // filterStream基础上映射为平方的流
        Stream<Integer> mapStream = filterStream.map(item-> item * item);
        // mapStream基础上截取前三个的流
        Stream<Integer> limitStream = mapStream.limit(2);

        // 最终结果累加求和(初始值为0)
        Integer sum = limitStream.reduce(0,(i1,i2)-> i1+i2);

        System.out.println(sum); // 20
    }

    private static boolean idOdd(int num){
        return (num % 2 != 0);
    }

    private static Integer square(int num){
        return num * num;
    }

    private static Integer scaleTwo(int num){
        return num * 2;
    }

    private static String toStr(int num){
        return num + "";
    }

}
