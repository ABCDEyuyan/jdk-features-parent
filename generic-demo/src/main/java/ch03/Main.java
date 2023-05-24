package ch03;

/**
 * 泛型方法的声明与使用
 */
public class Main {
    public static void main(String[] args) {


        SomeClass2<Integer> s = new SomeClass2<>();
        s.<String>m();//完整的泛型方法调用语法

        s.m();//可以简化

        s.m2(100, "asdf");//因为你传递的参数是字符串，所以推断出方法的U类型形参的实际参数是字符串
        s.<Double>m2(100, 11.2);
        s.m2(100, 11.2);

        NormalClass normalClass = new NormalClass();
        String result = normalClass.m("asdf");
        System.out.println(result);

        System.out.println(NormalClass.sm(100));
    }
}
