package ch03;

public class Main {
    //lambda表达式是一个方法的引用，类似于c#中委托
    //lambda表达式通常会赋值给函数接口（只有一个抽象方法的接口）

    public static void main(String[] args) {
        //1.lambda主要是可以赋值给一个函数接口
        //2.接口里面只能有一个抽象方法
        //3. lambda表达式相当于是给接口中的方法添加了一个实现
        //4. lambda表达式的签名必须与接口的签名一样（参数类型一样，个数一样，返回值一样）

        //5.下面的写法是最完整的写法
        // (把Inf1改成抽象类A会报错，错误提示信息告知只能是接口)
        Inf1 inf1 = (int a, int b) -> {

            System.out.println("1");
            System.out.println("2");
            //return 10;
            return a + b;
        };
        int result = inf1.add(5, 6);
        System.out.println(result);


        //参数类型可以省略
        Inf1 inf2 = (a, b) -> {
            return a + b;
        };
        //不能像下面这样写，要么都省略，要么都不省略
        //Inf1 inf22 = (a,int b) -> {return a + b;};


        //如果只有一行代码，大括号是可以省略的，return语句也可以省略
        Inf1 inf3 = (a, b) -> a + b;
        System.out.println(inf3.add(2, 3));
        //下面报错
       /* Inf1 inf33 = (a,b) ->  {
            System.out.println("sadf");
            (a + b);};*/

        //空的小括号，表示是无参。不能省略这个小括号
        Inf2 inf21 = () -> System.out.println(555);

        //如果只有一个参数，小括号是可以省略的
        Inf3 inf31 = (s) -> s.length();

        SomeClass someClass = new SomeClass();
        someClass.doSth(s -> s.length() + 5);

        //方法返回类型是接口，内部是可以返回一个lambda的
        System.out.println(someClass.doSth2().m("ab"));

    }
}
