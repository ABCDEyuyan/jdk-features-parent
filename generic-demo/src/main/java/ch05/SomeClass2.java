package ch05;

/**
 * T是个泛型参数，但是有约束
 * T只能是Number以及它的子类型
 *
 * T extends 后面只能跟一个类型吗？
 * 给类型参数添加上限约束，后面可以跟接口
 *
 * 如果有多个约束条件就用&连起来，并且类型放在前面，
 * 而且不能有多个类作为约束条件
 * T extends Number & Comparable<T>  (ok)
 * T extends   Comparable<T> & Number (error)
 * T extends Number & String (error)单继承体系的，不能有多个类作为约束
 *
 * 类型参数的约束只有上限，没有下限
 *
 * ？是有上限和下限的。
 * @param <T>
 */
public class SomeClass2 <T extends Number  >{

    public double add(T t1, T t2) {
        //因为T是Number类型，所以就可以有一些成员可以访问
        return t1.doubleValue() + t2.doubleValue();
    }

    public static void main(String[] args) {
        double result = new SomeClass2<Integer>().add(5, 6);

    }
}


