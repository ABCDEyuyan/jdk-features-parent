package ch04;

public class MyInfImpl3<T,U> implements MyInf<U>{
    @Override
    public void set(U data) {
        System.out.println("set1----" +data);
    }

    public void set2(U data, T d) {
        System.out.println("set2----" +data + "----" + d);

    }
}
