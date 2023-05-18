package ch02;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        //genericTypeInvocation();

        //RawType:原生类型  去掉泛型信息之后的类型

        //有了泛型之后，尽量不要用非泛型的任何形式
        Box box = new Box(); //不建议使用
        Box<Integer> integerBox = new Box<>();
        box = integerBox;//不建议
        integerBox = box; //不建议
    }

    private static void genericTypeInvocation() {
        //1.实例化一个泛型类方法一（最完整的写法）：
        //2.Integer,String等是类型实参（Type Argument）
        // 3.Box<Integer>称之为泛型类型调用（ a generic type invocation）
        // 也可以叫 泛型类型声明或者叫parameterized type(已经参数化了的类型）
        Box<Integer> integerBox = new Box<Integer>();

        //写法二：赋值的只写<>即可，因为编译器的类型推断
        Box<String> stringBox = new Box<>();

        Box<List<Integer>> listBox = new Box<>();
    }
}
