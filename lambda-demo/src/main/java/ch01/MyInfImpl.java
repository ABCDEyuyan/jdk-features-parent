package ch01;

public class MyInfImpl implements Inf1{
    @Override
    public void doSth() {
        System.out.println("my inf impl1--");
    }

    @Override
    public void doSth2(int b) {
        System.out.println(b);
        b=2;//被覆盖了
    }
}
