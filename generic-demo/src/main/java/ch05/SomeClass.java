package ch05;

public class SomeClass<T> {

    /**
     * T这种东西，由于代表的是任意的类型，所以能对他进行的操作是有限制
     * 只有Object类里面的东西是可以用的
     * @param t
     * @param m
     */
    public void doSth(T t,T m) {
        //T in = new T();//不行，这种写法意味着调用无参构造函数

        //常规的算术运算符也不能使用。。。
       // Object result = t + m;

        //因为T代表任意类型，所以T上能有的操作很有限的，基本就是Object类的操作可以用
        boolean flag = t == m;
     //t.hashCode();

    }
}
