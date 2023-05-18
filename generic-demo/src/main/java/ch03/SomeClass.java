package ch03;

public class SomeClass {

    //泛型方法声明，
    //1.泛型信息声明放在返回类型前面
    //2. 方法上的类型参数只能在方法内部使用
    public <U> void m1(){

    }

    public <U> U m2(U u){

        return null;
    }
}
