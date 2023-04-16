package ch02;

/**
 * 以前的接口主要是声明抽象的方法和常量
 * 接口的静态方法与默认方法可以有多个。
 */
public interface Jdk8Inf {
       int A = 10;
    int doSth(int a);

    static  void sm1(){
        System.out.println("接口中的静态方法");
    }

    default void m2(){
        doSth(3);
        System.out.println("默认方法，实例方法");
    }
}
