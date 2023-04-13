package ch03.methodref.variant;

public class SomeClass2 {
    public static A m1(){
        System.out.println("a");
        return new A();
    }

    public static B m2(){
        System.out.println("b");return new B();
    }

    public static   C m3(){
        System.out.println("c");
        return new C();
    }
}
