package faq;

public class Main4 {
    public static void main(String[] args) {
        //字符串流怎么变成一个字符流
        //下面的方法不行，因为s.chars返回的是IntStream，而flatMap所要的参数要求的是Stream<T>
        //Stream.of("abc").flatMap(s -> {s.chars()})

        //---下面打印的是字符的ascii值
        //        Stream
        //                .of("abc")
        //                //toCharArray==>char[] ,Stream<char[]>
        //                .flatMap(s -> s.chars().boxed())
        //                .forEach(System.out::println);


 /*       Stream
                .of("abc")
                //toCharArray==>char[] ,Stream<char[]>
                .flatMap(s -> s.chars()
                        .boxed())
                .forEach(c -> System.out.println((char) c.intValue()));
*/

/*        Stream
                .of("abc")
                //toCharArray==>char[] ,Stream<char[]>
                .flatMap(s -> s.chars().mapToObj(i->(char)i))
                .forEach(c -> System.out.println(c));*/

  /*      Stream.of("abc")
                .map(s->s.split(""))
                .flatMap(Stream::of)
                .forEach(System.out::println);*/


    }
}
