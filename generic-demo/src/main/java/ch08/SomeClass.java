package ch08;

public class SomeClass<T> {
    /**
     * SomeClass<String> sc = new SomeClass<String>
     * sc.m1("dasdf");
     *
     * @param t
     */
    public void m1(T t) {

    }

    /**
     * 因为泛型类型的使用分为2部曲，
     * 第一步：泛型类型调用或者叫参数化，
     * 目标是确定声明的类型变量的具体类型（实参，type argument）
     * 第二步：真正的使用，所有的Type Parameter就用类型实参代替。
     * 这样就可以保证存放与获取的是同样类型的数据。
     *
     * SomeClass.sm1(传递数据）//少了第一步，T还没有确定是什么实参
     * 这就表示什么数据都可以传递，这就不符合泛型的初衷。
     * @param t
     */
  /*  public  static void sm1(T t) {

    }*/

    /**
     * SomeClass.sm2("asdf")
     * <p>
     * 静态方法不能使用所在类型上面声明的类型变量
     *
     * @param t
     * @param <U>
     */
    public static <U> void sm2(U t) {

    }
}
