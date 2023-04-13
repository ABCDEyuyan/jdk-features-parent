package ch03;

public class SomeClass {
    public void doSth(Inf3 inf3) {
        int result = inf3.m("abc");
        System.out.println("做了别的事。。。" + result);
    }


    public Inf3 doSth2( ) {
        return s->s.length();
    }
}
