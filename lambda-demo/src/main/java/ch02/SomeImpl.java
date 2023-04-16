package ch02;

public class SomeImpl implements Jdk8Inf{
    @Override
    public int doSth(int a) {
        return 1;
    }

    @Override
    public void m2() {
        doSth(2);
        System.out.println("更改其默认实现");
    }
}
