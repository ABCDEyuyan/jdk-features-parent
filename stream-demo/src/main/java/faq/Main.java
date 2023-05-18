package faq;

public class Main {

    // == 操作是比较2个对象是否是同一个对象
    // equals是指的是2个对象的值是否相等
    // ==为true，那么一定是equals也为true
    public static void main(String[] args) {
        //是因为一些包装类型在某些值得范围是有缓存的，Integer就是【-128,127】
        Integer a = 120; //等价于 Integer.vauleOf(120)
        Integer b = 120;
        System.out.println(a == b);//true
        Integer c = 128;
        Integer d = 128;
        System.out.println(c == d); //false


        int e = 128;
        int f = 128;
        //这里由于是基本类型，只是进行纯粹的数字的比较，没有所谓的是否指向同一个对象的问题。
        System.out.println(e == f);//true

//所以关于整数，就很有必要创建2种类型的流，一个是包装的类型，一个是基本类型
//        IntStream stream;
//        stream.filter(i->i==5);

//        Stream<Integer> stream1;
//        stream1.filter((i->i==5));

    }
}
