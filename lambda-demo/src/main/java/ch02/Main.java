package ch02;

public class Main {
    public static void main(String[] args) {
        Jdk8Inf.sm1();

        Jdk8Inf inf2 = new Jdk8Inf() {
            @Override
            public void doSth() {
                System.out.println("接口中抽象方法的常规实现");
            }
        };
        //inf2.m2();

        Jdk8Inf inf3 = new SomeImpl();
        inf3.m2();
    }
}
