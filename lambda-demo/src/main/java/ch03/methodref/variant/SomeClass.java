package ch03.methodref.variant;

public class SomeClass {
    //参数类型是A，我可以传递B，C对象进去
    public static void m1(A a){
        System.out.println("a");
    }

    public static void m2(B a){
        System.out.println("b");
    }

    //只能传递C和C的子类，也就是不能传递B，也就不符合Inf1的doSth
    public static   void m3(C a){
        System.out.println("c");
    }
}
