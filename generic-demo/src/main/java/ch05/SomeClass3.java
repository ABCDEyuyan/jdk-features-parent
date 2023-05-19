package ch05;

public class SomeClass3 {
    //这样演示的是类型参数的上限可以是一个接口
    public <T extends Comparable<T>> T max(T t1, T t2) {
        return t1.compareTo(t2) >= 0 ? t1 : t2;
    }

    public <T extends Number & Comparable<T>> T max2(T t1, T t2) {
       return null;
       // return t1.compareTo(t2) >= 0 ? t1 : t2;
    }
}
